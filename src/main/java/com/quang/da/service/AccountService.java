package com.quang.da.service;

import com.nimbusds.jose.JOSEException;
import com.quang.da.entity.Customer;
import com.quang.da.entity.Expert;

public interface AccountService {

	String login(String email, String password, boolean isExpert) throws JOSEException;

	boolean register(Customer entity);

	boolean register(Expert entity);

	boolean changePassword(String currentPassword, String newPassword);

	Customer getProfileCustomer();

	Expert getProfileExpert();

	void updateProfile(Expert infor);

	void updateProfile(Customer infor);
}
