package com.example.websocketdemo.controller;

import com.example.websocketdemo.HttpSessionUtils;
import com.example.websocketdemo.domain.Chatroom;
import com.example.websocketdemo.domain.Message;
import com.example.websocketdemo.domain.User;
import com.example.websocketdemo.model.AjaxForm;
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
    
    @MessageMapping("/chat.deleteUser/{roomId}")
    public void kickuser(@DestinationVariable Long roomId, @Payload ChatMessage chatMessage) {
        // 로그인 되어 있는 경우
    	
    	User deleteUser = userService.getUserById(chatMessage.getDeleteUserId());
    	/*
    	 * DB에 ~~유저가 퇴장당한 것을 저장하고, 화면을 새로고침했을 때도 표시하기 위해서는 필요한 작업
    	 * 일단 생략
    	Message message = new Message(
    			user,
    			chatMessage.getContent(),
    			chatroomService.getChatroomById(roomId));
    	
    	messageService.saveMessage(message);
    	*/
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
    	
    	
        
    	/*
        if (HttpSessionUtils.isLoginUser(session)) {
            Chatroom chatroom = chatroomService.getChatroomById(roomId);
            User user = userService.getUserById(form.getId());
            chatroomService.exitChatroom(chatroom, user);
            List<User> userlist = chatroomService.getChatroomById(roomId).getUsers();
            
            // 문제점1 : 추방당한 사람이 새로고침하는 것이 아니므로 채팅방 내부에 남아있음.(나중에 알아보기)
            // 문제점2 : 채팅방의 유저 목록에 추방당한 사람이 그대로 남아있음.
            
            // HTTP 원리 : 요청-응답(request-response) 구조
            // 지금 a유저가 b를 추방하기 위한 요청을 보냈다.
            // 서버에서는 요청을 보낸 a에 대해서만 새로고침 요청을 할 수 있음.
            

        // 로그인 되어 있지 않은 경우
        } else {
            return null;
        }
        */
    }
} 