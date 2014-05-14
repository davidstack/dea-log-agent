package com.inspur.logagent.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUtils {

	private static Log logger = LogFactory.getLog(FileUtils.class);

	/**
	 * 从env.log中解析出VCAP_APPLICATION 对应的信息
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public static String paraseVcapEnv(String filePath){

		logger.debug("FileUtils parase envlog path=" + filePath);

		InputStream is = null;
		BufferedReader reader = null;
		try {
			is = new FileInputStream(filePath);
			String line; 
			reader = new BufferedReader(new InputStreamReader(is));

			line = reader.readLine();
			while (line != null) {
				String[] strArrays = line.split("=");
				if (strArrays.length != 2) {
					logger.error("FileUtils parase envlog undefine log content");
					return null;
				}
				if ("VCAP_APPLICATION".equals(strArrays[0])) {
					return strArrays[1];
				} 
				else {
					line = reader.readLine();
					continue;
				}
			}
		} catch (Exception e) {
			logger.error("FileUtils parase envlog exception",e);
			return null;
		} finally {
			try {
				if(null!=reader)
				{
					reader.close();
				}
				if(null!=is)
				{
					is.close();
				}
			} catch (Exception e) {
				logger.error("FileUtils parase envlog close fail",e);
			}
			
		}
		logger.error("FileUtils parase envlog does not find VCAP_APPLICATION filepath="
				+ filePath);
		return null;
	}
	
	public static void chownFile(String filePath)
	{
		if (null == filePath) {
			logger.error("Fileutils chownFile fail  filePath is null;");
			return;
		}
		
		File file = new File(filePath);
		if (!file.exists()) {
			logger.error("Fileutils chownFile fail  filePath does not exist;");
			return;
		}

		String cmd="chmod o+r "+filePath+" -R";
		logger.info("chown file cmd="+cmd);
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (Exception e) {
			logger.error("Fileutils chownFile fail exec commond fail",e);
		}
	}
	
	/**
	 * 通过调用shell 脚本执行 文件属性的修改
	 * @param filePath
	 */
	public static void chownFileWithSh(String filePath)
	{
		//TODO
//		if (null == filePath) {
//			logger.error("Fileutils chownFile fail  filePath is null;");
//			return;
//		}
//		
//		File file = new File(filePath);
//		if (!file.exists()) {
//			logger.error("Fileutils chownFile fail  filePath does not exist;");
//			return;
//		}
//
//		String cmd=Constants.CF_CHOWN_SHELL;
//		
//		try {
//			Process ps=Runtime.getRuntime().exec(cmd);
//		} catch (Exception e) {
//			logger.error("Fileutils chownFile fail exec commond fail",e);
//		}
	}
}


