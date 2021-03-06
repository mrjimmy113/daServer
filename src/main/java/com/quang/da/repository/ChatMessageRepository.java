package com.quang.da.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.quang.da.chat.MessageType;
import com.quang.da.entity.ChatMessage;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Integer>{
	
	
	@Query("SELECT c FROM ChatMessage as c WHERE c.request.requestId = :requestId ORDER BY c.time DESC")
	List<ChatMessage> findAllMessageOfRequest(@Param("requestId") int requestId, Pageable pageable);
	
	@Query("SELECT c FROM ChatMessage as c WHERE"
			+ " c.request.requestId = :requestId"
			+ " AND c.messageType = :messageType"
			+ " ORDER BY c.time DESC")
	List<ChatMessage> findLastMessageByType(
			Pageable pageable,
			@Param("requestId") int requestId,
			@Param("messageType") MessageType messageType);

}
