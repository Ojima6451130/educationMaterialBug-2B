/**
 * ファイル名：MonthlyShiftCheckController.java
 *
 * 変更履歴
 * 1.0  2010/07/19 Kazuya.Naraki
 * Spring 2025/04/11 Hironori.itaki
 */
package jp.co.kikin.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jp.co.kikin.CheckUtils;
import jp.co.kikin.CommonConstant.DayOfWeek;
import jp.co.kikin.constant.CommonConstant;
import jp.co.kikin.constant.RequestSessionNameConstant;
import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.dto.MonthlyShiftDto;
import jp.co.kikin.model.DateBean;
import jp.co.kikin.model.MonthlyShiftCheckBean;
import jp.co.kikin.model.MonthlyShiftCheckForm;
import jp.co.kikin.pentity.MonthShift;
import jp.co.kikin.prepository.MonthShiftRepository;
import jp.co.kikin.service.ComboListUtilLogic;
import jp.co.kikin.service.CommonUtils;
import jp.co.kikin.service.MethodComparator;
import jp.co.kikin.service.MonthlyShiftLogic;

/**
 * 説明：月別シフト確認画面
 *
 * @author
 *
 */
@Controller
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@RequestMapping(value = "/kikin")
public class MonthlyShiftCheckController {

	@Autowired
	CommonUtils commonUtils;

	@Autowired
	MonthlyShiftLogic monthlyShiftLogic;

	@Autowired
	CommonConstant commonConstant;

	@Autowired
	ComboListUtilLogic comboListUtilLogic;

	@Autowired
	MonthShiftRepository mr;

	/** 画面URL */
	public static final String SCREEN_PATH = "/monthlyShiftCheck";
	/** 「検索」押下時 */
	public static final String SCREEN_PATH_SEARCH = "/monthlyShiftCheck/search";
	/** 「印刷」押下時 */
	public static final String SCREEN_PATH_PRINT = "/monthlyShiftCheck/print";

	/** 「ページ」押下時 */
	public static final String SCREEN_PATH_DOPAGE = "/monthlyShiftCheck/dopage";

	public static final String PATH = "/kikin";

	/** サービス機能名={@value} */
	public static final String CONTENTS = "月別シフト確認画面";

	// 1ページあたりの表示件数(細井追加)
	private final int ROW = 16;

	/**
	 * Viewに共通URLを渡す.
	 *
	 * @return 保守画面共通URL
	 */
	@ModelAttribute("path")
	public String getPath() {
		return PATH;
	}

	// ページング（細井）
	public Page<MonthShift> page(Pageable pageable) {
		Page<MonthShift> pageList = mr.findAll(pageable);
		return pageList;
	}

	/**
	 * 画面初期表示時に動作する処理.
	 *
	 * @param request リクエスト情報
	 * @param model   リクエストスコープ上にオブジェクトを載せるためのmap
	 * @return view名称
	 * @throws Exception
	 */
	@RequestMapping(value = SCREEN_PATH)
	public String init(HttpServletRequest request, HttpSession session, Model model, MonthlyShiftCheckForm form,
			BindingResult bindingResult, @PageableDefault(page = 0, size = ROW) Pageable pageable) throws Exception {
		return view("init", request, model, form, bindingResult, pageable);
	}

	@RequestMapping(value = SCREEN_PATH_DOPAGE)
	public String dopage(HttpServletRequest request, HttpSession session, Model model, MonthlyShiftCheckForm form,
			BindingResult bindingResult, @PageableDefault(page = 0, size = ROW) Pageable pageable) throws Exception {
		return view("page", request, model, form, bindingResult, pageable);
	}

	@RequestMapping(value = SCREEN_PATH_SEARCH)
	public String search(HttpServletRequest request, HttpSession session, Model model, MonthlyShiftCheckForm form,
			BindingResult bindingResult, @PageableDefault(page = 0, size = ROW) Pageable pageable) throws Exception {
		return view("search", request, model, form, bindingResult, pageable);
	}

	// 表示
	private String view(String processType, HttpServletRequest request, Model model, MonthlyShiftCheckForm form,
			BindingResult bindingResult, Pageable pageable) throws Exception {
		// 対象年月日
		String yearMonth;
		// 日付Benリスト
		List<DateBean> dateBeanList = new ArrayList<>();
		// セッション
		HttpSession session = request.getSession();

		// ログインユーザ情報をセッションより取得
		LoginUserDto loginUserDto = (LoginUserDto) session
				.getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);

		// セレクトボックスの取得
		ComboListUtilLogic comboListUtils = new ComboListUtilLogic();
		Set<String> yearMonthSet = new LinkedHashSet<>();
		Map<String, String> yearMonthCmbMap = new LinkedHashMap<>();
		List<String> yearMonthValues;

