package com.quang.da.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.quang.da.entity.Account;
import com.quang.da.entity.Customer;
import com.quang.da.entity.Expert;
import com.quang.da.repository.CustomerRepository;
import com.quang.da.repository.ExpertRepository;

@Service("authService")
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	CustomerRepository cusRep;
	
	@Autowired
	ExpertRepository expRep;
	
	
	private List<GrantedAuthority> buildRoles(String role) {
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("ROLE_" + role));
        return roles;
    }

    private UserDetails userWithRoles(Account acc, List<GrantedAuthority> roles) {
        return new User(acc.getEmail(), acc.getPassword(), roles);
    }
	public UserDetails loadUserByUsername(String username,boolean isExpert) throws UsernameNotFoundException {
		boolean isPresent = false;
		Account account = null;
		if(isExpert) {
			Optional<Expert> acc = expRep.findOneByEmail(username);
			if(acc.isPresent()) {
				isPresent = acc.isPresent();
				account = (Account) acc.get();
			}
		}else {
			Optional<Customer> acc = cusRep.findOneByEmail(username);
			if(acc.isPresent()) {
				isPresent = acc.isPresent();
				account = (Account) acc.get();
			}
		}
		
		
		
		if(isPresent) {
			List<GrantedAuthority> roles;
			if(isExpert) roles = buildRoles("EXPERT");
			else roles = buildRoles("CUSTOMER");
	        return userWithRoles(account, roles);
		}
		return null;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		boolean isPresent = false;
		boolean isExpert = false;
		Account account = null;
		Optional<Customer> cus = cusRep.findOneByEmail(username);
		if(cus.isPresent()) {
			isExpert = false;
			isPresent = cus.isPresent();
			account = (Account) cus.get();
		} else {
			Optional<Expert> exp = expRep.findOneByEmail(username);
			if(exp.isPresent()) {
				isExpert = true;
				isPresent = exp.isPresent();
				account = (Account) exp.get();
			}
		}
		
		if(isPresent) {
			List<GrantedAuthority> roles;
			if(isExpert) roles = buildRoles("EXPERT");
			else roles = buildRoles("CUSTOMER");
	        return userWithRoles(account, roles);
		}
		
		
		
		return null;
	}
}
