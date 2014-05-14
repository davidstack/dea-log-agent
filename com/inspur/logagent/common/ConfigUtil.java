package com.inspur.logagent.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;




public class ConfigUtil {
	
	private  static  final  Log  logger=LogFactory.getLog(ConfigUtil.class) ;   
	
	private  static  Properties  property=new Properties();
	
	private static ConfigUtil instance=new ConfigUtil();
	
	private ConfigUtil()
	{
		loadProperties();
	}
	
	public static ConfigUtil getInstance()
	{
		return instance;
	}
	private  void  loadProperties(){
		String filePath = Constants.CF_CONSOLE_CONFIG_FILE;

		File isFile = new File(filePath);

		if (isFile.exists()) {
			try {
				InputStream is = new BufferedInputStream(new FileInputStream(
						new File(filePath)));
				property.load(is);
			} catch (IOException e) {
				logger.error("load file " + filePath
						+ " error." + e.getMessage(), e);
				throw new RuntimeException("load file "
						+ filePath + " error."
						+ e.getMessage(), e);
			}
		} else {
			logger.error("file " + filePath
					+ " not existed.");
			throw new RuntimeException("file "
					+ filePath + " not existed.");
		}

	}
	
	public  String  getlogServerBaseUrl(){
		
		return property.getProperty(Constants.LOG_SERVER_BASE_URL);
	}
	
	public  String  getDeaIp(){
		
		return property.getProperty(Constants.DEA_IP);
	}
	public  String  getDeaPort(){
		
		return property.getProperty(Constants.DEA_PORT);
	}
	public  String  getDeaHostName(){
		
		return property.getProperty(Constants.DEA_HOSTNAME);
	}
	
public  String  getDevServerBaseUrl(){
		
		return property.getProperty(Constants.DEV_SERVER_BASE_URL);
	}
}
