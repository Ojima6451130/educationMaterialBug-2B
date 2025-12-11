package jp.co.kikin.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jp.co.kikin.CheckUtils;
import jp.co.kikin.CommonConstant.DayOfWeek;
import jp.co.kikin.constant.CommonConstant;
import jp.co.kikin.constant.RequestSessionNameConstant;
import jp.co.kikin.dao.SalaryDao;
import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.dto.MonthlyShiftDto;
import jp.co.kikin.dto.SalaryRecordeDto;
import jp.co.kikin.exception.CommonException;
import jp.co.kikin.model.DateBean;
import jp.co.kikin.model.MonthlyShiftCheckBean;
import jp.co.kikin.model.MonthlyShiftCheckForm;
import jp.co.kikin.pentity.MonthShift;
import jp.co.kikin.pentity.PostEntity;
import jp.co.kikin.pentity.SalaryInfoEntity;
import jp.co.kikin.pentity.SalaryRecodeEntity;
import jp.co.kikin.prepository.MonthShiftRepository;
import jp.co.kikin.prepository.PostRepository;
import jp.co.kikin.prepository.SalaryRecodeRepository;
import jp.co.kikin.service.ComboListUtilLogic;
import jp.co.kikin.service.CommonUtils;

@Controller
@RequestMapping(value = "/kikin")
public class SalaryRecodeController {
	/** 画面URL */
	public static final String PATH = "/kikin";

	@Autowired
	SalaryRecodeRepository repositorySalary;

	@Autowired
	PostRepository repositoryPost;

	final int MINWORKTIME = 160;

	/** サービス機能名={@value} */
	public static final String CONTENTS = "給料明細";

	// メニュー画面でボタン押下後
	@GetMapping("/salaryRecodeInput")
	public String input(Model model) throws SQLException, CommonException {

		// 役職情報の取得
		List<PostEntity> postList = repositoryPost.findAll();
		model.addAttribute("postList", postList);

		// 表示年月の取得
		ComboListUtilLogic comboListUtils = new ComboListUtilLogic();
		Map<String, String> yearMonthCmbMap = new LinkedHashMap<>();
		yearMonthCmbMap = comboListUtils.getComboYearMonth(CommonUtils.getFisicalDay(CommonConstant.YEARMONTH_NOSL), 22,
				ComboListUtilLogic.KBN_YEARMONTH_PRE, false);

		model.addAttribute("yearMonthCmbMap", yearMonthCmbMap);

		return "salaryRecodeInput";
	}

	// 表示
	@PostMapping("/salaryRecodeOutput")
	public String init(HttpServletRequest request, HttpSession session, Model model,
			@RequestParam("yearMonth") String yearMonth, @RequestParam("postID") String postID) throws Exception {
		return view(request, model, yearMonth, postID);
	}

	// 表示するデータ
	private String view(HttpServletRequest request, Model model, String yearMonth, String postID) throws Exception {
		// セッション
		HttpSession session = request.getSession();
		// ログインユーザ情報をセッションより取得
		LoginUserDto loginUserDto = (LoginUserDto) session
				.getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);

		// t_monthly_salaryからデータを取得
		List<SalaryRecodeEntity> salaryData = repositorySalary.findAll();

		// m_salary_Infoから役職と一致するデータを取得
		SalaryDao salaryDao = new SalaryDao();
		SalaryInfoEntity salaryInfo = salaryDao.getEntity(postID);

		// 表示用データをまとめる
		SalaryRecordeDto salaryDto = new SalaryRecordeDto();
		for (SalaryRecodeEntity data : salaryData) {
			if (data.getEmployeeId().equals(loginUserDto.getEmployeeId()) && data.getYearMonth().equals(yearMonth)) {
				salaryDto.setSalaryRecodeEntity(data);
				salaryDto.setOtherPrice(salaryInfo.getOther_price());
				break;
			}
		}
		if(salaryDto.getSalaryRecodeEntity() == null) {
			return "salaryRecodeError";
		}
		
		// 給料計算
		int subSalary = 0;
		int addSalary = 0;
		int totalTime = salaryDto.getSalaryRecodeEntity().getTotalworkhours();
		int overTime = salaryDto.getSalaryRecodeEntity().getOvertimehours();
		int nightTime = salaryDto.getSalaryRecodeEntity().getNighthours();
		int bacePrice = salaryInfo.getBasic_salary();

		// 160時間超えてるか
		if (totalTime < MINWORKTIME) {
			// 足りない時間
			int missingHours = MINWORKTIME - totalTime;
			// マイナスする給料
			subSalary += (bacePrice / MINWORKTIME) * missingHours;
		} else {
			addSalary += overTime * salaryInfo.getOther_price();
		}
		// 夜勤分
		if (nightTime > 0) {
			addSalary += nightTime * salaryInfo.getNight_price();
		}
		// 合計金額
		int Salary = bacePrice + addSalary - subSalary + salaryInfo.getOther_price();
		
		String Year = yearMonth.substring(0,4);
		String month = yearMonth.substring(4);
		String reYearMonth = Year + "年" + month+ "月";

		model.addAttribute("salaryDto", salaryDto);
		model.addAttribute("Salary", Salary);
		model.addAttribute("bacePrice", bacePrice);
		model.addAttribute("yearMonth", reYearMonth);
		model.addAttribute("loginUserDto", loginUserDto);

		return "salaryRecodeOutput";
	}

}
