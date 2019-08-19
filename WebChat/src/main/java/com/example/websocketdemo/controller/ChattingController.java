package com.example.websocketdemo.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.websocketdemo.domain.Chatroom;
import com.example.websocketdemo.domain.Message;
import com.example.websocketdemo.domain.User;
import com.example.websocketdemo.entity.ChatroomEntity;
import com.example.websocketdemo.entity.UserEntity;
import com.example.websocketdemo.service.ChatroomService;
import com.example.websocketdemo.service.UserService;
import com.example.websocketdemo.HttpSessionUtils;

@Controller
@RequestMapping("/chattings")
public class ChattingController {
	@Autowired UserService userService;
	@Autowired ChatroomService chatroomService; 	
	
	// 채팅방 리스프 화면 출력
	// 한 채팅방을 클릭했을 때는 해당 채팅방으로 이동한다.
	// 방으로 이동했을 경우 url에 id를 표시한다.
	@GetMapping("")
	public String chattingsPage(Model model, HttpSession session) { 
		// 로그인 되어 있는 경우
		if (HttpSessionUtils.isLoginUser(session)) {
			
			User user = HttpSessionUtils.getUserFromSession(session);
			model.addAttribute("user", user);
			
			for(Chatroom chatroom : user.getChatrooms()) {
				System.out.println(chatroom.getId());
				System.out.println(chatroom.getName());
				System.out.println(chatroom.getMessages());
				System.out.println(chatroom.getUsers());
			}
			return "chatlist";
	
		// 로그인 되어 있지 않은 경우
		} else {
			return "redirect:/login";
		}
	}
	
	// 채팅방 화면
	@GetMapping("/{id}")
	public String chatroomPage(@PathVariable(value = "id") Long roomId, Model model, HttpSession session) { 
		// 로그인 되어 있는 경우
		if (HttpSessionUtils.isLoginUser(session)) {
			List<Message> messages = chatroomService.getMessagesByRoomId(roomId);
			model.addAttribute("messages", messages);
			return "chatroom";
		// 로그인 되어 있지 않은 경우
		} else {
			return "redirect:/login";
		}
	}
	
	// 채팅방 등록 페이지 출력
	@GetMapping("/form")
	public String registerChattingsPage(HttpServletRequest request, Model model, HttpSession session, Chatroom formChatroom) {
		// 로그인 되어 있는 경우
		if (HttpSessionUtils.isLoginUser(session)) {
			User user = HttpSessionUtils.getUserFromSession(session);
			
			model.addAttribute("user", user);
			List<User> userlist = userService.getUsers();
			model.addAttribute("userlist", userlist);
			return "registerchatroom";
		// 로그인 되어 있지 않은 경우
		} else {
			return "redirect:/login";
		}
	}
	
	// 채팅방 등록 처리
	@PostMapping("")
	public String registerChattings(HttpServletRequest request, @Valid Chatroom formChatroom, Model model, HttpSession session, EntityManager entityManager) {
		// 로그인 되어 있는 경우
		if (HttpSessionUtils.isLoginUser(session)) {
			User user = HttpSessionUtils.getUserFromSession(session);
			// 현재 유저 정보는 session, 다른 유저정보랑 채팅방 이름 form에서 들고오고
			// 각각 repository에 적용시킨다. 
			String otherUserId = request.getParameter("otherUserId");
			User otherUser = new User();
			
			otherUser = userService.getUserByUserId(otherUserId);
			
			chatroomService.saveChatroom(formChatroom);
			ChatroomEntity savedChatroom = new ChatroomEntity();
			savedChatroom.buildEntity(chatroomService.getChatroomByName(formChatroom.getName()));
			
			UserEntity userentity = new UserEntity();
			userentity.buildEntity(user);
			savedChatroom.addUser(userentity);
			chatroomService.saveChatroom2(savedChatroom);
			
			UserEntity userentity2 = new UserEntity();
			userentity2.buildEntity(otherUser);
			savedChatroom.addUser(userentity2);
			chatroomService.saveChatroom2(savedChatroom);
			
			return "redirect:/chat";
		// 로그인 되어 있지 않은 경우
		} else {
			return "redirect:/login";
		}
	}
	/*
	// 유저 수정
	@PutMapping("")
	public String updateUserById(@Valid User formUser, Model model, HttpSession session) {
		User user = HttpSessionUtils.getUserFromSession(session);
		user.setUserPw(formUser.getUserPw());
		user.setName(formUser.getName());
		
		System.out.println(user.getId());
		System.out.println(user.getUserId());
		System.out.println(user.getUserPw());
		System.out.println(user.getName());
		
		userService.updateUser(user);		
		model.addAttribute("user", user);
		session.setAttribute("user", user);
		return "redirect:/";
	}
	
	// 유저 삭제
	@DeleteMapping("")
	public String deleteUserById(Model model, HttpSession session) {
		userService.deleteUser(HttpSessionUtils.getUserFromSession(session));
		
		session.invalidate();
		return "redirect:/";
	}
	
	// 로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	*/
}