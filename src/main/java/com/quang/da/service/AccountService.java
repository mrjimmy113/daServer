package com.quang.da.service;

import java.io.IOException;
import java.text.ParseException;

import javax.mail.MessagingException;

import org.springframework.web.multipart.MultipartFile;

import com.nimbusds.jose.JOSEException;
import com.quang.da.entity.Customer;
import com.quang.da.entity.Expert;
import com.quang.da.service.customResult.CheckTokenResult;

import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public interface AccountService {

	boolean changePassword(String currentPassword, String newPassword);

	Customer getProfileCustomer();

	Expert getProfileExpert();



	String login(String email, String password) throws JOSEException;

	CheckTokenResult checkToken(String token) throws ParseException, JOSEException;

	boolean register(Customer entity);
	
	boolean registerExpert(Expert entity);

	void updateProfile(MultipartFile file, Customer infor) throws IOException;

	void updateProfileExpert(MultipartFile file, Expert infor) throws IOException;

	boolean forgetPassword(String email) throws JOSEException, TemplateNotFoundException, MalformedTemplateNameException, freemarker.core.ParseException, MessagingException, IOException, TemplateException;

	void sendNewPassword(String token) throws ParseException, JOSEException, TemplateNotFoundException, MalformedTemplateNameException, freemarker.core.ParseException, MessagingException, IOException, TemplateException;
}
