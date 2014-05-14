package com.inspur.logagent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import com.inspur.logagent.common.Constants;
import com.inspur.logagent.scan.Scanner;

public class MainImpl {

	private static Log logger = LogFactory.getLog(MainImpl.class);

	public static void main(String[] args) {

		//System.out.println(System.getProperty("user.dir"));
		PropertyConfigurator.configure(Constants.CF_LOG4J_CONFIG_FILE);  
		logger.info("app log agent start up:");
	
		/**
		 * 设置进程名称
		 */
		Thread.currentThread().setName("dea_applog_agent");
		Scanner scanner = new Scanner();
		scanner.beginScanTask();
		logger.info("app log agent start success!");
	}
}
