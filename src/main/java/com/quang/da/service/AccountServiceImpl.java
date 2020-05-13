package com.quang.da.service;

import java.sql.Date;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.quang.da.entity.Account;
import com.quang.da.entity.Customer;
import com.quang.da.entity.Expert;
import com.quang.da.repository.CustomerRepository;
import com.quang.da.repository.ExpertRepository;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private CustomerRepository cusRep;

	@Autowired
	private ExpertRepository expRep;

	@Autowired
	private JwtService jwt;

	private PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	private Account getUserContext() {
		return (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	private boolean isEmailAvailable(String email) {
		return expRep.findOneByEmail(email).isPresent() || cusRep.findOneByEmail(email).isPresent();
	}

	@Override
	public String login(String email, String password,boolean isExpert) throws JOSEException {
		boolean isExisted = false;

		if (isExpert)
			isExisted = (expRep.findOneByEmailAndPassword(email, password).isPresent());
		else
			isExisted = (cusRep.findOneByEmailAndPassword(email, password).isPresent());

		if (isExisted)
			return jwt.generateAuthToken(email,isExpert);

		return "";
	}

	@Override
	public boolean register(Expert entity) {
		if(isEmailAvailable(entity.getEmail()))  return false;
		
		entity.setPassword(passwordEncoder().encode(entity.getEmail()));
		entity.setCreatedDate(new Date(Calendar.getInstance().getTimeInMillis()));
		
		expRep.save(entity);
		
		return true;
	}
	

	@Override
	public boolean register(Customer entity) {
		if(isEmailAvailable(entity.getEmail()))  return false;
		
		entity.setPassword(passwordEncoder().encode(entity.getEmail()));
		entity.setCreatedDate(new Date(Calendar.getInstance().getTimeInMillis()));
		
		cusRep.save(entity);
		
		return true;
	}
	
	@Override
	public Expert getProfileExpert() {
		Expert result = (Expert) getUserContext();
		result.setPassword(null);
		return result;
	}
	
	@Override
	public Customer getProfileCustomer() {
		Customer result = (Customer) getUserContext();
		result.setPassword(null);
		return result;
	}
	
	@Override
	public void updateProfile(Expert infor) {
		Expert entity = (Expert) getUserContext();
		entity.setDescription(infor.getDescription());
		entity.setFeePerHour(infor.getFeePerHour());
		entity.setFirstname(infor.getFirstname());
		entity.setLastname(infor.getLastname());
		entity.setMajor(infor.getMajor());
		expRep.save(entity);
	}
	
	@Override
	public void updateProfile(Customer infor) {
		Customer entity = (Customer) getUserContext();
		entity.setFirstname(infor.getFirstname());
		entity.setLastname(infor.getLastname());
		entity.setAddress(infor.getAddress());
		entity.setCity(infor.getCity());
		entity.setDob(infor.getDob());
		entity.setPrimaryLanguage(infor.getPrimaryLanguage());
		cusRep.save(entity);
	}
	
	@Override
	public boolean changePassword(String currentPassword, String newPassword) {
		if(!passwordEncoder().matches(currentPassword, getUserContext().getPassword())) return false;
		
		Account account = getUserContext();
		account.setPassword(passwordEncoder().encode(newPassword));
		
		if(getUserContext().isExpert()) {
			expRep.save((Expert)account);
		}else {
			cusRep.save((Customer)account);
		}
		
		return true;
	}

	
}
