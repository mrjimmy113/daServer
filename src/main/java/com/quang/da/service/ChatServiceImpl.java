package com.quang.da.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quang.da.chat.OutputMessage;
import com.quang.da.chat.SendMessage;
import com.quang.da.entity.ChatMessage;
import com.quang.da.entity.Customer;
import com.quang.da.entity.Expert;
import com.quang.da.entity.ProblemRequest;
import com.quang.da.repository.ChatMessageRepository;
import com.quang.da.repository.CustomerRepository;
import com.quang.da.repository.ExpertRepository;

@Service
public class ChatServiceImpl implements ChatService {


	
	@Autowired
	ChatMessageRepository rep;
	
	@Autowired
	ExpertRepository expRep;
	
	@Autowired
	CustomerRepository cusRep;
	
	@Transactional(rollbackOn = Exception.class)
	@Override
	public OutputMessage saveMessage(int requestId,SendMessage message, String email) {
		String time = new SimpleDateFormat("HH:mm").format(System.currentTimeMillis());
		ChatMessage chatMessage = new ChatMessage();
		ProblemRequest request = new ProblemRequest();
		request.setRequestId(requestId);
		chatMessage.setRequest(request);
		chatMessage.setMessage(message.getMessage());
		chatMessage.setTime(new Timestamp(System.currentTimeMillis()));
		chatMessage.setMessageType(message.getType());
		if(message.isExpert()) {
			Expert expert = expRep.findOneByEmail(email).get();
			chatMessage.setExpert(expert);
		}else {
			Customer customer = cusRep.findOneByEmail(email).get();
			chatMessage.setCustomer(customer);
		}
		rep.save(chatMessage);
		
		
		
        return new OutputMessage(message.isExpert(),message.getMessage(), message.getType(),"Today - " + time);
	}
	
	@Override
	public List<OutputMessage> getChatMessageByRequestId(int requestId) {
		List<OutputMessage> outputList = new ArrayList<OutputMessage>();
		List<ChatMessage> chatMessages = rep.findAllMessageOfRequest(requestId);
		
		SimpleDateFormat today = new SimpleDateFormat("HH:mm");
		SimpleDateFormat time = new SimpleDateFormat("MM:dd - HH:mm");
		
		for (ChatMessage chatMessage : chatMessages) {
			OutputMessage outputMessage = new OutputMessage();
			outputMessage.setMessage(chatMessage.getMessage());
			outputMessage.setType(chatMessage.getMessageType());
			
			if(chatMessage.getCustomer() == null) outputMessage.setExpert(false);
			if(chatMessage.getExpert() == null) outputMessage.setExpert(true);
			Calendar now = Calendar.getInstance();
			Calendar then = Calendar.getInstance();
			then.setTimeInMillis(chatMessage.getTime().getTime());
			Date date = new Date(then.getTimeInMillis());
			if(now.get(Calendar.DATE) == then.get(Calendar.DATE)) {
				outputMessage.setTime("Today - " + today.format(date));
			}else {
				outputMessage.setTime(time.format(date));
			}
			outputList.add(outputMessage);
		}
		return outputList;
	}
}
