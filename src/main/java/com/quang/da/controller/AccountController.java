package com.quang.da.controller;

import java.sql.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quang.da.dto.CustomerProfileDTO;
import com.quang.da.dto.ExpertProfileDTO;
import com.quang.da.dto.MajorDTO;
import com.quang.da.entity.Customer;
import com.quang.da.entity.Expert;
import com.quang.da.entity.Major;
import com.quang.da.service.AccountService;
import com.quang.da.service.customResult.CheckTokenResult;

@RestController
@RequestMapping("/account")
public class AccountController {

	@Autowired
	private AccountService service;
	
	
	@PostMapping(value = "/login")
	public ResponseEntity<String> login(@RequestParam("email") String email, @RequestParam("password") String password) {
		HttpStatus status = null;
		String result = null;
		try {

			result = service.login(email, password);
			status = HttpStatus.OK;

		} catch (Exception e) {
			status = HttpStatus.BAD_REQUEST;
			e.printStackTrace();

		}
		return new ResponseEntity<String>(result, status);
	}
	
	@PostMapping(value = "check")
	public ResponseEntity<Boolean> checkToken(@RequestParam("token") String token) {
		HttpStatus status = null;
		Boolean result = null;
		try {
			CheckTokenResult res = service.checkToken(token);
			if(res.isValid()) {
				status = HttpStatus.OK;
			}else {
				status = HttpStatus.ACCEPTED;
			}
			
			result = res.isExpert();
			
		} catch (Exception e) {
			status = HttpStatus.BAD_REQUEST;
			e.printStackTrace();
		}
		return new ResponseEntity<Boolean>(result,status);
	}
	
	@PostMapping(value = "/changePassword")
	public ResponseEntity<Number> changePassword(@RequestParam String currentPassword,@RequestParam String newPassword) {
		HttpStatus status = null;

		try {
			boolean res = service.changePassword(currentPassword, newPassword);

			if (res) {
				status = HttpStatus.OK;
			} else {
				status = HttpStatus.ACCEPTED;
			}

		} catch (Exception e) {
			status = HttpStatus.BAD_REQUEST;
			e.printStackTrace();

		}
		return new ResponseEntity<Number>(status.value(), status);
	}
	
	@GetMapping(value = "/cus")
	public ResponseEntity<CustomerProfileDTO> profileCustomer() {
		HttpStatus status = null;
		CustomerProfileDTO dto = null;
		
		try {
			dto = new CustomerProfileDTO();
			Customer entity = service.getProfileCustomer();
			BeanUtils.copyProperties(entity,dto);
			status = HttpStatus.OK;

		} catch (Exception e) {
			status = HttpStatus.BAD_REQUEST;
			e.printStackTrace();

		}
		return new ResponseEntity<CustomerProfileDTO>(dto, status);
	}
	
	@PostMapping(value = "/cus")
	public ResponseEntity<Number> register(@RequestBody CustomerProfileDTO infor) {
		HttpStatus status = null;
		try {
			Customer entity = new Customer();
			BeanUtils.copyProperties(infor, entity);
			entity.setDob(new Date(infor.getDob().getTime()));
			boolean res = service.register(entity);

			if (res) {
				status = HttpStatus.CREATED;
			} else {
				status = HttpStatus.CONFLICT;
			}
			System.out.println(status.toString());
		} catch (Exception e) {
			status = HttpStatus.BAD_REQUEST;
			e.printStackTrace();

		}
		return new ResponseEntity<Number>(status.value(), status);
	}
	
	@PutMapping(value = "/cus")
	public ResponseEntity<Number> updateCustomer(
			@RequestParam(name = "file", required = false) MultipartFile file
			,@RequestParam String infor) {
		HttpStatus status = null;

		try {
			CustomerProfileDTO dto = new CustomerProfileDTO();
			Gson gson = new GsonBuilder().create();
			dto = gson.fromJson(infor, CustomerProfileDTO.class);
			Customer entity = new Customer();
			BeanUtils.copyProperties(dto, entity);
			entity.setDob(new Date(dto.getDob().getTime()));
			service.updateProfile(file,entity);

			status = HttpStatus.OK;

		} catch (Exception e) {
			status = HttpStatus.BAD_REQUEST;
			e.printStackTrace();

		}
		return new ResponseEntity<Number>(status.value(), status);
	}
	
	@GetMapping(value = "/exp")
	public ResponseEntity<ExpertProfileDTO> profileExpert() {
		HttpStatus status = null;
		ExpertProfileDTO dto = null;
		try {
			dto = new ExpertProfileDTO();
			Expert entity = service.getProfileExpert();
			BeanUtils.copyProperties(entity,dto);
			MajorDTO marDto = new MajorDTO();
			BeanUtils.copyProperties(entity.getMajor(), marDto);
			dto.setMajor(marDto);
			
			

			status = HttpStatus.OK;

		} catch (Exception e) {
			status = HttpStatus.BAD_REQUEST;
			e.printStackTrace();

		}
		return new ResponseEntity<ExpertProfileDTO>(dto, status);
	}
	
	@PostMapping(value = "/exp")
	public ResponseEntity<Number> registerExpert(@RequestBody ExpertProfileDTO infor) {
		HttpStatus status = null;

		try {
			Expert entity = new Expert();
			BeanUtils.copyProperties(infor, entity);
			Major major = new Major();
			major.setId(infor.getMajor().getId());
			entity.setMajor(major);
			boolean res = service.registerExpert(entity);

			if (res) {
				status = HttpStatus.CREATED;
			} else {
				status = HttpStatus.CONFLICT;
			}

		} catch (Exception e) {
			status = HttpStatus.BAD_REQUEST;
			e.printStackTrace();

		}
		return new ResponseEntity<Number>(status.value(), status);
	}
	
	@PutMapping(value = "/exp")
	public ResponseEntity<Number> updateExpert(@RequestParam(name = "file", required = false) MultipartFile file
			,@RequestParam String infor) {
		HttpStatus status = null;

		try {
			ExpertProfileDTO dto = new ExpertProfileDTO();
			Gson gson = new GsonBuilder().create();
			dto = gson.fromJson(infor, ExpertProfileDTO.class);
			Expert entity = new Expert();
			BeanUtils.copyProperties(dto, entity);
			Major major = new Major();
			BeanUtils.copyProperties(dto.getMajor(), major);
			entity.setMajor(major);
			service.updateProfileExpert(file,entity);

			status = HttpStatus.OK;

		} catch (Exception e) {
			status = HttpStatus.BAD_REQUEST;
			e.printStackTrace();

		}
		return new ResponseEntity<Number>(status.value(), status);
	}
}
