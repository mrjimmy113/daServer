package com.quang.da.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quang.da.dto.MajorDTO;
import com.quang.da.entity.Major;
import com.quang.da.service.MajorService;

@RestController
@RequestMapping("/major")
public class MajorController {

	@Autowired
	private MajorService ser;
	
	
	@Secured({"ROLE_EXPERT","ROLE_CUSTOMER"})
	@GetMapping
	public ResponseEntity<List<MajorDTO>> getAllMajor() {
		HttpStatus status = null;
		List<MajorDTO> dtos = new ArrayList<MajorDTO>();
		
		try {

			List<Major> majors = ser.getAllMajor();
			for (Major major : majors) {
				MajorDTO dto = new MajorDTO();
				BeanUtils.copyProperties(major, dto);
				dtos.add(dto);
			}
			
			status = HttpStatus.OK;
		} catch (Exception e) {
			status = HttpStatus.BAD_REQUEST;
		}
		
		return new ResponseEntity<List<MajorDTO>>(dtos,status);
		
	}
}
