package com.quang.da.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {



	void saveFileFromMultipartFile(MultipartFile file, String fileName) throws IOException;

	void deleteImageByFileName(String fileName) throws IOException;

}
