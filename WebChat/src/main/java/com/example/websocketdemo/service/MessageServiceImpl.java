package com.example.websocketdemo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.websocketdemo.domain.Message;
import com.example.websocketdemo.entity.ChatroomEntity;
import com.example.websocketdemo.entity.MessageEntity;
import com.example.websocketdemo.exception.ResourceNotFoundException;
import com.example.websocketdemo.repository.ChatroomRepository;
import com.example.websocketdemo.repository.MessageRepository;
import com.example.websocketdemo.repository.MessageRepository;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

	@Autowired MessageRepository messageRepository;
	@Autowired ChatroomRepository chatroomRepository;
	
	@Override
	public List<Message> getMessagesByRoomId(Long roomId) {
		List<Message> messages = new ArrayList<Message>();
		ChatroomEntity chatroomEntity = chatroomRepository.findById(roomId);
		List<MessageEntity> messageEntities = messageRepository.findByChatroom(chatroomEntity);
		
		for(MessageEntity entity : messageEntities) {
			Message message = entity.buildDomain();
			messages.add(message);
		}
		return messages;
	}

	@Override
	public Message getLastMessageByRoomId(Long roomId) {
		Message message = new Message();
		ChatroomEntity chatroomEntity = chatroomRepository.findById(roomId);
		MessageEntity messageEntity = messageRepository.findTopByChatroomOrderByIdDesc(chatroomEntity);
		message = messageEntity.buildDomain();
		return message;
	}
	
	@Override
	public void saveMessage(Message message) {
		// TODO Auto-generated method stub
		MessageEntity entity = new MessageEntity();
		entity.buildEntity(message);
		
		messageRepository.save(entity);
	}
	@Override
	public void updateMessage(Message message) {
		MessageEntity entity = new MessageEntity();
		entity.buildEntity(message);
		
		messageRepository.save(entity);
	}

	@Override
	public void deleteMessage(Message message) {
		MessageEntity entity = new MessageEntity();
		entity.buildEntity(message);
		
		messageRepository.delete(entity);
	}
}
