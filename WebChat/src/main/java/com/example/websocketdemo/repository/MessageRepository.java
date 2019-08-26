package com.example.websocketdemo.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.websocketdemo.entity.ChatroomEntity;
import com.example.websocketdemo.entity.MessageEntity;
import com.example.websocketdemo.entity.UserEntity;

@Repository
public interface MessageRepository	extends JpaRepository<MessageEntity, Long> {
	List<MessageEntity> findByChatroom(ChatroomEntity chatroom);
	MessageEntity findTopByChatroomOrderByIdDesc(ChatroomEntity chatroom);
	Integer countByChatroom(ChatroomEntity chatroom);
	// 안읽은것만 가져와야함.
	
}
