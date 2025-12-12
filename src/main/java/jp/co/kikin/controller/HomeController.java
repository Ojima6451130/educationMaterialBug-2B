package jp.co.kikin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jp.co.kikin.constant.CommonConstant;
import jp.co.kikin.constant.RequestSessionNameConstant;

@Controller
@RequestMapping("/kikin")
public class HomeController {

	@RequestMapping(value = "/home")
	public String init(HttpSession session, HttpServletRequest request, Model model) throws Exception {

		return view("init", session, request, model);
	}

	private String view(String processType, HttpSession session, HttpServletRequest req, Model model) throws Exception {

		// ログインユーザー情報セット
		model.addAttribute("authority_id",
				session.getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_AUTHORITY_ID));

		// 定数
		model.addAttribute("userAuthority", CommonConstant.Authority.USER.getId());
		model.addAttribute("adminAuthority", CommonConstant.Authority.ADMIN.getId());

		return "home";
	}

}