		if (processType == "init") {
			// システム日付より対象年月を取得する。
			yearMonth = CommonUtils.getFisicalDay(CommonConstant.YEARMONTH_NOSL);
			dateBeanList = CommonUtils.getDateBeanList(yearMonth);
			yearMonthCmbMap = comboListUtils.getComboYearMonth(yearMonth, 2, 1, false);
			yearMonthSet.addAll(yearMonthCmbMap.values());
			yearMonthCmbMap = comboListUtils.getComboYearMonth(yearMonth, 2, 0, false);
			yearMonthSet.addAll(yearMonthCmbMap.values());
			yearMonthValues = new ArrayList<>(yearMonthSet);
			yearMonthValues.sort(Comparator.naturalOrder());

			// 対象年月選択のため、初期表示年月を画面フォームにセット
			String initYearMonth = CommonUtils.changeFormat(yearMonth, CommonConstant.YEARMONTH_NOSL,
					CommonConstant.YEARMONTH);
			form.setYearMonth(initYearMonth);

			// ページ用にセッション属性に表示年月を設定(細井)
			session.setAttribute("yearMonth", form.getYearMonth());
		} else {
			// 共通部品で対象年月の1ヶ月分の日付情報格納クラスのリストを取得する。
			String searchYearMonth = null;
			if (processType == "search") {
				searchYearMonth = form.getYearMonth();
				// ページ用にセッション属性に表示年月を設定(細井)
				session.setAttribute("yearMonth", form.getYearMonth());
			} else {
				searchYearMonth = (String) session.getAttribute("yearMonth");
			}
			yearMonth = CommonUtils.changeFormat(searchYearMonth, CommonConstant.YEARMONTH,
					CommonConstant.YEARMONTH_NOSL);
			dateBeanList = CommonUtils.getDateBeanList(yearMonth);
			yearMonthCmbMap = comboListUtils.getComboYearMonth(yearMonth, 2, 1, false);
			yearMonthSet.addAll(yearMonthCmbMap.values());
			yearMonthCmbMap = comboListUtils.getComboYearMonth(yearMonth, 2, 0, false);
			yearMonthSet.addAll(yearMonthCmbMap.values());
			yearMonthValues = new ArrayList<>(yearMonthSet);
			yearMonthValues.sort(Comparator.naturalOrder());

			// 対象年月選択のため、選択年月を画面フォームにセット
			form.setYearMonth(searchYearMonth);
		}

		// ----------------------
		// 曜日定数を取得
		// ----------------------
		// 土曜日
		String saturday = DayOfWeek.SATURDAY.getWeekdayShort();
		// 日曜日
		String sunday = DayOfWeek.SUNDAY.getWeekdayShort();

		// シフトテーブルより各社員の月別シフトデータを取得する。
		Map<String, List<MonthlyShiftDto>> monthlyShiftDtoMap = monthlyShiftLogic.getMonthlyShiftDtoMap(yearMonth,
				true);

		List<MonthlyShiftCheckBean> monthlyShiftCheckBean = new ArrayList<MonthlyShiftCheckBean>();

		if (CheckUtils.isEmpty(monthlyShiftDtoMap)) {
			// データなし
			MonthlyShiftCheckBean monthlyShiftBean = new MonthlyShiftCheckBean();
			monthlyShiftBean.setEmployeeId(loginUserDto.getEmployeeId());
			monthlyShiftBean.setEmployeeName(loginUserDto.getEmployeeName());
			monthlyShiftCheckBean.add(monthlyShiftBean);
		} else {
			// データあり
			monthlyShiftCheckBean = dtoToBean(monthlyShiftDtoMap, loginUserDto);
		}
		// ページング(細井)=============================================================================================
		Page<MonthShift> p = page(pageable);
		Page<MonthShift> page = new PageImpl<MonthShift>(p.getContent(), pageable, monthlyShiftCheckBean.size());
		List<MonthlyShiftCheckBean> monthlyShiftCheckBeanNew = new ArrayList<MonthlyShiftCheckBean>();
		// 表示しないデータを削除
		String lstID = p.getContent().getLast().getEmployeeId();
		String firstID = p.getContent().getFirst().getEmployeeId();
		boolean bL = false;
		boolean bF = false;
		Iterator<MonthlyShiftCheckBean> iterator = monthlyShiftCheckBean.iterator();
		while (iterator.hasNext()) {
			MonthlyShiftCheckBean bean = iterator.next();
			String id = bean.getEmployeeId();

			if (id.equals(firstID)) {
				bF = true;
			}
			if (bF && !bL) {
				monthlyShiftCheckBeanNew.add(bean);
			}
			if (id.equals(lstID)) {
				bL = true;
			}
		}

