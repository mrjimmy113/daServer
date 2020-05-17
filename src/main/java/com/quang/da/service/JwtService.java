package com.quang.da.service;

import java.text.ParseException;

import com.nimbusds.jose.JOSEException;

public interface JwtService {

	String getEmailFromToken(String token) throws ParseException, JOSEException;

	Boolean validateTokenEmail(String token) throws ParseException, JOSEException;

	String generateAuthToken(String email, boolean isExpert) throws JOSEException;

	boolean isExpertFromToken(String token) throws ParseException, JOSEException;

	Boolean getIsExpertFromToken(String token) throws ParseException, JOSEException;
}
