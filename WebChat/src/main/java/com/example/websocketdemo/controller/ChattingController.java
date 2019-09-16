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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.websocketdemo.domain.Chatroom;
import com.example.websocketdemo.domain.Message;
import com.example.websocketdemo.domain.User;
import com.example.websocketdemo.entity.ChatroomEntity;
import com.example.websocketdemo.entity.UserEntity;
import com.example.websocketdemo.model.AjaxForm;
import com.example.websocketdemo.model.SearchMessage;
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
			user = userService.getUserById(user.getId());
			session.setAttribute("user", user);
			
			model.addAttribute("user", user);
			model.addAttribute("chatrooms", user.getChatrooms());
			
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
			Chatroom chatroom = chatroomService.getChatroomById(roomId);
			model.addAttribute("users", chatroom.getUsers());
			model.addAttribute("user", HttpSessionUtils.getUserFromSession(session));
			model.addAttribute("chatroom", chatroom);
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
			userlist.remove(user);
			model.addAttribute("userlist", userlist);
			return "registerchatroom";
		// 로그인 되어 있지 않은 경우
		} else {
			return "redirect:/login";
		}
	}
	
	@GetMapping("/search")
	@ResponseBody
	public List<User> search(SearchMessage search, HttpSession session, Model model) {
		// 로그인 되어 있는 경우
		if (HttpSessionUtils.isLoginUser(session)) {
			User user = HttpSessionUtils.getUserFromSession(session);
			
			List<User> userlist = userService.getUsersByName(search.getName());
			userlist.remove(user);
			return userlist;
			
		// 로그인 되어 있지 않은 경우
		} else {
			return null;
		}
	}
	
	@GetMapping("{id}/kick")
    @ResponseBody
    public List<User> kickuser(@PathVariable(value = "id") Long roomId, AjaxForm form, HttpSession session, Model model) {
        // 로그인 되어 있는 경우
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

            return userlist;

        // 로그인 되어 있지 않은 경우
        } else {
            return null;
        }
    }
	
	// 채팅방 등록 처리
	@PostMapping("")
	public String registerChattings(HttpServletRequest request, @Valid Chatroom formChatroom, Model model, HttpSession session) {
		System.out.println("등록처리 시작");
		// 로그인 되어 있는 경우
		if (HttpSessionUtils.isLoginUser(session)) {
			User user = HttpSessionUtils.getUserFromSession(session);
			// 현재 유저 정보는 session, 다른 유저정보랑 채팅방 이름 form에서 들고오고
			// 각각 repository에 적용시킨다.
			
			chatroomService.saveChatroom(formChatroom);
			ChatroomEntity savedChatroom = new ChatroomEntity();
			savedChatroom.buildEntity(chatroomService.getChatroomByName(formChatroom.getName()));
			
			String[] otherUserIds = request.getParameterValues("otherUserId");
			
			UserEntity userentity = new UserEntity();
			userentity.buildEntity(user);
			savedChatroom.addUser(userentity);
			
			if(otherUserIds !=null) for (String otherUserId : otherUserIds) {
				User otherUser = new User();
				System.out.println(otherUserId);
				
				otherUser = userService.getUserById(Long.valueOf(otherUserId));
				
				UserEntity otherUserEntity = new UserEntity();
				otherUserEntity.buildEntity(otherUser);
				savedChatroom.addUser(otherUserEntity);
			}
			chatroomService.saveChatroom2(savedChatroom);
			return "redirect:/chattings";
		// 로그인 되어 있지 않은 경우
		} else {
			return "redirect:/login";
		}
	}
	
	@DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable(value = "id") Long roomId, Model model, HttpSession session) {
        Chatroom chatroom = chatroomService.getChatroomById(roomId);
        User user = HttpSessionUtils.getUserFromSession(session);
        chatroomService.exitChatroom(chatroom, user);

        return "redirect:/chattings";
    }
	
	/*
	// 채팅방 수정(이름변경 or 강퇴)
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
	
	
	*/
}