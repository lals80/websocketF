package com.example.websocketdemo.model;

import java.time.LocalDateTime;
import com.example.websocketdemo.domain.User;
import java.util.List;

public class ChatMessage {
	private Long senderId;
    private MessageType type;
    private String content;
    private String sender;
    private String roomName;
    private Long roomId;
    private LocalDateTime chatTime;
    private Long deleteUserId;
    private String deleteUserName;
    private List<User> leftUsers;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        KICK
    }
    // 열거형 : CHAT=0 JOIN=1 LEAVE=2

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	public LocalDateTime getChatTime() {
		return chatTime;
	}

	public void setChatTime(LocalDateTime chatTime) {
		this.chatTime = chatTime;
	}

	public Long getDeleteUserId() {
		return deleteUserId;
	}

	public void setDeleteUserId(Long deleteUserId) {
		this.deleteUserId = deleteUserId;
	}

	public String getDeleteUserName() {
		return deleteUserName;
	}

	public void setDeleteUserName(String deleteUserName) {
		this.deleteUserName = deleteUserName;
	}

	public List<User> getLeftUsers() {
		return leftUsers;
	}

	public void setLeftUsers(List<User> leftUsers) {
		this.leftUsers = leftUsers;
	}	
}