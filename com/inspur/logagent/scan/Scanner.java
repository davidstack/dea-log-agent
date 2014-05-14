package com.inspur.logagent.scan;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.TypeReference;

import com.inspur.logagent.common.AppLogInfo;
import com.inspur.logagent.common.ConfigUtil;
import com.inspur.logagent.common.Constants;
import com.inspur.logagent.common.FileInfo;
import com.inspur.logagent.common.FileUtils;
import com.inspur.logagent.common.LogDetail;
import com.inspur.logagent.common.RestClientUtil;
/**
 * 扫描 线程以及线程池
 * @author wangdekui
 *
 */
public class Scanner {

	private ScheduledExecutorService scheduExec = Executors
			.newScheduledThreadPool(1);

	
	
	public void beginScanTask() {
		scheduExec.scheduleWithFixedDelay(new ScanTask(), 1000 * 2, 1000 * 30,
				TimeUnit.MILLISECONDS);
	}

	/**
	 * 扫描线程
	 * 
	 * @author wang
	 * 
	 */
	private static class ScanTask implements Runnable {
		
		private static Log logger = LogFactory.getLog(ScanTask.class);
		private FileScanner fileScanner = new FileScanner();
		private RestClientUtil restClientUtil=new RestClientUtil();
		
		/**
		 * 本次扫描到的数据
		 */
		private List<FileInfo> scannedFiles = new ArrayList<FileInfo>();

		/**
		 * 保存上次扫描到的数据
		 */
		private List<FileInfo> lastScannedFiles = new ArrayList<FileInfo>();
		private List<FileInfo> updateFiles = new ArrayList<FileInfo>();
		
		private boolean firstRun=true;
		@Override
		public void run() {
			
			try {
				
				/**
				 *  clear old data
				 */
				scannedFiles.clear();
				updateFiles.clear();
				
				/**
				 * scan the root path
				 */
				scannedFiles = fileScanner.scan(Constants.scannRootPath);
				
				/**
				 * 获取详细信息 预防 运行日志文件在程序运行过程中才产生，所以每次全部 
				 */
				Iterator<FileInfo> iter=scannedFiles.iterator();
				while(iter.hasNext())
				{
					FileInfo fileInfo=iter.next();
					String filePath=fileInfo.getPath();
					AppLogInfo appLogInfo=getAppsInfo(filePath);
					if(null==appLogInfo)
					{
						logger.error("scann task one app does not exist app log dir scann next time!");
						iter.remove();
						continue;
					}
					fileInfo.setAppLogInfo(appLogInfo);
				}

				
				if (isChanged()||firstRun) {
					logger.info("is it the first time scann task success? "+firstRun);
					logger.info("Scan Task log dir changed scannedFiles"
							+ scannedFiles);
					logger.info("Scan Task log dir changed last scannedFiles"
							+ lastScannedFiles);
					 /**
					  * 上报logger server 成功 更新旧数据
					  */
					List<AppLogInfo> sendData = new ArrayList<AppLogInfo>();

					for (int i = 0; i < updateFiles.size(); i++) {
						sendData.add(updateFiles.get(i).getAppLogInfo());
					}

					boolean loggerServerFlag = notifyLoggerServer(sendData);
					//loggerServerFlag=true;
					boolean devServerFlag = notifyDevServer(sendData);
					if (devServerFlag && loggerServerFlag) {
						firstRun=false;
						updateLastInfo();
					} else {
						logger.info("Scan Task notify log server failed,wait next time");
					}
				}
				else
				{
					logger.info("Scan Task notify log server there is no change");	
				}
			} catch (Exception e) {
				
				logger.info("Scan Task notify log server exception continue again!",e);
			}
		}

