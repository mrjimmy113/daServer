package com.quang.da.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quang.da.dto.CustomerProfileDTO;
import com.quang.da.dto.ExpertProfileDTO;
import com.quang.da.dto.MajorDTO;
import com.quang.da.dto.ProblemRequestDTO;
import com.quang.da.dto.ProblemRequestDetailDTO;
import com.quang.da.entity.Customer;
import com.quang.da.entity.Expert;
import com.quang.da.entity.Major;
import com.quang.da.entity.ProblemRequest;
import com.quang.da.entity.ProblemRequestImage;
import com.quang.da.enumaration.StatusEnum;
import com.quang.da.service.ProblemRequestService;
import com.quang.da.service.StorageService;

@RestController
@RequestMapping("/request")
public class ProblemRequestController {
	
	@Autowired
	ProblemRequestService service;
	
	@Autowired
	StorageService storageSer;
	
	@Secured({"ROLE_CUSTOMER"})
	@PostMapping
	public ResponseEntity<Number> createRequest(@RequestParam("files") MultipartFile[] files,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date endDate,
			@RequestParam("title") String title, @RequestParam("description") String description,
			@RequestParam("majorId") int id
			) {
		HttpStatus status = null;
		try {
			ProblemRequest entity = new ProblemRequest();
			entity.setTitle(title);
			entity.setDescription(description);
			entity.setDeadlineDate(new Date(endDate.getTime()));
			Major major = new Major();
			major.setId(id);
			entity.setMajor(major);
			service.createRequest(files,entity);
			status = HttpStatus.CREATED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<Number>(status.value(),status);
	}
	
	@Secured({"ROLE_CUSTOMER"})
	@PutMapping
	public ResponseEntity<Number> updateRequest(@RequestParam("files") MultipartFile[] files,
			@RequestParam("requestId") Integer id,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date endDate,
			@RequestParam("title") String title, @RequestParam("description") String description,
			@RequestParam("majorId") Integer majorParam,
			@RequestParam("delImgs") String delImgs) {
		HttpStatus status = null;
		try {
			Gson gson = new GsonBuilder().create();
			
			ProblemRequest entity = new ProblemRequest();
			entity.setRequestId(id);
			entity.setTitle(title);
			entity.setDescription(description);
			entity.setDeadlineDate(new Date(endDate.getTime()));
			Major major = new Major();
			major.setId(majorParam);
			entity.setMajor(major);
			service.modifyRequest(files,entity,gson.fromJson(delImgs, String[].class));
			status = HttpStatus.OK;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<Number>(status.value(),status);
	}
	
	
	@Secured({"ROLE_CUSTOMER"})
	@PatchMapping("/accept")
	public ResponseEntity<Number> acceptExpert(@RequestParam int requestId, @RequestParam int expertId) {
		HttpStatus status = null;
		try {
			service.acceptExpert(expertId, requestId);
			status = HttpStatus.OK;
		} catch (Exception e) {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<Number>(status.value(),status);
	}
	
	
	@PatchMapping("/complete")
	public ResponseEntity<Number> completeRequest(@RequestParam int requestId, @RequestParam String feedBack, @RequestParam float rating) {
		HttpStatus status = null;
		try {
			service.completeRequest(requestId, feedBack, rating);
			status = HttpStatus.OK;
		} catch (Exception e) {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<Number>(status.value(),status);
	}
	
	@Secured({"ROLE_EXPERT","ROLE_CUSTOMER"})
	@GetMapping("/detail")
	public ResponseEntity<ProblemRequestDetailDTO> getProblemDetail (@RequestParam("id") int id) {
		HttpStatus status = null;
		ProblemRequestDetailDTO result = null;
		try {
			ProblemRequest entity = service.getProblemRequestDetail(id);
			result = new ProblemRequestDetailDTO();
			BeanUtils.copyProperties(entity, result,"images");
			for (ProblemRequestImage s : entity.getImages()) {
				result.getImages().add(s.getImageName());
			}
			MajorDTO majorDTO = new MajorDTO();
			BeanUtils.copyProperties(entity.getMajor(), majorDTO);
			result.setMajor(majorDTO);
			result.setStatus(entity.getStatus().getStatus());
			status = HttpStatus.OK;
		} catch (Exception e) {
			status = HttpStatus.BAD_REQUEST;
		}
		
		return new ResponseEntity<ProblemRequestDetailDTO>(result,status);
	}
	
	@Secured({"ROLE_EXPERT","ROLE_CUSTOMER"})
	@GetMapping
	public ResponseEntity<List<ProblemRequestDTO>> getCurrentUserProblem() {
		HttpStatus status = null;
		List<ProblemRequestDTO> result = new ArrayList<ProblemRequestDTO>();

		try {
			List<ProblemRequest> entities = service.getCurrentUserRequest();
			for (ProblemRequest e : entities) {
				ProblemRequestDTO dto = new ProblemRequestDTO();
				BeanUtils.copyProperties(e, dto);
				result.add(dto);
			}
			status = HttpStatus.OK;
		} catch (Exception e) {

			status = HttpStatus.BAD_REQUEST;
		}
		
		return new ResponseEntity<List<ProblemRequestDTO>>(result,status);
	}
	
	@Secured({"ROLE_EXPERT","ROLE_CUSTOMER"})
	@GetMapping("/status")
	public ResponseEntity<List<ProblemRequestDTO>> getCurrentUserProblemWithStatus(@RequestParam("status") StatusEnum[] statusEnum) {
		HttpStatus status = null;
		List<ProblemRequestDTO> result = new ArrayList<ProblemRequestDTO>();
		try {

			List<ProblemRequest> entities = service.getCurrentUserRequestByStatus(statusEnum);
			for (ProblemRequest e : entities) {
				ProblemRequestDTO dto = new ProblemRequestDTO();
				BeanUtils.copyProperties(e, dto);
				result.add(dto);
			}
			status = HttpStatus.OK;
		} catch (Exception e) {

			status = HttpStatus.BAD_REQUEST;
		}
		
		return new ResponseEntity<List<ProblemRequestDTO>>(result,status);
	}

	@Secured({"ROLE_EXPERT"})
	@GetMapping("/applied")
	public ResponseEntity<List<ProblemRequestDTO>> getCurrentUserAppliedProblem() {
		HttpStatus status = null;
		List<ProblemRequestDTO> result = new ArrayList<ProblemRequestDTO>();
		try {

			List<ProblemRequest> entities = service.getCurrentUserAppliedRequest();
			for (ProblemRequest e : entities) {
				ProblemRequestDTO dto = new ProblemRequestDTO();
				BeanUtils.copyProperties(e, dto);
				result.add(dto);
			}
			status = HttpStatus.OK;
		} catch (Exception e) {

			status = HttpStatus.BAD_REQUEST;
		}
		
		return new ResponseEntity<List<ProblemRequestDTO>>(result,status);
	}
	
	@GetMapping(
			  value = "/image",
			  produces = MediaType.IMAGE_JPEG_VALUE
			)
	public @ResponseBody byte[] getImageWithMediaType(@RequestParam("imgName") String imgName) {
			 
		if(imgName != null) {
			File a = new File("D:/" + imgName + ".jpeg");
			InputStream in;
			try {
				in = new FileInputStream(a);
				return IOUtils.toByteArray(in);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				
			}
		}
		return null;
		
	}
	
	@Secured({"ROLE_EXPERT"})
	@GetMapping("/search")
	public ResponseEntity<List<ProblemRequestDTO>> expertSearch(@RequestParam int major,
			@RequestParam(required = false) String city, @RequestParam(required = false) String language,
			@RequestParam int time) {
		HttpStatus status = null;
		List<ProblemRequestDTO> result = new ArrayList<ProblemRequestDTO>();
		try {
			List<ProblemRequest> entities = service.expertSearch(major,city,language,time);
			for (ProblemRequest e : entities) {
				ProblemRequestDTO dto = new ProblemRequestDTO();
				BeanUtils.copyProperties(e, dto);
				result.add(dto);
			}
			status = HttpStatus.OK;
		} catch (Exception e) {

			status = HttpStatus.BAD_REQUEST;
		}
		
		return new ResponseEntity<List<ProblemRequestDTO>>(result,status);
	}
	
	@Secured({"ROLE_EXPERT"})
	@PostMapping("/apply")
	public ResponseEntity<Number> expertApply(@RequestParam int requestId) {
		HttpStatus status = null;
		try {
			boolean res = service.expertApply(requestId);
			if(res) {
				status = HttpStatus.OK;
			}else {
				status = HttpStatus.ACCEPTED;
			}
			
		} catch (Exception e) {
			
			status = HttpStatus.BAD_REQUEST;
		}
		
		return new ResponseEntity<Number>(status.value(),status);
	}
	
	@Secured({"ROLE_CUSTOMER"})
	@GetMapping("/applicant")
	public ResponseEntity<List<ExpertProfileDTO>> getApplicantList(@RequestParam int requestId) {
		HttpStatus status = null;
		List<ExpertProfileDTO> result = new ArrayList<ExpertProfileDTO>();
		try {
			List<Expert> entiList = service.getApplicantList(requestId);
			for (Expert expert : entiList) {
				ExpertProfileDTO dto = new ExpertProfileDTO();
				dto.setFullName(expert.getFullName());
				dto.setId(expert.getId());
				MajorDTO majorDto = new MajorDTO();
				majorDto.setMajor(expert.getMajor().getMajor());
				dto.setMajor(majorDto);
				dto.setFeePerHour(expert.getFeePerHour());
				result.add(dto);
			}
			
			status = HttpStatus.OK;
		} catch (Exception e) {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<List<ExpertProfileDTO>>(result,status);
	}
	
	@Secured({"ROLE_CUSTOMER"})
	@PutMapping("/accept")
	public ResponseEntity<Number> accepExpert(@RequestParam int requestId, @RequestParam int expertId) {
		HttpStatus status = null;
		try {
			service.acceptExpert(requestId, expertId);
			status = HttpStatus.OK;
			
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.BAD_REQUEST;
		}
		
		return new ResponseEntity<Number>(status.value(),status);
	}
	
	@Secured({"ROLE_EXPERT","ROLE_CUSTOMER"})
	@GetMapping(value = "/cus")
	public ResponseEntity<CustomerProfileDTO> profileCustomerRequest(@RequestParam("requestId") int requestId) {
		HttpStatus status = null;
		CustomerProfileDTO dto = null;
		
		try {
			dto = new CustomerProfileDTO();
			Customer entity = service.getCustomerProfileInRequestId(requestId);
			BeanUtils.copyProperties(entity,dto);
			status = HttpStatus.OK;

		} catch (Exception e) {
			status = HttpStatus.BAD_REQUEST;
			e.printStackTrace();

		}
		return new ResponseEntity<CustomerProfileDTO>(dto, status);
	}
	
	@Secured({"ROLE_EXPERT","ROLE_CUSTOMER"})
	@GetMapping(value = "/exp")
	public ResponseEntity<ExpertProfileDTO> profileExpertRequest(@RequestParam("requestId") int requestId) {
		HttpStatus status = null;
		ExpertProfileDTO dto = null;
		
		try {
			dto = new ExpertProfileDTO();
			Expert entity = service.getExpertProfileInRequestId(requestId);
			MajorDTO marjDto = new MajorDTO();
			BeanUtils.copyProperties(entity.getMajor(), marjDto);
			BeanUtils.copyProperties(entity,dto);
			dto.setMajor(marjDto);
			status = HttpStatus.OK;

		} catch (Exception e) {
			status = HttpStatus.BAD_REQUEST;
			e.printStackTrace();

		}
		return new ResponseEntity<ExpertProfileDTO>(dto, status);
	}
	
	
	

	
}
