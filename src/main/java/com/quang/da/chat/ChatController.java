package com.quang.da.chat;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.quang.da.service.ChatService;

@Controller
public class ChatController {
	
	@Autowired
	ChatService service;
	

    @MessageMapping("/chat.{channel}")
    @SendTo("/topic/messages.{channel}")
    public OutputMessage send(@DestinationVariable int channel,SendMessage message, Principal principal) throws Exception {
    	System.out.println(channel);
    	System.out.println(((SocketUser) principal).getName());
        return service.saveMessage(channel,message,principal.getName());
    }

}
