package com.example.websocketdemo.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.websocketdemo.domain.Message;

public interface MessageService {
	
	List<Message> getMessagesByRoomId(Long roomId);
	Message getLastMessageByRoomId(Long roomId);
	void saveMessage(Message message); // 생성
	void updateMessage(Message message); // 수정
	void deleteMessage(Message message); // 삭제
}
