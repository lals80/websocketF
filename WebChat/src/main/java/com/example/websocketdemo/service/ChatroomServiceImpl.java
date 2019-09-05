package com.example.websocketdemo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.websocketdemo.domain.Chatroom;
import com.example.websocketdemo.domain.Message;
import com.example.websocketdemo.domain.User;
import com.example.websocketdemo.entity.ChatroomEntity;
import com.example.websocketdemo.entity.MessageEntity;
import com.example.websocketdemo.entity.UserEntity;
import com.example.websocketdemo.exception.ResourceNotFoundException;
import com.example.websocketdemo.repository.ChatroomRepository;
import com.example.websocketdemo.repository.UserRepository;

@Service("ChatroomService")
public class ChatroomServiceImpl implements ChatroomService {

	@Autowired ChatroomRepository repository;

	@Override
	public List<Message> getMessagesByRoomId(Long roomId) {
		// TODO Auto-generated method stub
		List<Message> messages = new ArrayList<Message>();
		
		ChatroomEntity chatroomEntity = repository.findById(roomId);
		List<MessageEntity> messageEntities = chatroomEntity.getMessages(); 
		
		System.out.println(messageEntities);
		for(MessageEntity message : messageEntities) {
			messages.add(message.buildDomain());
		}
		
		return messages;
	}

	@Override
	public void saveChatroom(Chatroom chatroom) {
		// TODO Auto-generated method stub
		ChatroomEntity entity = new ChatroomEntity();
		entity.buildEntity(chatroom);
		
		repository.save(entity);
	}

	@Override
	public void updateChatroom(Chatroom chatroom) {
		// TODO Auto-generated method stub
		ChatroomEntity entity = new ChatroomEntity();
		entity.buildEntity(chatroom);
		
		repository.save(entity);
	}

	// 채팅방 인원 중 한명이 나갔을 때.
    @Override
    public void exitChatroom(Chatroom chatroom, User user) {
        // TODO Auto-generated method stub
    	System.out.println(chatroom.getUsers());
        System.out.println(user);
        chatroom.getUsers().remove(user);
        System.out.println(chatroom.getUsers());
        ChatroomEntity entity = new ChatroomEntity();
        entity.buildEntity(chatroom);
        System.out.println(entity.getUsers());
        repository.save(entity);
    }
	
	// 채팅방의 모든 인원이 나갔을 때.
	@Override
	public void deleteChatroom(Chatroom chatroom) {
		// TODO Auto-generated method stub
		ChatroomEntity entity = new ChatroomEntity();
		entity.buildEntity(chatroom);
		
		repository.delete(entity);
	}

	@Override
	public Chatroom getChatroomByName(String name) {
		// TODO Auto-generated method stub
		return repository.findByName(name).buildDomain();
	}
	
	@Override
	public Chatroom getChatroomById(Long roomId) {
		// TODO Auto-generated method stub
		return repository.findById(roomId).buildDomain();
	}

	@Override
	public void saveChatroom2(ChatroomEntity chatroomEntity) {
		repository.saveAndFlush(chatroomEntity);
	}
}