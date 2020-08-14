package com.quang.da.service;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class StorageServiceImpl implements StorageService {

	@Value("${server.storage}")
	private String filePath;
	
	@Override
	public String saveFileFromMultipartFile(MultipartFile file) throws IOException {
		String realFileName = UUID.randomUUID().toString() +"." + getImageExtension(file.getContentType()); 
		Path path = Paths.get(filePath + realFileName);
        Files.createFile(path);
        Files.write(path, file.getBytes());
        return realFileName;
        
	}
	
	@Override
	public void deleteImageByFileName(String fileName) throws IOException {
		Path path = Paths.get(filePath + fileName);
		Files.delete(path);
	}
	
	private String getImageExtension(String contentType) {
		return contentType.replace("image/", "");
	}
	
	@Override
	public byte[] getImageByte(String name) throws IOException {
		if(name == "null") return null;
		File a = new File(filePath + name);
		InputStream in;
		in = new FileInputStream(a);
		System.out.println(a.getName());
		return IOUtils.toByteArray(in);
	
	}
	
}
