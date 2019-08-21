package com.example.websocketdemo.entity;

import org.springframework.stereotype.Component;

import com.example.websocketdemo.domain.Chatroom;
import com.example.websocketdemo.domain.Message;
import com.example.websocketdemo.domain.User;

@Component
public class DependencyClass {
	private ChatroomEntity chatroomEntity;
	private UserEntity userEntity;
	private MessageEntity messageEntity;
	
	public Chatroom chatroomBuildDomain(ChatroomEntity chatroomEntity) {
		this.chatroomEntity = chatroomEntity;
		Chatroom chatroom = new Chatroom();
		chatroom.setId(chatroomEntity.getId());
		chatroom.setName(chatroomEntity.getName());
		for(MessageEntity message : chatroomEntity.getMessages()) {
			this.messageEntity=message;
			// List 인터페이스의 size()메서드는 List 내부의 요소들의 갯수를 의미한다.
			chatroom.getMessages().add(messageBuildDomain(message));
			// List 인터페이스의 add 메서드는 List에 요소를 추가한다.
			// List 인터페이스의 get 메서드는 List에서 i번째 요소를 return 한다.
		}
		
		for(UserEntity user : chatroomEntity.getUsers()) {
			this.userEntity=user;
			// List 인터페이스의 size()메서드는 List 내부의 요소들의 갯수를 의미한다.
			chatroom.getUsers().add(userBuildDomain(user));
			// List 인터페이스의 add 메서드는 List에 요소를 추가한다.
			// List 인터페이스의 get 메서드는 List에서 i번째 요소를 return 한다.
		}
		
		return chatroom;
	}
	
	public User userBuildDomain(UserEntity userEntity) {
		this.userEntity = userEntity;
		
		User user = new User();
		user.setId(userEntity.getId());
		user.setUserId(userEntity.getUserId());
		user.setUserPw(userEntity.getUserPw());
		user.setName(userEntity.getName());
		
		for(MessageEntity message : userEntity.getMessages()) {
			this.messageEntity=message;
			user.getMessages().add(messageBuildDomain(message));
		}
		
		for(ChatroomEntity chatroom : userEntity.getChatrooms()) {
			this.chatroomEntity=chatroom;
			user.getChatrooms().add(chatroomBuildDomain(chatroom));
		}
		
		return user;
	}
	
	public Message messageBuildDomain(MessageEntity messageEntity) {
		this.messageEntity = messageEntity;
		this.userEntity = messageEntity.getUser();
		this.chatroomEntity = messageEntity.getChatroom();
		
		Message message = new Message();
		message.setId(messageEntity.getId());
		message.setUser(userBuildDomain(messageEntity.getUser()));
		message.setContents(messageEntity.getContents());
		message.setChatroom(chatroomBuildDomain(messageEntity.getChatroom()));
		message.setChatTime(messageEntity.getChatTime());
		return message;
	}
}