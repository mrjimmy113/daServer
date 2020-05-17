package com.quang.da.service;

import java.text.ParseException;

import com.nimbusds.jose.JOSEException;
import com.quang.da.entity.Customer;
import com.quang.da.entity.Expert;
import com.quang.da.service.customResult.CheckTokenResult;

public interface AccountService {



	boolean register(Customer entity);

	boolean register(Expert entity);

	boolean changePassword(String currentPassword, String newPassword);

	Customer getProfileCustomer();

	Expert getProfileExpert();

	void updateProfile(Expert infor);

	void updateProfile(Customer infor);

	String login(String email, String password) throws JOSEException;

	CheckTokenResult checkToken(String token) throws ParseException, JOSEException;
}
