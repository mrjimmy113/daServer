package com.quang.da.service;

import java.sql.Date;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.quang.da.entity.Customer;
import com.quang.da.entity.Expert;
import com.quang.da.repository.CustomerRepository;
import com.quang.da.repository.ExpertRepository;
import com.quang.da.service.customResult.CheckTokenResult;

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
	
	
	private User getUserContext() {
		
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	private boolean isEmailAvailable(String email) {
		return expRep.findOneByEmail(email).isPresent() || cusRep.findOneByEmail(email).isPresent();
	}

	@Override
	public String login(String email, String password) throws JOSEException {
		boolean isExpert = false;
		boolean isExisted = false;
		Optional<Expert> expert = expRep.findOneByEmail(email);
		if(expert.isPresent()) {
			if(passwordEncoder().matches(password, expert.get().getPassword())) {
				isExisted = true;
				isExpert = true;
			}
		}
		
		Optional<Customer> customer = cusRep.findOneByEmail(email);
		if(customer.isPresent()) {
			if(passwordEncoder().matches(password, customer.get().getPassword())) {
				isExisted = true;
			}
		}
			

		if (isExisted)
			return jwt.generateAuthToken(email,isExpert);

		return "0";
	}
	
	@Override
	public CheckTokenResult checkToken (String token) throws ParseException, JOSEException {
		boolean isValid = jwt.validateTokenEmail(token);
		Boolean isExpert = jwt.getIsExpertFromToken(token);
		if(isExpert == null) {
			isValid = false;
			isExpert = false;
		}
		
		return new CheckTokenResult(isExpert, isValid);
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
		Expert result = expRep.findOneByEmail(getUserContext().getUsername()).get();
		result.setPassword(null);
		
		return result;
	}
	
	@Override
	public Customer getProfileCustomer() {
		Customer result = cusRep.findOneByEmail(getUserContext().getUsername()).get();
		result.setPassword(null);
		return result;
	}
	
	@Override
	public void updateProfile(Expert infor) {
		Expert entity = expRep.findOneByEmail(getUserContext().getUsername()).get();
		entity.setDescription(infor.getDescription());
		entity.setFeePerHour(infor.getFeePerHour());
		entity.setFirstname(infor.getFirstname());
		entity.setLastname(infor.getLastname());
		entity.setMajor(infor.getMajor());
		expRep.save(entity);
	}
	
	@Override
	public void updateProfile(Customer infor) {
		Customer entity = cusRep.findOneByEmail(getUserContext().getUsername()).get();
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
		boolean isExpert = false;
		if(getUserContext().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EXPERT"))) {
			isExpert = true;
		}
		
		if(isExpert) {
			Expert expert = expRep.findOneByEmail(getUserContext().getUsername()).get();
			expert.setPassword(passwordEncoder().encode(newPassword));
			expRep.save(expert);
		}else {
			Customer cus = cusRep.findOneByEmail(getUserContext().getUsername()).get();
			cus.setPassword(passwordEncoder().encode(newPassword));
			cusRep.save(cus);
		}

		
		
		return true;
	}

	
}


