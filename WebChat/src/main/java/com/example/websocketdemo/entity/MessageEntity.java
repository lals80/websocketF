package com.example.websocketdemo.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.example.websocketdemo.domain.Message;
import com.example.websocketdemo.model.ChatMessage;

@Entity
@Table(name = "message")
public class MessageEntity {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	@OrderBy
	private Long id; // database에서 sequence number, primary key 역할
	
	@ManyToOne
	@JoinColumn(name="fkUser")
	private UserEntity user;
	// 한 명의 유저는 여러 개의 메시지를 작성할 수 있다.
	// 한 개의 메시지는 하나의 유저를 갖는다.
	// 1:n
	// 양방향으로 하고싶으면 반대쪽(UserEntity)에서도 OneToMany 어노테이션 사용
	
	// class : 메시지 타입(사진, 오디오, 텍스트) 생략.
	
	@Column(nullable=false)
	private String contents;
	
	@ManyToOne
	@JoinColumn(name="fkChatroom")
	private ChatroomEntity chatroom;
	// 한 개의 채팅룸은 여러 개의 메시지를 가질 수 있다.
	// 한 개의 메시지는 하나의 채팅룸을 갖는다.
	// 1:n
	// 양방향으로 하고싶으면 반대쪽(UserEntity)에서도 OneToMany 어노테이션 사용
	// 양방향 권장
	
	private LocalDateTime chatTime;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
		this.user = user;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public ChatroomEntity getChatroom() {
		return chatroom;
	}
	public void setChatroom(ChatroomEntity chatroom) {
		this.chatroom = chatroom;
	}
	public LocalDateTime getChatTime() {
		return chatTime;
	}
	public void setChatTime(LocalDateTime chatTime) {
		this.chatTime = chatTime;
	}
	
	public Message buildDomain() {
		Message message = new Message();
		message.setId(id);
		message.setUser(user.buildDomain2());
		message.setContents(contents);
		message.setChatroom(chatroom.buildDomain2());
		message.setChatTime(chatTime);
		return message;
	}
	
	public Message buildDomain2() {
		Message message = new Message();
		message.setId(id);
		message.setUser(user.buildDomain3());
		message.setContents(contents);
		message.setChatroom(chatroom.buildDomain3());
		message.setChatTime(chatTime);
		return message;
	}
	
	public Message buildDomain3() {
		Message message = new Message();
		message.setId(id);
		message.setContents(contents);
		message.setChatTime(chatTime);
		return message;
	}
	
	public void buildEntity(Message message) {
		id=message.getId();
		UserEntity userEntity = new UserEntity();
		userEntity.buildEntity2(message.getUser());
		user = userEntity;
		contents=message.getContents();
		ChatroomEntity chatroomEntity = new ChatroomEntity();
		chatroomEntity.buildEntity2(message.getChatroom());
		chatroom = chatroomEntity;
		chatTime = message.getChatTime();
	}
	
	public void buildEntity2(Message message) {
		id=message.getId();
		UserEntity userEntity = new UserEntity();
		userEntity.buildEntity3(message.getUser());
		user = userEntity;
		contents=message.getContents();
		ChatroomEntity chatroomEntity = new ChatroomEntity();
		chatroomEntity.buildEntity3(message.getChatroom());
		chatroom = chatroomEntity;
		chatTime = message.getChatTime();
	}
	
	public void buildEntity3(Message message) {
		id=message.getId();
		contents=message.getContents();
		chatTime = message.getChatTime();
	}
}