		// =============================================================================================
		// フォームにデータをセットする
		// form.setShiftCmbMap(shiftCmbMap);
		form.setYearMonthCmbMap(yearMonthCmbMap);
		form.setMonthlyShiftCheckBeanList(monthlyShiftCheckBeanNew);
		form.setDateBeanList(dateBeanList);

		model.addAttribute("Page", page);
		model.addAttribute("MonthlyShiftCheckForm", form);
		model.addAttribute("yearMonthValues", yearMonthValues);
		model.addAttribute("monthlyShiftCheckBean", monthlyShiftCheckBeanNew);
		model.addAttribute("dateBeanList", dateBeanList);
		model.addAttribute("saturday", saturday);
		model.addAttribute("sunday", sunday);
		return "monthlyShiftCheck";
	}

	/**
	 * 印刷ボタン押下後Excelダウンロード時に動作する処理.
	 *
	 * @param request リクエスト情報
	 * @param model   リクエストスコープ上にオブジェクトを載せるためのmap
	 * @return view名称
	 * @throws Exception
	 */
	@RequestMapping(value = SCREEN_PATH_PRINT)
	private void print(HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model,
			MonthlyShiftCheckForm form, BindingResult bindingResult) throws Exception {

		// ログインユーザ情報をセッションより取得
		LoginUserDto loginUserDto = (LoginUserDto) session
				.getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);

		String yearMonth = CommonUtils.changeFormat(form.getYearMonth(), CommonConstant.YEARMONTH,
				CommonConstant.YEARMONTH_NOSL);

		// 共通部品で対象年月の1ヶ月分の日付情報格納クラスのリストを取得する。
		List<DateBean> dateBeanList = CommonUtils.getDateBeanList(yearMonth);

		// シフトテーブルより各社員の月別シフトデータを取得する。
		Map<String, List<MonthlyShiftDto>> monthlyShiftDtoMap = monthlyShiftLogic.getMonthlyShiftDtoMap(yearMonth,
				true);

		List<MonthlyShiftCheckBean> monthlyShiftCheckBean = new ArrayList<MonthlyShiftCheckBean>();

		if (CheckUtils.isEmpty(monthlyShiftDtoMap)) {
			// データなし
			MonthlyShiftCheckBean monthlyShiftBean = new MonthlyShiftCheckBean();
			monthlyShiftBean.setEmployeeId(loginUserDto.getEmployeeId());
			monthlyShiftBean.setEmployeeName(loginUserDto.getEmployeeName());
			monthlyShiftCheckBean.add(monthlyShiftBean);
		} else {
			// データあり
			monthlyShiftCheckBean = dtoToBean(monthlyShiftDtoMap, loginUserDto);
		}

		// Excel出力メソッドを呼び出す
		monthlyShiftLogic.print(response, dateBeanList, monthlyShiftCheckBean);

	}

	/**
	 * DtoからBeanへ変換する
	 * 
	 * @param monthlyShiftDtoMap
	 * @param loginUserDto
	 * @return 一覧に表示するリスト
	 * @author naraki
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private List<MonthlyShiftCheckBean> dtoToBean(Map<String, List<MonthlyShiftDto>> monthlyShiftDtoMap,
			LoginUserDto loginUserDto)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Collection<List<MonthlyShiftDto>> collection = monthlyShiftDtoMap.values();

		List<MonthlyShiftCheckBean> monthlyShiftCheckBeanList = new ArrayList<MonthlyShiftCheckBean>();

		for (List<MonthlyShiftDto> monthlyShiftDtoList : collection) {

			// 実行するオブジェクトの生成
			MonthlyShiftCheckBean monthlyShiftCheckBean = new MonthlyShiftCheckBean();

			// メソッドの取得
			Method[] methods = monthlyShiftCheckBean.getClass().getMethods();

			// メソッドのソートを行う
			Comparator<Method> sortAsc = new MethodComparator();
			Arrays.sort(methods, sortAsc); // 配列をソート

			int index = 0;
			int listSize = monthlyShiftDtoList.size();

			String employeeId = "";
			String employeeName = "";

			for (int i = 0; i < methods.length; i++) {
				// "setShiftIdXX" のメソッドを動的に実行する
				if (methods[i].getName().startsWith("setSymbol") && listSize > index) {
					MonthlyShiftDto monthlyShiftDto = monthlyShiftDtoList.get(index);
					// メソッド実行
					methods[i].invoke(monthlyShiftCheckBean, monthlyShiftDto.getSymbol());

					employeeId = monthlyShiftDto.getEmployeeId();
					employeeName = monthlyShiftDto.getEmployeeName();

					index++;
				}
			}

			monthlyShiftCheckBean.setEmployeeId(employeeId);
			monthlyShiftCheckBean.setEmployeeName(employeeName);

			monthlyShiftCheckBeanList.add(monthlyShiftCheckBean);

		}

		return monthlyShiftCheckBeanList;
	}
}