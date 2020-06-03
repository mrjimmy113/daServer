package com.quang.da.service;

import com.quang.da.chat.OutputMessage;
import com.quang.da.chat.SendMessage;

public interface ChatService {

	OutputMessage saveMessage(int requestId, SendMessage message, String email);

}
