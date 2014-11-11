package com.example.andriod;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.util.Log;

/**
 * This utility extracts files and directories of a standard zip file to
 * a destination directory.
 * @author www.codejava.net
 *
 */
public class UnzipUtility {
	
	public interface UnzipListener{
		public void onExtractStart( );
		public void onExtractProgress(int progress, String filePath);
		public void onExtractCompleted();
		
	}
	/**
	 * Size of the buffer to read/write data
	 */
	private static final int BUFFER_SIZE = 4096;

	/**
	 * Extracts a zip file specified by the zipFilePath to a directory specified by
	 * destDirectory (will be created if does not exists)
	 * @param zipFilePath
	 * @param destDirectory
	 * @param unzipListener 
	 * @throws IOException
	 */
	public void unzip(String zipFilePath, String destDirectory, UnzipListener unzipListener) throws IOException {
		File destDir = new File(destDirectory);
		if (!destDir.exists()) {
			destDir.mkdir();
		}

		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));

		ZipEntry entry = zipIn.getNextEntry();
		unzipListener.onExtractStart();
		// iterates over entries in the zip file
		while (entry != null) {
			String filePath = destDirectory + File.separator + entry.getName();
			if(filePath.contains(".DS_Store")){
				
			}else{
			if (!entry.isDirectory()) {
				// if the entry is a file, extracts it
				extractFile(zipIn, filePath,entry,unzipListener);
			} else {
				// if the entry is a directory, make the directory
				File dir = new File(filePath);
				dir.mkdir();
			}
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
		unzipListener.onExtractCompleted();
	}

	/**
	 * Extracts a zip entry (file entry)
	 * @param zipIn
	 * @param filePath
	 * @param entry 
	 * @param unzipListener 
	 * @throws IOException
	 */
	private synchronized void extractFile(ZipInputStream zipIn, String filePath, ZipEntry entry, UnzipListener unzipListener) throws IOException {
		Log.e("extractFile",filePath+"");
		
	
		
		String tempx[]=filePath.split("/");
		StringBuffer buffer=new StringBuffer();
		for(int i=0;i<tempx.length-1;i++){
			buffer.append("/"+tempx[i]);
		}
		final File file = new File(buffer.toString());
		file.mkdirs();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[BUFFER_SIZE];
		int read = 0;
		long total=0;
		long entry_size=entry.getSize();
		if (entry_size < 0) {
			entry_size = 0xffffffffl + entry_size ;
		}
		unzipListener.onExtractProgress(0,filePath);
		while ((read = zipIn.read(bytesIn)) != -1) {
			total+=read;
			bos.write(bytesIn, 0, read);
			Log.e("extractFile",total*100+"<>"+entry_size);
			
			unzipListener.onExtractProgress((int) ((total*100)/entry_size),filePath);
		}
		bos.close();
		
	}
}
