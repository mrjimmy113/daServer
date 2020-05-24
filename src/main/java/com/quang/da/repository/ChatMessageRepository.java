package com.quang.da.repository;

import org.springframework.data.repository.CrudRepository;

import com.quang.da.entity.ChatMessage;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Integer>{

}
