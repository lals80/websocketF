package com.example.websocketdemo.controller;

import com.example.websocketdemo.HttpSessionUtils;
import com.example.websocketdemo.domain.Chatroom;
import com.example.websocketdemo.domain.Message;
import com.example.websocketdemo.domain.User;
import com.example.websocketdemo.model.ChatMessage;
import com.example.websocketdemo.service.ChatroomService;
import com.example.websocketdemo.service.MessageService;
import com.example.websocketdemo.service.UserService;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

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
    
    @MessageMapping("/chat.deleteUser/{roomId}")
    public void kickuser(@DestinationVariable Long roomId, @Payload ChatMessage chatMessage, HttpSession session) {
        // 로그인 되어 있는 경우
    	if(chatroomService.getChatroomById(roomId).getUsers().get(0).equals(
    			HttpSessionUtils.getUserFromSession(session))) {
	    	User deleteUser = userService.getUserById(chatMessage.getDeleteUserId());
	    	
	    	// 퇴장당했을 때 퇴장 메시지는 나옴. 해당 유저도 자동으로 나가게 됨. 하지만 DB에 해당 내용이 저장되지 않으므로,
	    	// 다시 채팅방에 들어왔을 때는 퇴장 메시지가 보이지 않음.
	    	
	    	Chatroom chatroom = chatroomService.getChatroomById(roomId);
	    	
	    	chatMessage.setRoomName(chatroom.getName());
	    	chatMessage.setDeleteUserName(deleteUser.getName());
	    	
	    	chatroomService.exitChatroom(chatroom, deleteUser);
	    	
	    	List<User> userlist = chatroomService.getChatroomById(roomId).getUsers();
	    	for(User user : userlist) {
	    		user.setChatrooms(null);
	    		user.setMessages(null);
	    		user.setUserId(null);
	    		user.setUserPw(null);
	    	}
	    	chatMessage.setLeftUsers(userlist);
	    	template.convertAndSend("/topic/" + roomId, chatMessage);
    	}
    }
} 