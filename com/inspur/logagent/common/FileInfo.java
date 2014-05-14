package com.inspur.logagent.common;

/**
 * warden 的 根目录信息
 * @author wang
 *
 */
@SuppressWarnings("rawtypes")
public class FileInfo  implements Comparable{    
    private String path;   
    private String lastModifyTime;
    
    private AppLogInfo appLogInfo;
    
    public String getPath() {    
        return path;    
    }    
    public void setPath(String path) {    
        this.path = path;    
    }    
    public String getLastModifyTime() {    
        return lastModifyTime;    
    }    
    public void setLastModifyTime(String lastModifyTime) {    
        this.lastModifyTime = lastModifyTime;    
    }


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((appLogInfo == null) ? 0 : appLogInfo.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
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
		FileInfo other = (FileInfo) obj;
		if (appLogInfo == null) {
			if (other.appLogInfo != null)
				return false;
		} else if (!appLogInfo.equals(other.appLogInfo))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FileInfo [path=");
		builder.append(path);
		builder.append(", lastModifyTime=");
		builder.append(lastModifyTime);
		builder.append(", appLogInfo=");
		builder.append(appLogInfo);
		builder.append("]");
		return builder.toString();
	}
	@Override
	public int compareTo(Object o) {
		if (o instanceof FileInfo) {
			throw new IllegalArgumentException();
		}
		FileInfo fileInfo = (FileInfo) o;
		if (null == fileInfo.getPath()) {
			throw new IllegalArgumentException();
		}

		return fileInfo.getPath().compareTo(this.path);

	}
	public AppLogInfo getAppLogInfo() {
		return appLogInfo;
	}
	public void setAppLogInfo(AppLogInfo appLogInfo) {
		this.appLogInfo = appLogInfo;
	}
    
}  
