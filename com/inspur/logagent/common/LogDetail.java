package com.inspur.logagent.common;

import java.io.Serializable;
import java.util.List;

/**
 * 一组日志信息
 * @author wang
 *
 */
public class LogDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4876549471039882388L;
	private String logDir;
	private List<String> logFiles;
	private String logType;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((logDir == null) ? 0 : logDir.hashCode());
		result = prime * result
				+ ((logFiles == null) ? 0 : logFiles.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LogDetail other = (LogDetail) obj;
		if (logDir == null) {
			if (other.logDir != null)
				return false;
		} else if (!logDir.equals(other.logDir))
			return false;
		if (logFiles == null) {
			if (other.logFiles != null)
				return false;
		} else if (!logFiles.equals(other.logFiles))
			return false;
		return true;
	}
	
	public String getLogDir() {
		return logDir;
	}
	public void setLogDir(String logDir) {
		this.logDir = logDir;
	}
	public List<String> getLogFiles() {
		return logFiles;
	}
	public void setLogFiles(List<String> logFiles) {
		this.logFiles = logFiles;
	}
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("LogDetail [logDir=");
		builder.append(logDir);
		builder.append(", logFiles=");
		builder.append(logFiles);
		builder.append(", logType=");
		builder.append(logType);
		builder.append("]");
		return builder.toString();
	}
	
}
