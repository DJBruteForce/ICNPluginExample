package com.ibm.gil.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
	
	
	public static boolean inputStreamToFile(InputStream inputStream, String filePath, String fileName) {
		
		OutputStream outputStream = null;

		try {
	
			outputStream = new FileOutputStream(new File(filePath + System.getProperty("file.separator") + fileName));

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
			
			return true;
			
			
		

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					// outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	    
	
	}
	
	public static boolean deleteFile(File temp){
		return temp.delete();
	}
	
	public static String getFilePath(File temp){
		
	    String absolutePath = temp.getAbsolutePath();

	    String filePath = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));

	    return filePath;
	}
	
	

}
