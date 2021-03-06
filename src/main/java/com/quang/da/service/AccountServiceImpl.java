package com.quang.da.service;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nimbusds.jose.JOSEException;
import com.quang.da.entity.Customer;
import com.quang.da.entity.Expert;
import com.quang.da.entity.Status;
import com.quang.da.enumaration.StatusEnum;
import com.quang.da.repository.CustomerRepository;
import com.quang.da.repository.ExpertRepository;
import com.quang.da.repository.StatusRepository;
import com.quang.da.service.customResult.CheckTokenResult;

import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private CustomerRepository cusRep;

	@Autowired
	private ExpertRepository expRep;
	
	@Autowired
	private StatusRepository statusRep;
	
	@Autowired
	private StorageService storageSer;

	@Autowired
	private JwtService jwt;
	
	@Autowired
	private MailService mail;
	
	@Value("${server.path}")
	private String serverPath;

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
	public boolean register(Customer entity) {
		if(isEmailAvailable(entity.getEmail()))  return false;
		
		entity.setPassword(passwordEncoder().encode(entity.getPassword()));
		entity.setCreatedDate(new Date(Calendar.getInstance().getTimeInMillis()));
		Status status = statusRep.findOneByStatus(StatusEnum.NEW);
		entity.setStatus(status);
		cusRep.save(entity);

		
		return true;
	}
	
	@Override
	public boolean registerExpert(Expert entity) {
		if(isEmailAvailable(entity.getEmail()))  return false;
		
		entity.setPassword(passwordEncoder().encode(entity.getPassword()));
		entity.setCreatedDate(new Date(Calendar.getInstance().getTimeInMillis()));
		Status status = statusRep.findOneByStatus(StatusEnum.NEW);
		entity.setStatus(status);
		expRep.save(entity);
		
		
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
	

	public void updateProfile(Expert infor) {
		Expert entity = expRep.findOneByEmail(getUserContext().getUsername()).get();
		entity.setDescription(infor.getDescription());
		entity.setFeePerHour(infor.getFeePerHour());

		entity.setMajor(infor.getMajor());
		expRep.save(entity);
	}
	
	@Transactional(rollbackOn = Exception.class)
	@Override
	public void updateProfile(MultipartFile file,Customer infor) throws IOException {
		Customer entity = cusRep.findOneByEmail(getUserContext().getUsername()).get();
		entity.setAddress(infor.getAddress());
		entity.setCity(infor.getCity());
		entity.setDob(infor.getDob());
		entity.setPrimaryLanguage(infor.getPrimaryLanguage());
		
		if(file != null) {
			String imgName = storageSer.saveFileFromMultipartFile(file);
			entity.setImgName(imgName);
		}
		cusRep.save(entity);
	}
	
	@Transactional(rollbackOn = Exception.class)
	@Override
	public void updateProfileExpert(MultipartFile file,Expert infor) throws IOException {
		Expert entity = expRep.findOneByEmail(getUserContext().getUsername()).get();
		entity.setMajor(infor.getMajor());
		entity.setFullName(infor.getFullName());
		entity.setFeePerHour(infor.getFeePerHour());
		entity.setBankName(infor.getBankName());
		entity.setBankAccountNo(infor.getBankAccountNo());
		entity.setDescription(infor.getDescription());
		
		if(file != null) {
			String imgName = storageSer.saveFileFromMultipartFile(file);
			entity.setImgName(imgName);
		}
		expRep.save(entity);
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
	
	@Override
	public boolean forgetPassword(String email) throws JOSEException, TemplateNotFoundException, MalformedTemplateNameException, freemarker.core.ParseException, MessagingException, IOException, TemplateException {
		boolean isExist = false;
		String token = "";
		Optional<Customer> opCus = cusRep.findOneByEmail(email);
		if(opCus.isPresent()) {
			isExist = true;
			token = jwt.generateAuthToken(email, false);
		}
		Optional<Expert> opExp = expRep.findOneByEmail(email);
		if(opExp.isPresent()) {
			isExist = true;
			token = jwt.generateAuthToken(email, true);
		}
		if(isExist) {
			Map<String, Object> model = new HashMap<>();
			model.put("link", serverPath + "/account/reset?token=" + token);
			String[] mailList = new String[1];
			mailList[0] = email;
			mail.sendMail(mailList, "forget.ftl", model);
		}
		
		return isExist;
	}
	
	@Override
	public void sendNewPassword(String token) throws ParseException, JOSEException, TemplateNotFoundException, MalformedTemplateNameException, freemarker.core.ParseException, MessagingException, IOException, TemplateException {
		String email = jwt.getEmailFromToken(token);
		boolean isExpert = jwt.getIsExpertFromToken(token);
		String newPassword = UUID.randomUUID().toString();
		newPassword = newPassword.substring(0, 7);
		String password = newPassword;
		newPassword = passwordEncoder().encode(newPassword);
		if(isExpert) {
			Expert expert = expRep.findOneByEmail(email).get();
			expert.setPassword(newPassword);
			expRep.save(expert);
		}else {
			Customer customer = cusRep.findOneByEmail(email).get();
			customer.setPassword(newPassword);
			cusRep.save(customer);
		}
		Map<String, Object> model = new HashMap<>();
		model.put("newPassword", password);
		String[] mailList = new String[1];
		mailList[0] = email;
		mail.sendMail(mailList, "newPassword.ftl", model);
	}

	
}


