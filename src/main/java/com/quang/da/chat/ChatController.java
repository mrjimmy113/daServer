package com.quang.da.chat;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quang.da.service.ChatService;

@RestController
public class ChatController {
	
	@Autowired
	ChatService service;
	

    @MessageMapping("/chat.{channel}")
    @SendTo("/topic/messages.{channel}")
    public OutputMessage send(@DestinationVariable int channel,SendMessage message, Principal principal) throws Exception {

        return service.saveMessage(channel,message,(SocketUser)principal);
    }
    
    @Secured({"ROLE_EXPERT","ROLE_CUSTOMER"})
    @GetMapping("/chatMessage")
    public ResponseEntity<List<OutputMessage>> getMessages(@RequestParam int requestId, @RequestParam int page) {
    	HttpStatus status = null;

    	List<OutputMessage> outputList = new ArrayList<OutputMessage>();
    	try {
			outputList = service.getChatMessageByRequestId(requestId, page);
    		status = HttpStatus.OK;
    		
		} catch (Exception e) {
			e.printStackTrace();
			status = HttpStatus.BAD_REQUEST;
		}
    	return new ResponseEntity<List<OutputMessage>>(outputList,status);
    }

}
