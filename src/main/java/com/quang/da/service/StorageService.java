package com.quang.da.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	void deleteImageByFileName(String fileName) throws IOException;

	String saveFileFromMultipartFile(MultipartFile file) throws IOException;

	byte[] getImageByte(String name) throws IOException;

}
