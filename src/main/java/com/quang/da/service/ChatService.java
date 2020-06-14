package com.quang.da.service;

import java.util.List;

import com.quang.da.chat.OutputMessage;
import com.quang.da.chat.SendMessage;
import com.quang.da.chat.SocketUser;

public interface ChatService {

	OutputMessage saveMessage(int requestId, SendMessage message, SocketUser user);

	List<OutputMessage> getChatMessageByRequestId(int requestId, int page);

}
