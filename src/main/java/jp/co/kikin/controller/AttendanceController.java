package jp.co.kikin.controller;

import jp.co.kikin.service.AttendanceLogic;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jp.co.kikin.constant.RequestSessionNameConstant;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("kikin/attendance")
public class AttendanceController {
	private final AttendanceLogic attendanceLogic;

	AttendanceController(AttendanceLogic attendanceLogic) {
		this.attendanceLogic = attendanceLogic;
	}

	@PostMapping("/start")
	public String start(HttpSession session, RedirectAttributes redirectAttributes) throws Exception {
		// ログインID取得
		String employeeId = (String) session.getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_ID);
		// エラーメッセージ
		try {
			// 出勤時間打刻
			attendanceLogic.setStartTime(employeeId);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}

		// ホームに戻る
		return "redirect:/kikin/home";
	}

	@PostMapping("/end")
	public String end(HttpSession session, RedirectAttributes redirectAttributes,@RequestParam(name = "breakTime") String breakTime) throws Exception {
		// ログインID取得
		String employeeId = (String) session.getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_ID);
		// エラーメッセージ
		try {
			// 退勤時間打刻
			attendanceLogic.setEndTime(employeeId,breakTime);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
		}
		// ホームに戻る
		return "redirect:/kikin/home";

	}

}
