package org.jimple.planner.storage;

import static org.jimple.planner.Constants.EMPTY_STRING;

import java.io.File;
import java.io.IOException;

public interface StorageCreateFile {
	default File createFile(String fileName) {
		File file = new File(fileName);
		
		assert !file.isDirectory();
		
		String dirPath = file.getAbsolutePath().replaceAll(file.getName(), EMPTY_STRING);
		File dir = new File(dirPath);
		dir.mkdirs();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}
}
