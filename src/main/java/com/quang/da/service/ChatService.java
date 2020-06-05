package com.quang.da.service;

import java.util.List;

import com.quang.da.chat.OutputMessage;
import com.quang.da.chat.SendMessage;
import com.quang.da.chat.SocketUser;
import com.quang.da.entity.ChatMessage;

public interface ChatService {



	List<OutputMessage> getChatMessageByRequestId(int requestId);

	OutputMessage saveMessage(int requestId, SendMessage message, SocketUser user);

}
