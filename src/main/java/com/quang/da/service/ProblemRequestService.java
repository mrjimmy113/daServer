package com.quang.da.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.quang.da.dto.ExpertStatDTO;
import com.quang.da.entity.Customer;
import com.quang.da.entity.Expert;
import com.quang.da.entity.ProblemRequest;
import com.quang.da.enumaration.StatusEnum;

public interface ProblemRequestService {

	void cancelRequest(int id);


	void completeRequest(int requestId, String feedBack, float rating);

	void createRequest(MultipartFile[] files, ProblemRequest entity) throws IOException;

	void modifyRequest(MultipartFile[] files, ProblemRequest entity, String[] delImgName) throws IOException;

	List<ProblemRequest> getCurrentUserRequest();

	ProblemRequest getProblemRequestDetail(int id);

	List<ProblemRequest> expertSearch(int major, String city, String language, int time);

	boolean expertApply(int requestId);

	List<Expert> getApplicantList(int requestId);


	void acceptExpert(int requestId, int expertId);


	List<ProblemRequest> getCurrentUserAppliedRequest();


	Expert getExpertProfileInRequestId(int requestId);


	Customer getCustomerProfileInRequestId(int requestId);


	List<Number> getSubableRequest();


	List<ProblemRequest> getCurrentUserRequestByStatus(StatusEnum[] status, int page);


	void deactiveExpireRequest();


	ExpertStatDTO getExpertStat(int id);

}
