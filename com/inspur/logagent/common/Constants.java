package com.inspur.logagent.common;

public class Constants {

	/**
    public static String scannRootPath="F:\\warden";
	
	public static String RELEATIVE_APP_RUNTIME_LOG_DIR="\\run\\logs";
	
	public static String RELEATIVE_APP_ENV_LOG="\\run\\logs\\env.log";
	
	public static String  RELEATIVE_APP_STRAT_LOG_DIR="\\run\\logs";
	
    public  static String CF_CONSOLE_CONFIG_FILE="E:\\workspace\\config\\config.properties";
	 
	public  static String CF_LOG4J_CONFIG_FILE="E:\\workspace\\config\\log4j.properties";
	*/
	
	public static void main(String[] args) {
		String str="E:\\workspace\\config\\log4j.properties";
		System.out.println(str.substring(str.lastIndexOf("\\")+1));
	}
	
	
	// env linux
	
	
	public static String scannRootPath="/var/vcap/data/warden/depot";
	
	public static String RELEATIVE_APP_RUNTIME_LOG_DIR="/tmp/rootfs/tmp/applogs";
	
	public static String RELEATIVE_APP_ENV_LOG="/tmp/rootfs/home/vcap/logs/env.log";
	
	public static String  RELEATIVE_APP_STRAT_LOG_DIR="/tmp/rootfs/home/vcap/logs";
	
	public  static String CF_CONSOLE_CONFIG_FILE="/opt/inspur/logagent/agent/config/config.properties";
	
	public  static String CF_LOG4J_CONFIG_FILE="/opt/inspur/logagent/agent/config/log4j.properties";
	
	
	
	//public  static String CF_CHOWN_SHELL=System.getProperty("user.dir")+"/../sh/chownfile.sh";
	
	
	public static String DEA_IP="deaIp";
	
	public static String DEA_PORT="deaPort";
	
	public static String DEA_HOSTNAME="deaHostName";
	public static String LOG_SERVER_BASE_URL="logserverurl";
	
	public static String DEV_SERVER_BASE_URL="devserverurl";
	

}
