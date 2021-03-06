package com.quang.da.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.quang.da.customResult.ExpertStat;
import com.quang.da.dto.ExpertStatDTO;
import com.quang.da.entity.Customer;
import com.quang.da.entity.Expert;
import com.quang.da.entity.ProblemRequest;
import com.quang.da.entity.ProblemRequestImage;
import com.quang.da.entity.RequestApplication;
import com.quang.da.entity.Status;
import com.quang.da.enumaration.StatusEnum;
import com.quang.da.repository.ExpertRepository;
import com.quang.da.repository.ProblemRequestImageRepository;
import com.quang.da.repository.ProblemRequestRepository;
import com.quang.da.repository.RequestApplicationRepository;
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
	
	@Autowired
	private RequestApplicationRepository appRep;
	
	@Autowired
	private ProblemRequestImageRepository imgRep;
	
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
			String imgName = storageSer.saveFileFromMultipartFile(files[i]);
			tmp.setImageName(imgName);
			tmp.setRequestId(entity);
			imgEntity.add(tmp);
			
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
				List<ProblemRequestImage> imgEntity = saveEntity.getImages();
				for (int i = 0 ; i < files.length; i ++) {
					ProblemRequestImage tmp = new ProblemRequestImage();
					String imgName = storageSer.saveFileFromMultipartFile(files[i]);
					tmp.setImageName(imgName);
					tmp.setRequestId(entity);
					imgEntity.add(tmp);
					/*
					 * ProblemRequestImage tmp = new ProblemRequestImage();
					 * tmp.setImageName(UUID.randomUUID().toString()); tmp.setRequestId(entity);
					 * imgEntity.add(tmp); storageSer.saveFileFromMultipartFile(files[i],
					 * tmp.getImageName());
					 */
				}
				for(String imgName: delImgName) {
					imgRep.deleteById(imgName);
					storageSer.deleteImageByFileName(imgName);
					
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
	
	private final static int ITEMCOUNT = 10;
	
	@Override
	public List<ProblemRequest> getCurrentUserRequestByStatus(StatusEnum[] status, int page) {
		Pageable pageable = PageRequest.of(page, ITEMCOUNT);
		if(getUserContext().isExpert()) 
			return rep.findByExpertIdAndStatus(getUserContext().getId(), status, pageable);
		else 
			return rep.findByCustomerIdAndStatus(getUserContext().getId(), status, pageable);
	}
	
	@Override
	public List<ProblemRequest> getCurrentUserAppliedRequest() {
		return  appRep.findAppliedRequests(getUserContext().getId());

	}
	

	
	@Override
	public List<ProblemRequest> expertSearch(int id, String city, String language, int time) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		Date endDate = new Date(cal.getTimeInMillis());
		cal.add(Calendar.DATE, -1);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.add(Calendar.DATE, - 7 * time);
		Date startDate = new Date(cal.getTimeInMillis());
		List<Integer> appliedRequest = appRep.findAppliedRequestIds(getUserContext().getId());
		appliedRequest.add(0);
		
		if(city == null && language == null) {
			return rep.findByMajorStartDateEndDate(id, appliedRequest, startDate, endDate);
		}else if(city != null && language == null) {
			return rep.findByMajorCityStartDateEndDate(id, city, startDate, endDate);
		}else if(city == null && language != null) {
			return rep.findByMajorLanguageStartDateEndDate(id, language, startDate, endDate);
		}else {
			return rep.findByMajorCityLanguageStartDateEndDate(id, city, language, startDate, endDate);
		}
		
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
	
	@Transactional(rollbackOn = Exception.class)
	@Override
	public void acceptExpert(int requestId,int expertId) {
		//Check status
		//Change status
		Optional<ProblemRequest> request = rep.findOneByIdAndCustomerId(requestId, getUserContext().getId());
		if(request.isPresent()) {
			Optional<Expert> expert = expRep.findById(expertId);
			if(expert.isPresent()) {
				ProblemRequest entity = request.get();
				Expert expertEntity = expert.get();
				entity.setExpert(expertEntity);
				Status status = statusRep.findOneByStatus(StatusEnum.ACCEPTED);
				entity.setStatus(status);
				rep.save(entity);
				appRep.deleteApplicationByRequestId(requestId);
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
	
	@Override
	public boolean expertApply(int requestId) {
		//Check request con hop le hay khong
		boolean result = false;
		Optional<ProblemRequest> problemRequest = rep.findById(requestId);
		
		if(problemRequest.isPresent()) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.add(Calendar.MINUTE, -2);
			if(problemRequest.get().getDeadlineDate().before(calendar.getTime())) return result;
			result = true;
			RequestApplication entity = new RequestApplication();
			Expert expert = new Expert();
			expert.setId(getUserContext().getId());
			entity.setProblemRequest(problemRequest.get());
			entity.setExpert(expert);
			entity.setCreatedDate(new Date(Calendar.getInstance().getTimeInMillis()));
			appRep.save(entity);
		}
		
		return result;
	}
	
	@Override
	public List<Expert> getApplicantList(int requestId) {
		return appRep.findAllExpertByRequestId(requestId);
	}
	
	@Override
	public Customer getCustomerProfileInRequestId(int requestId) {
		return rep.findCustomerInRequest(requestId).get();
	}
	
	@Override
	public Expert getExpertProfileInRequestId(int requestId) {
		return rep.findExpertInRequest(requestId).get();
	}
	
	@Override
	public List<Number> getSubableRequest() {
		if(getUserContext().isExpert()) {
			return rep.expertFindSubableRequest(getUserContext().getId());
		}else {
			return rep.customerFindSubableRequest(getUserContext().getId());
		}
	}
	
	@Async
	@Override
	public void deactiveExpireRequest() {
		Calendar calendar =Calendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.MINUTE, -1);
		List<ProblemRequest> list = rep.findExpireRequest(new Date(calendar.getTimeInMillis()));
		Status status = statusRep.findOneByStatus(StatusEnum.DEACTIVE);
		for (ProblemRequest problemRequest : list) {
			problemRequest.setStatus(status);
		}
		rep.saveAll(list);
	}
	
	@Override
	public ExpertStatDTO getExpertStat(int id) {
		ExpertStatDTO result = new ExpertStatDTO();
		StatusEnum[] status = {StatusEnum.COMPLETE,StatusEnum.CANCEL};
		List<ExpertStat> stat = rep.getExpertStat(id, status);
		for (ExpertStat expertStat : stat) {
			if(expertStat.getStatusEnum() == StatusEnum.COMPLETE) {
				result.setCompleteCount((int)expertStat.getCount());
				result.setRating((float)expertStat.getRating());
			}else if(expertStat.getStatusEnum() == StatusEnum.CANCEL) {
				result.setCancelCount((int)expertStat.getCount());
			}
		}
		return result;
	}
	
	@Override
	public byte[] getImage(String imgName)  {
		if(imgName != "null")
			try {
				return storageSer.getImageByte(imgName);
			} catch (IOException e) {
				if(e instanceof FileNotFoundException) {
					
				}
			}
		
		return null;
	}
	
	
}
