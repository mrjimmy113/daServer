package com.quang.da.service;

import java.text.ParseException;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Service
public class JwtServiceImpl implements JwtService {
	public static final String EMAIL = "email";
	public static final String ISEXPERT = "expert";
	public static final String SECRET_KEY = "11111111111111111111111111111111";
	public static final int EXPIRE_TIME = 24 * 3600 * 1000;

	@Override
	public String generateAuthToken(String email, boolean isExpert) throws JOSEException {
		String token = null;
		JWSSigner signer = new MACSigner(generateShareSecret());
		JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
		builder.claim(EMAIL, email);
		builder.claim(ISEXPERT, isExpert);
		builder.expirationTime(generateExpirationDate(1));
		JWTClaimsSet claimsSet = builder.build();
		SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
		signedJWT.sign(signer);
		token = signedJWT.serialize();
		return token;
	}
	
	@Override
	public Boolean validateTokenEmail(String token) throws ParseException, JOSEException {
		if (token == null || token.trim().length() == 0) {
			return false;
		}
		String email = getEmailFromToken(token);
		if (email == null || email.isEmpty()) {
			return false;
		}
		if (isTokenExpired(token)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String getEmailFromToken(String token) throws ParseException, JOSEException {
		String email = null;

		JWTClaimsSet claims = getClaimsFromToken(token);
		email = claims.getStringClaim(EMAIL);

		return email;
	}
	
	@Override
	public Boolean getIsExpertFromToken(String token) throws ParseException, JOSEException {
		Boolean isExpert = null;

		JWTClaimsSet claims = getClaimsFromToken(token);
		isExpert = claims.getBooleanClaim(ISEXPERT);

		return isExpert;
	}
	
	@Override
	public boolean isExpertFromToken(String token) throws ParseException, JOSEException {
		boolean isExpert = false;

		JWTClaimsSet claims = getClaimsFromToken(token);
		isExpert = claims.getBooleanClaim(ISEXPERT);

		return isExpert;
	}
	
	private JWTClaimsSet getClaimsFromToken(String token) throws ParseException, JOSEException {
		JWTClaimsSet claims = null;
		SignedJWT signedJWT = SignedJWT.parse(token);
		JWSVerifier verifier = new MACVerifier(generateShareSecret());
		if (signedJWT.verify(verifier)) {
			claims = signedJWT.getJWTClaimsSet();
		}
		return claims;
	}
	
	private Boolean isTokenExpired(String token) throws ParseException, JOSEException {
		Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	private Date getExpirationDateFromToken(String token) throws ParseException, JOSEException {
		Date expiration = null;
		JWTClaimsSet claims = getClaimsFromToken(token);
		expiration = claims.getExpirationTime();
		return expiration;
	}

	
	private byte[] generateShareSecret() {
		// Generate 256-bit (32-byte) shared secret
		byte[] sharedSecret = new byte[32];
		sharedSecret = SECRET_KEY.getBytes();
		return sharedSecret;
	}
	
	private Date generateExpirationDate(int duration) {
		return new Date(System.currentTimeMillis() + EXPIRE_TIME * duration);
	}


}
