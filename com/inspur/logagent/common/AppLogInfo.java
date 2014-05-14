package com.inspur.logagent.common;

import java.io.Serializable;
import java.util.List;

/**
 * 应用的日志信息
 * @author wang
 *
 */

public class AppLogInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5710650969110458889L;
	
	private String applicationVersion;
	private String applicationName;
	private String instanceId;
	private String startTime;
	private String deaIp;
	private String deaPort;
	private String deaHostName;
	private String logGroup="App Log";
	private String instanceNum;
	private List<LogDetail> logs;
	public String getApplicationVersion() {
		return applicationVersion;
	}
	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getDeaIp() {
		return deaIp;
	}
	public void setDeaIp(String deaIp) {
		this.deaIp = deaIp;
	}
	public String getDeaPort() {
		return deaPort;
	}
	public void setDeaPort(String deaPort) {
		this.deaPort = deaPort;
	}
	public String getDeaHostName() {
		return deaHostName;
	}
	public void setDeaHostName(String deaHostName) {
		this.deaHostName = deaHostName;
	}

	public List<LogDetail> getLogs() {
		return logs;
	}
	public void setLogs(List<LogDetail> logs) {
		this.logs = logs;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AppLogInfo [applicationVersion=");
		builder.append(applicationVersion);
		builder.append(", applicationName=");
		builder.append(applicationName);
		builder.append(", instanceId=");
		builder.append(instanceId);
		builder.append(", startTime=");
		builder.append(startTime);
		builder.append(", deaIp=");
		builder.append(deaIp);
		builder.append(", deaPort=");
		builder.append(deaPort);
		builder.append(", deaHostName=");
		builder.append(deaHostName);
		builder.append(", logs=");
		builder.append(logs);
		builder.append("]");
		return builder.toString();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((applicationName == null) ? 0 : applicationName.hashCode());
		result = prime
				* result
				+ ((applicationVersion == null) ? 0 : applicationVersion
						.hashCode());
		result = prime * result
				+ ((instanceId == null) ? 0 : instanceId.hashCode());
		result = prime * result + ((logs == null) ? 0 : logs.hashCode());
		result = prime * result
				+ ((startTime == null) ? 0 : startTime.hashCode());
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
		AppLogInfo other = (AppLogInfo) obj;
		if (applicationName == null) {
			if (other.applicationName != null)
				return false;
		} else if (!applicationName.equals(other.applicationName))
			return false;
		if (applicationVersion == null) {
			if (other.applicationVersion != null)
				return false;
		} else if (!applicationVersion.equals(other.applicationVersion))
			return false;
		if (instanceId == null) {
			if (other.instanceId != null)
				return false;
		} else if (!instanceId.equals(other.instanceId))
			return false;
		if (logs == null) {
			if (other.logs != null)
				return false;
		} else if (!logs.equals(other.logs))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		return true;
	}
	public String getLogGroup() {
		return logGroup;
	}
	public void setLogGroup(String logGroup) {
		this.logGroup = logGroup;
	}
	public String getInstanceNum() {
		return instanceNum;
	}
	public void setInstanceNum(String instanceNum) {
		this.instanceNum = instanceNum;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

}
