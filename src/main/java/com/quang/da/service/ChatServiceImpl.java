package com.quang.da.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quang.da.chat.MessageType;
import com.quang.da.chat.OutputMessage;
import com.quang.da.chat.SendMessage;
import com.quang.da.chat.SocketUser;
import com.quang.da.dto.EstimateDTO;
import com.quang.da.entity.ChatMessage;
import com.quang.da.entity.Customer;
import com.quang.da.entity.Expert;
import com.quang.da.entity.ProblemRequest;
import com.quang.da.entity.Status;
import com.quang.da.enumaration.StatusEnum;
import com.quang.da.repository.ChatMessageRepository;
import com.quang.da.repository.CustomerRepository;
import com.quang.da.repository.ExpertRepository;
import com.quang.da.repository.ProblemRequestRepository;
import com.quang.da.repository.StatusRepository;

@Service
public class ChatServiceImpl implements ChatService {

	@Autowired
	ProblemRequestRepository proRep;

	@Autowired
	ChatMessageRepository rep;

	@Autowired
	ExpertRepository expRep;

	@Autowired
	CustomerRepository cusRep;

	@Autowired
	StatusRepository statusRep;

	@Transactional(rollbackOn = Exception.class)
	@Override
	public OutputMessage saveMessage(int requestId, SendMessage message, SocketUser user) {
		String time = new SimpleDateFormat("HH:mm").format(System.currentTimeMillis());
		ChatMessage chatMessage = new ChatMessage();
		ProblemRequest request = new ProblemRequest();
		request.setRequestId(requestId);
		chatMessage.setRequest(request);
		chatMessage.setMessage(message.getMessage());
		chatMessage.setTime(new Timestamp(System.currentTimeMillis()));
		chatMessage.setMessageType(message.getType());
		if (user.isExpert()) {
			Expert expert = expRep.findOneByEmail(user.getName()).get();
			chatMessage.setExpert(expert);
		} else {
			Customer customer = cusRep.findOneByEmail(user.getName()).get();
			chatMessage.setCustomer(customer);
		}
		rep.save(chatMessage);
		if (message.getType() != MessageType.CHAT)
			switch (message.getType()) {
			case ESTIMATE: {
				
				break;
			}
			
			
			case ESTIMATE_YES: {
				
				request = proRep.findById(requestId).get();
				Gson gson = new GsonBuilder().create();
				EstimateDTO estimateDTO = gson.fromJson(message.getMessage(), EstimateDTO.class);
				request.setTotal(estimateDTO.getTotal());
				request.setEstimateHour(estimateDTO.getHour());
				Status processStatus = statusRep.findOneByStatus(StatusEnum.PROCESSING);
				request.setStatus(processStatus);
				proRep.save(request);
				break;
			}
			
			case COMPLETE: {
				request = proRep.findById(requestId).get();
				switch (request.getStatus().getStatus()) {
					case PROCESSING: {
						Status newStatus = statusRep.findOneByStatus(StatusEnum.TMPCOMPLETE);
						request.setStatus(newStatus);
						proRep.save(request);
						break;
					}
	
					case TMPCANCEL: {
						Optional<ChatMessage> lastestCancel = rep.findLastMessageByType(requestId, MessageType.COMPLETE);
						if(!lastestCancel.isPresent()) break;
						
						ChatMessage chMessage = lastestCancel.get();
						if(chMessage.isExpert()) {
							if(chMessage.getExpert().getEmail().equals(user.getName())) break;
						}else {
							if(chMessage.getCustomer().getEmail().equals(user.getName())) break;
						}
						
						Status newStatus = statusRep.findOneByStatus(StatusEnum.COMPLETE);
						request.setStatus(newStatus);
						proRep.save(request);
						message.setType(MessageType.COMPLETE_YES);
						break;
					}
	
					default:
						break;
				}
				break;
			}

			case CANCEL: {
				request = proRep.findById(requestId).get();
				switch (request.getStatus().getStatus()) {
					case ACCEPTED: {
						Status newStatus = statusRep.findOneByStatus(StatusEnum.CANCEL);
						request.setStatus(newStatus);
						//request.setExpert(null);
						proRep.save(request);
						message.setType(MessageType.CANCEL_YES);
						break;
					}
	
					case PROCESSING: {
						Status newStatus = statusRep.findOneByStatus(StatusEnum.TMPCANCEL);
						request.setStatus(newStatus);
						proRep.save(request);
						break;
					}
	
					case TMPCANCEL: {
						Optional<ChatMessage> lastestCancel = rep.findLastMessageByType(requestId, MessageType.CANCEL);
						if(!lastestCancel.isPresent()) break;
						
						ChatMessage chMessage = lastestCancel.get();
						if(chMessage.isExpert()) {
							if(chMessage.getExpert().getEmail().equals(user.getName())) break;
						}else {
							if(chMessage.getCustomer().getEmail().equals(user.getName())) break;
						}
						
						Status newStatus = statusRep.findOneByStatus(StatusEnum.CANCEL);
						request.setStatus(newStatus);
						proRep.save(request);
						message.setType(MessageType.CANCEL_YES);
						break;
					}
	
					default:
						break;
				}
				break;
			}

			default:
				break;

			}

		return new OutputMessage(user.isExpert(), message.getMessage(), message.getType(), "Today - " + time);
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

			if (chatMessage.getCustomer() == null)
				outputMessage.setExpert(true);
			if (chatMessage.getExpert() == null)
				outputMessage.setExpert(false);
			Calendar now = Calendar.getInstance();
			Calendar then = Calendar.getInstance();
			then.setTimeInMillis(chatMessage.getTime().getTime());
			Date date = new Date(then.getTimeInMillis());
			if (now.get(Calendar.DATE) == then.get(Calendar.DATE)) {
				outputMessage.setTime("Today - " + today.format(date));
			} else {
				outputMessage.setTime(time.format(date));
			}
	
			
			outputList.add(outputMessage);
		}
		return outputList;
	}
}
