package com.quang.da.service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class StorageServiceImpl implements StorageService {

	
	
	@Override
	public void saveFileFromMultipartFile(MultipartFile file, String fileName) throws IOException {
		//String uniqueID = UUID.randomUUID().toString();
		Path path = Paths.get("D:/" + fileName +"." + getImageExtension(file.getContentType()));
        Files.createFile(path);
        Files.write(path, file.getBytes());
        
	}
	
	@Override
	public void deleteImageByFileName(String fileName) throws IOException {
		Path path = Paths.get("D:/" + fileName + ".jpeg");
		Files.delete(path);
	}
	
	private String getImageExtension(String contentType) {
		return contentType.replace("image/", "");
	}
	
}
