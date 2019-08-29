package com.example.websocketdemo.model;

import java.time.LocalDateTime;

public class ChatMessage {
	private Long senderId;
    private MessageType type;
    private String content;
    private String sender;
    private Long roomId;
    private LocalDateTime chatTime;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
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
	
}