		/**
		 * 通知logger 日志目录发生变化，需要进行日志收集
		 */
		private boolean notifyLoggerServer(List<AppLogInfo> sendData) {
			
			
			logger.info("send data="+sendData);
			@SuppressWarnings({ "rawtypes", "unchecked" })
			Map<String, Object> result =null;
			result=new HashMap<String,Object>();

			/**
			 * 通知运维中心 
			 */
			String url=ConfigUtil.getInstance().getlogServerBaseUrl();
			Map<String,Object> value=new HashMap<String,Object>();
			value.put("value", sendData);
			result = restClientUtil.invokePost(
					url, value,
					new TypeReference<Map<String, Object>>() {
					});

			if (null!=result&&"ok".equals((String) result.get("result"))) {
				logger.info("notify logger server success!");
				return true;
			}
			else
			{
				logger.info("notify logger server failed! logger server response="+result);
				// 返回true 表示通知成功
				return false;
			}
			
		}
		/**
		 * 通知logger 日志目录发生变化，需要进行日志收集
		 */
		private boolean notifyDevServer(List<AppLogInfo> sendData) {
			logger.info("send data="+sendData);
			@SuppressWarnings({ "rawtypes", "unchecked" })
			Map<String, Object> result =null;
			result=new HashMap<String,Object>();

			/**
			 * 通知运维中心 
			 */
//			Map<String,Object> sendResult=new HashMap<String,Object>();
//			sendResult.put("value", sendData);
//			sendResult.put("deaIp", ConfigUtil.getInstance().getDeaIp());
			String url=ConfigUtil.getInstance().getDevServerBaseUrl();
			Map<String,Object> value=new HashMap<String,Object>();
			Map<String,Object> deaInfo=new HashMap<String,Object>();
			deaInfo.put("deaIp", ConfigUtil.getInstance().getDeaIp());
			value.put("value", sendData);
			value.put("deaIp", deaInfo);
			result = restClientUtil.invokePost(
					url, value,
					new TypeReference<Map<String, Object>>() {
					});
			if (null!=result) {
				
				String status=String.valueOf((Integer) result.get("status"));
				String content=(String) result.get("content");
				if("200".equals(status)&&"success".equals(content))
				logger.info("notify dev server success!");
				return true;
			}
			else
			{
				logger.info("notify dev server failed! logger server response="+result);
				// 返回true 表示通知成功
				return false;
			}
			
		}
		/**
		 * p判断是否有日志信息更新，如果有更新，刷新内存数据
		 * @return
		 */
		@SuppressWarnings("unchecked")
		private boolean isChanged() {
			
			/**
			 * 本次扫描发现的新增app目录
			 */
			List<FileInfo> addedFiles=new ArrayList<FileInfo>();
			
			/**
			 * 本次扫描 发现的减少的app目录
			 */
			List<FileInfo> deletedFiles=new ArrayList<FileInfo>();
			
			/**
			 * get added app dir
			 */
			for (int i = 0; i < scannedFiles.size(); i++) {
				if (!lastScannedFiles.contains(scannedFiles.get(i))) {
					addedFiles.add(scannedFiles.get(i));
				}
			}

			for (int i = 0; i < lastScannedFiles.size(); i++) {
				if (!scannedFiles.contains(lastScannedFiles.get(i))) {
					deletedFiles.add(lastScannedFiles.get(i));
				}
			}
			
			if(addedFiles.isEmpty()&&deletedFiles.isEmpty())
			{
				// no change
				return false;
			}
			else
			{
				/**
				 * 提取需要上报的数据
				 */
				getUpdateInfo(addedFiles, deletedFiles);
				return true;
			}
			
		}

		/**
		 * 获取 需要上报的数据，但是保存的lastScannedFiles 不进行改变，只
		 *  有当通知成功的时候再更新lastScannedFiles
		 * @param updateFiles
		 * @param addFiles
		 * @param deletedFiles
		 */
		private void getUpdateInfo(List<FileInfo> addFiles,List<FileInfo> deletedFiles)
		{
			/**
			 * store old data
			 */
			for (int i = 0; i < lastScannedFiles.size(); i++) {
				updateFiles.add(lastScannedFiles.get(i));
			}

			/**
			 * 删除那些已经被删除的应用
			 */
			for (int i = 0; i < deletedFiles.size(); i++) {
				updateFiles.remove(deletedFiles.get(i));
			}
			
			/**
			 * 获取新增文件的详细信息
			 */
			for(int i=0;i<addFiles.size();i++)
			{
				// 新增的日志，进行属性修改
				
				AppLogInfo temp = addFiles.get(i).getAppLogInfo();
				List<LogDetail> logs = temp.getLogs();
				for (int j = 0; j < logs.size(); j++) {
					
					FileUtils.chownFile(logs.get(j).getLogDir());
				}
				updateFiles.add(addFiles.get(i));
			}
		}
		
