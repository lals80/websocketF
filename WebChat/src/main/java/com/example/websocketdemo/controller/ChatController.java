package com.example.websocketdemo.controller;

import com.example.websocketdemo.domain.Message;
import com.example.websocketdemo.domain.User;
import com.example.websocketdemo.model.ChatMessage;
import com.example.websocketdemo.service.ChatroomService;
import com.example.websocketdemo.service.MessageService;
import com.example.websocketdemo.service.UserService;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
	private final SimpMessagingTemplate template;
	@Autowired MessageService messageService;
	@Autowired ChatroomService chatroomService;
	@Autowired UserService userService;

    @Autowired
    public ChatController(SimpMessagingTemplate template) {
        this.template = template;
    }
    
    @MessageMapping("/chat.sendMessage/{roomId}") // 방 번호 변수로 받기
    public void sendMessage(@DestinationVariable Long roomId, @Payload ChatMessage chatMessage) {
    	// DB Message 테이블에 insert 해야함.
    	// MessageMapping 동적 경로 설정, send 방법
    	
    	User user = userService.getUserById(chatMessage.getSenderId()); 
    	Message message = new Message(
    			user,
    			chatMessage.getContent(),
    			chatroomService.getChatroomById(roomId));
    	
    	messageService.saveMessage(message);
    	chatMessage.setRoomName(chatroomService.getChatroomById(roomId).getName());
    	
    	template.convertAndSend("/topic/" + roomId, chatMessage);
    }
    /*
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
    */
} 