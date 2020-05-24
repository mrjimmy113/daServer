package com.quang.da.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CustomUser extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private boolean isExpert;

	public CustomUser(Integer id,String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.id = id;
		if(authorities.contains(new SimpleGrantedAuthority("ROLE_" + "EXPERT"))) {
			isExpert = true;
		}else {
			isExpert = false;
		}

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isExpert() {
		return isExpert;
	}

	public void setExpert(boolean isExpert) {
		this.isExpert = isExpert;
	}
	
	

}
