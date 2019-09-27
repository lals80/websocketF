package com.example.websocketdemo.controller;

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
import com.example.websocketdemo.domain.User;
import com.example.websocketdemo.service.UserService;
import com.example.websocketdemo.HttpSessionUtils;

// User 조작에 관련된 클래스 
// 회원정보보기(GET-user) 회원등록(POST-user), 회원 정보 수정(PUT-user), 회원 탈퇴(DELETE-user)를 다룬다.
// 하나의 아이디로 로그인했을 때 해당 유저의 정보만을 출력해야 하므로(list 출력할 필요 없음),
// 일반적인 RESTful의 id(ex:GET-/users, GET-/users/{id})와 같은 정보를 url로 전송할 필요가 없음.
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired UserService userService;  	
	
	// 유저 페이지 출력
	@GetMapping("")
	public String userPage(Model model, HttpSession session, User formUser) {
		// 로그인 되어있는 경우
		if (HttpSessionUtils.isLoginUser(session)) {
			User user = HttpSessionUtils.getUserFromSession(session);
			model.addAttribute("user", user);
			return "user";
		// 로그인이 되어있지 않은 경우
		} else {
			// 로그인이 되어있지 않을 때
			return "redirect:/login";
		}
	}
	
	// 유저 등록 페이지 출력
	// 로그인 되어 있는 경우는 자신의 정보를 출력하는 페이지 출력
	// 로그인 되어 있지 않은 경우는 register 페이지 출력
	@GetMapping("/form")
	public String registerUserPage(Model model, HttpSession session, User formUser) {
		// 로그인 되어있는 경우
		if (HttpSessionUtils.isLoginUser(session)) {
			User user = HttpSessionUtils.getUserFromSession(session);
			model.addAttribute("user", user);
			return "redirect:/user";
		// 로그인이 되어있지 않은 경우
		} else {
			return "register";
		}
	}
	
	// 유저 등록 처리
	// register 페이지에서 POST로 전송한다.
	// 로그인 되어 있는 경우는 비정상 접근이므로 error 페이지 출력
	// 로그인 되어 있지 않은 경우는 form 정보를 확인하고 이상있으면 register 페이지 출력
	// 이상 없으면 register 처리 후 로그인 페이지 출력
	@PostMapping("")
	public String registerUser(Model model, User formUser, HttpSession session) {
		// 로그인 되어있는 경우
		if (HttpSessionUtils.isLoginUser(session)) {
			return "error";
		// 로그인이 되어있지 않을 때
		} else {
			// form에 입력된 정보가 비어있을 경우
			if(formUser.getUserId().equals("") || formUser.getUserPw().equals("") || formUser.getName().equals("")) {
				return "/user/form";
			// form에 입력된 정보가 있었다면
			} else {				
				User user = userService.getUserByUserId(formUser.getUserId());
				// 등록되지 않은 ID일때만 등록 처리 후 login 페이지 이동
				if(user == null) {
					userService.saveUser(formUser);
					return "redirect:/login";
				} 
				
				// 이미 같은 ID가 등록되어 있는 경우
				return "error";
			}
		}
	}
	
	// 유저 수정 페이지 출력
	// 로그인 되어 있는 경우는 자신의 정보를 수정하는 페이지 출력
	// 로그인 되어 있지 않은 경우는 login 페이지 출력
	@GetMapping("/edit")
	public String editPage(Model model, HttpSession session, User formUser) {
		// 로그인 되어있는 경우
		if (HttpSessionUtils.isLoginUser(session)) {
			User user = HttpSessionUtils.getUserFromSession(session);
			model.addAttribute("user", user);
			return "edit";
		// 로그인이 되어있지 않은 경우
		} else {
			return "redirect:/login";
		}
	}
	
	// 유저 수정 처리
	@PutMapping("")
	public String updateUser(@Valid User formUser, Model model, HttpSession session) {
		System.out.println("asdfasdf");
		User user = HttpSessionUtils.getUserFromSession(session);
		user.setUserPw(formUser.getUserPw());
		user.setName(formUser.getName());
		userService.updateUser(user);
		session.setAttribute("user", user);
		model.addAttribute("user", user);
		return "redirect:/user";
	}
	
	// 유저 삭제
	@DeleteMapping("")
	public String deleteUser(Model model, HttpSession session) {
		userService.deleteUser(HttpSessionUtils.getUserFromSession(session));
		return "redirect:/logout.do";
	}
}