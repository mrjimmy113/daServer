package com.quang.da.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.quang.da.entity.ProblemRequest;

public interface ProblemRequestService {

	void cancelRequest(int id);

	void acceptExpert(int expertId, int requestId);

	void completeRequest(int requestId, String feedBack, float rating);

	void createRequest(MultipartFile[] files, ProblemRequest entity) throws IOException;

	void modifyRequest(MultipartFile[] files, ProblemRequest entity, String[] delImgName) throws IOException;

	List<ProblemRequest> getCurrentUserRequest();

	ProblemRequest getProblemRequestDetail(int id);

}
