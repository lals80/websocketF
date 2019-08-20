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

// HomeController 클래스
// 기본(GET-/) 매핑과 기타 매핑(한 자원에 대한 GET/POST/PUT/DELETE를 제외한 매핑)을 처리한다.
// 로그인 페이지 이동(GET-/login), 로그인(GET-/login.do), 로그아웃(GET-/logout.do)을 다룬다.
@Controller
public class HomeController {
	// login처리시 userRepository 정보와 비교할 필요가 있음
	@Autowired UserService userService;  	
	
	// url에서 어떠한 변수 없이 해당 ip(url)로 접근했을 경우
	// session에 user정보가 저장되어 있을 경우(로그인 되어 있을 경우) user정보를 model 전송 후 user 페이지를 출력한다.
	// session에 user정보가 저장되지 않은 경우(로그아웃 되어 있을 경우) login 페이지를 출력한다.
	@GetMapping("")
	public String generalAccessSite(Model model, HttpSession session, User formUser) {
		// 로그인 되어 있을 경우
		if (HttpSessionUtils.isLoginUser(session)) {
			User user = HttpSessionUtils.getUserFromSession(session);
			model.addAttribute("user", user);
			return "redirect:/chattings";
		// 로그인이 되어있지 않을 때
		} else {	
			return "login";
		}
	}
	
	// url에서 /login으로 접근했을 경우
	// session에 user정보가 저장되어 있을 경우(로그인 되어 있을 경우) user정보를 model 전송 후 user 페이지를 출력한다.
	// session에 user정보가 저장되지 않은 경우(로그아웃 되어 있을 경우) login 페이지를 출력한다.
	@GetMapping("/login")
	public String loginPage(Model model, HttpSession session, User formUser) {
		// 로그인 되어 있을 경우
		if (HttpSessionUtils.isLoginUser(session)) {
			User user = HttpSessionUtils.getUserFromSession(session);
			model.addAttribute("user", user);
			return "redirect:/chattings";
		// 로그인이 되어있지 않을 때
		} else {	
			return "login";
		}
	}
	
	// login 처리
	// login 페이지에서 login.do로 이동하도록 한다.
	// form에 입력된 정보를 DB와 비교하여 로그인 작업을 처리한다.
	@GetMapping("/login.do")
	public String login(Model model, HttpSession session, User formUser) {
		// 로그인 되어 있을 경우
		if (HttpSessionUtils.isLoginUser(session)) {
			User user = HttpSessionUtils.getUserFromSession(session);
			model.addAttribute("user", user);
			return "redirect:/chattings";
		} 
		
		// form에 입력된 정보도 없었다면
		if(formUser.getUserId() == null) {
			return "redirect:/login";
		
		// form에 입력된 정보가 있었다면
		} else {
			User user = userService.getUserByUserId(formUser.getUserId());
			 
			// 입력한 id로 찾을 수 있는 user 정보가 없을 때는 id error
			// 입력한 id로 user정보를 찾은 경우 입력한 password와 DB의 password 비교, 불일치시 pw error
			// error가 없을 시 session에 user 정보 저장 후 /user url로 이동
			if(user == null) {
				System.out.println("id error : ");
				return "redirect:/login";
			} else if(!user.getUserPw().equals(formUser.getUserPw())) {
				System.out.println("pw error : ");
				return "redirect:/login";
			} else {
				session.setAttribute("user", user);
				return "redirect:/chattings";
			}
		}
	}
	
	// logout 처리(session 의 user 정보 제거)
	// logout 메뉴를 통해 logout.do로 이동하도록 한다.
	// 회원 탈퇴시 DB작업 후 logout.do로 이동하도록 한다. 
	@GetMapping("/logout.do")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
}