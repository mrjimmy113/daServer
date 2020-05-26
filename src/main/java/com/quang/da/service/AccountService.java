package com.quang.da.service;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.web.multipart.MultipartFile;

import com.nimbusds.jose.JOSEException;
import com.quang.da.entity.Account;
import com.quang.da.entity.Customer;
import com.quang.da.entity.Expert;
import com.quang.da.service.customResult.CheckTokenResult;

public interface AccountService {

	boolean changePassword(String currentPassword, String newPassword);

	Customer getProfileCustomer();

	Expert getProfileExpert();



	String login(String email, String password) throws JOSEException;

	CheckTokenResult checkToken(String token) throws ParseException, JOSEException;

	boolean register(Account entity);

	void updateProfile(MultipartFile file, Customer infor) throws IOException;
}
