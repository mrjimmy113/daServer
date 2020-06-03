package com.quang.da.chat;

import java.security.Principal;
import java.text.ParseException;
import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.nimbusds.jose.JOSEException;
import com.quang.da.service.JwtService;


public class CustomHandshakeHandler extends DefaultHandshakeHandler {

	JwtService jwtService;
	
	
	
	
    public CustomHandshakeHandler(JwtService jwtService) {
		super();
		this.jwtService = jwtService;
	}




	@Override
    protected Principal determineUser(ServerHttpRequest request,
            WebSocketHandler wsHandler, Map<String, Object> attributes) {
        Principal principal = request.getPrincipal();           

        if (principal == null) {
            principal = new SocketUser();

            ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
            
            String uniqueName = "";

            try {
				uniqueName = jwtService.getEmailFromToken(serverHttpRequest.getServletRequest().getParameter("token"));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JOSEException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	

            ((SocketUser) principal).setName(uniqueName);
        }

        return principal;

    }

}
