package jp.co.kikin.controller;

import java.util.List;
import jp.co.kikin.service.UserShiftLogic;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jp.co.kikin.constant.CommonConstant;
import jp.co.kikin.constant.RequestSessionNameConstant;
import jp.co.kikin.dto.DailyShiftDto;
import jp.co.kikin.dto.LoginDto;
import jp.co.kikin.service.CommonUtils;
import jp.co.kikin.service.DailyShiftLogic;

@Controller
@RequestMapping("/kikin")
public class HomeController {

	private final UserShiftLogic userShiftLogic;

	HomeController(UserShiftLogic userShiftLogic) {
		this.userShiftLogic = userShiftLogic;
	}

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

		// 今日のシフトセット 長谷川
		// 今日の日付取得
		String yearMonthDay = CommonUtils.getFisicalDay();
		// ログインID取得
		String employeeId = (String) session.getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_ID);
		UserShiftLogic logic = new UserShiftLogic();
		// 一人分の今日のシフト取得
		DailyShiftDto dto = logic.getTodayShift(yearMonthDay, employeeId);
		model.addAttribute("todayShift", dto);
		if (dto == null || dto.getStartTime() == null || dto.getStartTime().isEmpty()) {
			model.addAttribute("todayShiftMessage", "本日は勤務がありません");
			model.addAttribute("todayShift", null);
		}

		String employeeName = (String) session.getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_NAME);
		model.addAttribute("employeeID", employeeId);
		model.addAttribute("employeeName", employeeName);

		return "home";
	}

}
