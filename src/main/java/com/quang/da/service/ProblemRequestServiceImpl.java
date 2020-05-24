package com.quang.da.service;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.quang.da.entity.Customer;
import com.quang.da.entity.Expert;
import com.quang.da.entity.ProblemRequest;
import com.quang.da.entity.ProblemRequestImage;
import com.quang.da.entity.Status;
import com.quang.da.enumaration.StatusEnum;
import com.quang.da.repository.ExpertRepository;
import com.quang.da.repository.ProblemRequestRepository;
import com.quang.da.repository.StatusRepository;
import com.quang.da.security.CustomUser;



@Service
public class ProblemRequestServiceImpl implements ProblemRequestService {

	
	@Autowired
	private ProblemRequestRepository rep;
	
	@Autowired
	private ExpertRepository expRep;
	
	@Autowired
	private StorageService storageSer;
	
	@Autowired
	private StatusRepository statusRep;
	
	private CustomUser getUserContext() {
		return (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	@Transactional(rollbackOn = Exception.class)
	@Override
	public void createRequest(MultipartFile[] files,ProblemRequest entity) throws IOException {
		//Set status

		Status status = statusRep.findOneByStatus(StatusEnum.NEW);
		
		Customer customer = new Customer();
		customer.setId(getUserContext().getId());
		entity.setCustomer(customer);
		entity.setCreatedDate(new Date(Calendar.getInstance().getTimeInMillis()));

		ArrayList<ProblemRequestImage> imgEntity = new ArrayList<ProblemRequestImage>();
		for (int i = 0 ; i < files.length; i ++) {
			ProblemRequestImage tmp = new ProblemRequestImage();
			tmp.setImageName(UUID.randomUUID().toString());
			tmp.setRequestId(entity);
			imgEntity.add(tmp);
			storageSer.saveFileFromMultipartFile(files[i], tmp.getImageName());
		}
		entity.setImages(imgEntity);
		entity.setStatus(status);
		rep.save(entity);

	}
	
	@Transactional(rollbackOn = Exception.class)
	@Override
	public void modifyRequest(MultipartFile[] files,ProblemRequest entity, String[] delImgName) throws IOException {
		//Check status
		Optional<ProblemRequest> opEntity = rep.findById(entity.getRequestId());
		if(opEntity.isPresent()) {
			ProblemRequest saveEntity = opEntity.get();
			if(saveEntity.getStatus().getStatus().equals(StatusEnum.NEW)) {
				saveEntity.setTitle(entity.getTitle());
				saveEntity.setDescription(entity.getDescription());
				saveEntity.setDeadlineDate(entity.getDeadlineDate());
				ArrayList<ProblemRequestImage> imgEntity = (ArrayList<ProblemRequestImage>) saveEntity.getImages();
				for (int i = 0 ; i < files.length; i ++) {
					ProblemRequestImage tmp = new ProblemRequestImage();
					tmp.setImageName(UUID.randomUUID().toString());
					tmp.setRequestId(entity);
					imgEntity.add(tmp);
					storageSer.saveFileFromMultipartFile(files[i], tmp.getImageName());
				}
				for(String imgName: delImgName) {
					storageSer.deleteImageByFileName(imgName);
					imgEntity.remove(new ProblemRequestImage(imgName, null));
				}
				entity.setImages(imgEntity);
				rep.save(saveEntity);
			}
		}
	}
	
	@Override
	public List<ProblemRequest> getCurrentUserRequest() {
		if(getUserContext().isExpert()) 
			return rep.findByExpertId(getUserContext().getId());
		else 
			return rep.findByCustomerId(getUserContext().getId());
	}
	
	@Override
	public ProblemRequest getProblemRequestDetail(int id) {
		ProblemRequest result = null;
		Optional<ProblemRequest> entity = rep.findById(id);
		if(entity.isPresent()) result = entity.get();
		return result;
	}
	
	@Override
	public void cancelRequest(int id) {
		//Check status
	}
	
	@Override
	public void acceptExpert(int expertId, int requestId) {
		//Check status
		//Change status
		Optional<ProblemRequest> request = rep.findOneByIdAndCustomerId(requestId, getUserContext().getId());
		if(request.isPresent()) {
			Optional<Expert> expert = expRep.findById(expertId);
			if(expert.isPresent()) {
				ProblemRequest entity = request.get();
				Expert expertEntity = expert.get();
				entity.setExpert(expertEntity);
				rep.save(entity);
			}
		}
	}
	
	@Override
	public void completeRequest(int requestId, String feedBack, float rating) {
		Optional<ProblemRequest> request = rep.findById(requestId);
		if(request.isPresent()) {
			ProblemRequest entity = request.get();
			entity.setFeedBack(feedBack);
			entity.setRating(rating);
			//Change status
			rep.save(entity);
		}
	}
	
	
}
