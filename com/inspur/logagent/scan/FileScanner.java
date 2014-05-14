package com.inspur.logagent.scan;

/**
 * scan the target path,return directory in this path
 * @author wang
 *
 */
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inspur.logagent.common.FileInfo;
   
public class FileScanner {
	
	private Log logger = LogFactory.getLog(FileScanner.class);

    /**
     * 
     *每执行一次，会将上次的数据清空
     */
    public List<FileInfo> scan(String rootPath){
    	
    	File file = new File(rootPath);
    	List<FileInfo> fileList=new ArrayList<FileInfo>();
    	if(!file.exists())
    	{
    		logger.error("FileScanner the path doe not exist  rootPaht="+rootPath);
    		return fileList;
    	}
		String[] filelist = file.list();
		StringBuilder scannedFiles=new StringBuilder();
		for (int i = 0; i < filelist.length; i++) {
			if("tmp".equals(filelist[i]))
			{
				continue;
			}
			File sFile = new File(rootPath + File.separator + filelist[i]);
			scannedFiles.append(filelist[i]).append("; ");
			fillFileList(fileList,sFile);
		}  
		logger.debug("FileScanner scannedFiles = "+scannedFiles);
		return fileList;
    }    
    
    public void fillFileList(List<FileInfo> fileList,File file){    
        FileInfo fileInfo=new FileInfo();    
        fileInfo.setPath(file.getPath());    
        String time=new SimpleDateFormat ("yyyy-MM-dd").format(new Date(file.lastModified()));    
        fileInfo.setLastModifyTime(time);    
        fileList.add(fileInfo);    
    }          
}  