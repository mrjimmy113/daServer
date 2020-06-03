package com.quang.da.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quang.da.chat.OutputMessage;
import com.quang.da.chat.SendMessage;
import com.quang.da.entity.Expert;
import com.quang.da.entity.ExpertMessage;
import com.quang.da.entity.ProblemRequest;
import com.quang.da.repository.ChatMessageRepository;
import com.quang.da.repository.CustomerMessageRepository;
import com.quang.da.repository.ExpertMessageRepository;
import com.quang.da.repository.ExpertRepository;

@Service
public class ChatServiceImpl implements ChatService {

	@Autowired
	CustomerMessageRepository cusMesRep;
	
	@Autowired
	ExpertMessageRepository expMesRep;
	
	@Autowired
	ChatMessageRepository rep;
	
	@Autowired
	ExpertRepository expRep;
	
	@Transactional(rollbackOn = Exception.class)
	@Override
	public OutputMessage saveMessage(int requestId,SendMessage message, String email) {
		String time = new SimpleDateFormat("HH:mm").format(new Date());

		if(message.isExpert()) {
			ExpertMessage entity = new ExpertMessage();
			Expert expert = expRep.findOneByEmail(email).get();
			entity.setExpert(expert);
			expMesRep.save(entity);
			ProblemRequest request = new ProblemRequest();
			request.setRequestId(requestId);

			
			
		}else {
			
		}
		
		
		
        return new OutputMessage(message.isExpert(),message.getMessage(), message.getType(), time);
	}
}
