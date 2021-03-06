package com.quang.da.security;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.quang.da.service.JwtService;

@Component
public class SecurityFilter extends UsernamePasswordAuthenticationFilter {
	private final static String TOKEN_HEADER = "authorization";

	@Autowired
	private JwtService jwt;

	@Autowired
	private UserDetailsService ser;

	@Autowired
	@Override
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}
	
	private final static Logger LOGGER = Logger.getLogger(SecurityFilter.class.getName());

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) req;
		String authToken = httpRequest.getHeader(TOKEN_HEADER);
		if (authToken != null && !authToken.trim().isEmpty()) {
			try {
				if (jwt.validateTokenEmail(authToken)) {
					String email = jwt.getEmailFromToken(authToken);
					UserDetails userDetails = ser.loadUserByUsername(email);
					if (userDetails != null) {
						UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
						SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					}
				}
			} catch (UsernameNotFoundException e) {
				LOGGER.log(Level.WARNING, e.getMessage());
			} catch (java.text.ParseException e) {
				LOGGER.log(Level.WARNING, authToken +  e.getMessage());
			} catch (JOSEException e) {
				LOGGER.log(Level.WARNING, e.getMessage());
			}
		}
		chain.doFilter(req, res);
	}
}
