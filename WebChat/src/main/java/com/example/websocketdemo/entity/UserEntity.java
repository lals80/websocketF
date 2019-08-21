package com.example.websocketdemo.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.websocketdemo.domain.Chatroom;
import com.example.websocketdemo.domain.Message;
import com.example.websocketdemo.domain.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "user")
public class UserEntity {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	@OrderBy
	private Long id; // database에서 sequence number, primary key 역할
	
	@Column(nullable=false, length=20, unique=true) // null 하용 안함, 유일키
	private String userId;
	@Column(nullable=false)
	private String userPw;
	@Column(nullable=false)
	private String name;
	
	@OneToMany(mappedBy="user")
	private List<MessageEntity> messages = new ArrayList<MessageEntity>();
	
	@ManyToMany(mappedBy="users")
	private List<ChatroomEntity> chatrooms = new ArrayList<ChatroomEntity>();
	
	// 얼굴 사진 profile 필드, 생략
	// 친구 기능 friend List<User> 생략
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserPw() {
		return userPw;
	}
	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<MessageEntity> getMessages() {
		return messages;
	}
	public void setMessages(List<MessageEntity> messages) {
		this.messages = messages;
	}
	public List<ChatroomEntity> getChatrooms() {
		return chatrooms;
	}
	public void setChatrooms(List<ChatroomEntity> chatrooms) {
		this.chatrooms = chatrooms;
	}
    
	public User buildDomain() {
		User user = new User();
		user.setId(id);
		user.setUserId(userId);
		user.setUserPw(userPw);
		user.setName(name);
		
		for(MessageEntity message : messages) {
			user.getMessages().add(message.buildDomain());
		}		
		
		for(ChatroomEntity chatroom : chatrooms) {
			user.getChatrooms().add(chatroom.buildDomain2());
		}
		
		return user;
	}
	
	public User buildDomain2() {
		User user = new User();
		user.setId(id);
		user.setUserId(userId);
		user.setUserPw(userPw);
		user.setName(name);
		
		for(MessageEntity message : messages) {
			user.getMessages().add(message.buildDomain());
		}		
		
		for(ChatroomEntity chatroom : chatrooms) {
			user.getChatrooms().add(chatroom.buildDomain3());
		}
		
		return user;
	}
	
	public User buildDomain3() {
		User user = new User();
		user.setId(id);
		user.setUserId(userId);
		user.setUserPw(userPw);
		user.setName(name);
		
		return user;
	}
	
	public void buildEntity(User user) {
		id = user.getId();
		userId = user.getUserId();
		userPw = user.getUserPw();
		name = user.getName();
		
		MessageEntity messageEntity = new MessageEntity();
		// Message -> MessageEntity
		for(int i=0;i<user.getMessages().size();i++) {
			messageEntity.buildEntity(user.getMessages().get(i));
			messages.add(messageEntity);
		}
		
		ChatroomEntity chatroomEntity = new ChatroomEntity();
		// Message -> MessageEntity
		for(int i=0;i<user.getChatrooms().size();i++) {
			chatroomEntity.buildEntity(user.getChatrooms().get(i));
			chatrooms.add(chatroomEntity);
		}
	}
}