		/**
		 * 获取该应用的详细信息，以及日志信息
		 * @param filePath
		 * @return
		 */
		private AppLogInfo getAppsInfo(String filePath)
		{
			String logFilepath=filePath+Constants.RELEATIVE_APP_ENV_LOG;
			
			String vcapApplicationInfo=FileUtils.paraseVcapEnv(logFilepath);
			
			if(null==vcapApplicationInfo)
			{
				logger.error("getAppLogInfo parase app env log exception filePath="+filePath);
				return null;
			}
			
			JSONObject jsonObject = JSONObject.fromObject(vcapApplicationInfo);
			Map<String,Object> appDetail=(Map<String, Object>) JSONObject.toBean(jsonObject, Map.class);
			
			List<LogDetail> logDetails=getLogsDetails(filePath);
			if(logDetails.isEmpty())
			{
				logger.error("getAppLogInfo app exist ,but the log path doest not exist now  filePath="+filePath);
				return null;
			}
			AppLogInfo appLogInfo=new AppLogInfo();
			appLogInfo.setApplicationName((String)appDetail.get("name"));
			appLogInfo.setApplicationVersion((String)appDetail.get("application_version"));
			appLogInfo.setDeaHostName(ConfigUtil.getInstance().getDeaHostName());
			appLogInfo.setDeaIp(ConfigUtil.getInstance().getDeaIp());
			appLogInfo.setDeaPort(ConfigUtil.getInstance().getDeaPort());
			appLogInfo.setInstanceId((String)appDetail.get("instance_id"));
			appLogInfo.setStartTime(String.valueOf(appDetail.get("started_at")));
			appLogInfo.setInstanceNum(String.valueOf(appDetail.get("instance_index")));
			appLogInfo.setLogs(logDetails);
			
			return appLogInfo;
		}
		
		/**
		 * 设置权限，获取全部日志的文件名称
		 * @param filePath
		 * @return
		 */
		private  List<LogDetail> getLogsDetails(String filePath)
		{
			List<LogDetail> result = new ArrayList<LogDetail>();
			
			String vcapLogPathDir=filePath+Constants.RELEATIVE_APP_STRAT_LOG_DIR;
			/**
			 * 该目录肯定存在，所以先检查该目录，保证CF logger收集的日志能够立即被取到
			 */
			File logFiles2=new File(vcapLogPathDir);
			
			if(!logFiles2.exists())
			{
				logger.error(" Scanner Task log dir"+vcapLogPathDir+"does not exist logFiles2");
				return result;
			}
//			else
//			{
//				FileUtils.chownFile(vcapLogPath);
//			}
			
			String []logFileNames2=logFiles2.list();
			List<String> vcapLogFiles=Arrays.asList(logFileNames2);
			
			for(int i=0;i<vcapLogFiles.size();i++)
			{
				vcapLogFiles.set(i, vcapLogPathDir+File.separator+vcapLogFiles.get(i));
			}
			
			LogDetail logDetail2=new LogDetail();
			logDetail2.setLogDir(vcapLogPathDir);
			logDetail2.setLogFiles(vcapLogFiles);
			logDetail2.setLogType("runtime");
			result.add(logDetail2);
			
			String logDir=filePath+Constants.RELEATIVE_APP_RUNTIME_LOG_DIR;
			
			File logFileTest=new File(logDir);
           
			/**
			 * 如果日志文件还没有产生
			 */
			if(!logFileTest.exists())
           {
        	    logger.error(" Scanner Task log dir does not exist logDir="+logDir);
				return result;
           }

			
			//runtime log
			File logFiles=new File(logDir);
			String []logFileNames=logFiles.list();
			List<String> applogFiles=Arrays.asList(logFileNames);
			for(int i=0;i<applogFiles.size();i++)
			{
				applogFiles.set(i, logDir+File.separator+applogFiles.get(i));	
			}
			
			LogDetail logDetail1=new LogDetail();
			logDetail1.setLogDir(logDir);
			logDetail1.setLogFiles(applogFiles);
			logDetail1.setLogType("runtime");
	
			result.add(logDetail1);
			
			return result;
		}
		
		
		/**
		 * 更新上次的数据n ()只有当通知log server 成功时，才会更新之前的数据
		 */
		private void updateLastInfo() {
			
			lastScannedFiles.clear();
			
			for (int i = 0; i < scannedFiles.size(); i++) {
				lastScannedFiles.add(scannedFiles.get(i));
			}
		}
	}
}
