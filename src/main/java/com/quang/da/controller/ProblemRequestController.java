package com.quang.da.controller;


import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.quang.da.dto.ProblemRequestDTO;
import com.quang.da.dto.ProblemRequestDetailDTO;
import com.quang.da.entity.ProblemRequest;
import com.quang.da.service.ProblemRequestService;
import com.quang.da.service.StorageService;

@RestController
@RequestMapping("/request")
public class ProblemRequestController {
	
	@Autowired
	ProblemRequestService service;
	
	@Autowired
	StorageService storageSer;
	
	@PostMapping
	public ResponseEntity<Number> createRequest(@RequestParam("files") MultipartFile[] files,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date endDate,
			@RequestParam("title") String title, @RequestParam("description") String description) {
		HttpStatus status = null;
		try {
			ProblemRequest entity = new ProblemRequest();
			entity.setTitle(title);
			entity.setDescription(description);
			entity.setDeadlineDate(new Date(endDate.getTime()));
			service.createRequest(files,entity);
			status = HttpStatus.CREATED;
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<Number>(status.value(),status);
	}
	
	@PutMapping
	public ResponseEntity<Number> updateRequest(@RequestParam("files") MultipartFile[] files,
			@RequestParam("requestId") Integer id,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date endDate,
			@RequestParam("title") String title, @RequestParam("description") String description,
			@RequestParam("delImgs") String[] delImgs) {
		HttpStatus status = null;
		try {
			ProblemRequest entity = new ProblemRequest();
			entity.setRequestId(id);
			entity.setTitle(title);
			entity.setDescription(description);
			entity.setDeadlineDate(new Date(endDate.getTime()));
			service.modifyRequest(files,entity,delImgs);
			status = HttpStatus.OK;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return new ResponseEntity<Number>(status.value(),status);
	}
	
	
	
	@PatchMapping("/accept")
	public ResponseEntity<Number> acceptExpert(@RequestParam int requestId, @RequestParam int expertId) {
		HttpStatus status = null;
		try {
			service.acceptExpert(expertId, requestId);
			status = HttpStatus.OK;
		} catch (Exception e) {
			// TODO: handle exception
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
			// TODO: handle exception
		}
		return new ResponseEntity<Number>(status.value(),status);
	}
	
	@GetMapping("/detail")
	public ResponseEntity<ProblemRequestDetailDTO> getProblemDetail (@RequestParam("id") int id) {
		HttpStatus status = null;
		ProblemRequestDetailDTO result = null;
		try {
			ProblemRequest entity = service.getProblemRequestDetail(id);
			BeanUtils.copyProperties(entity, result);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return new ResponseEntity<ProblemRequestDetailDTO>(result,status);
	}
	
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
	
	
	

	
}
