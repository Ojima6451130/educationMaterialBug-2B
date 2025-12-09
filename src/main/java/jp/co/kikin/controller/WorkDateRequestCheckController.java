/**
 * ファイル名：WorkDateRequestCheckController.java
 *
 * 変更履歴
 * 1.0  2010/09/04 Kazuya.Naraki
 * Spring 2025/04/16 Shuta.Hashimoto
 * 
 */
package jp.co.kikin.controller;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jp.co.kikin.model.WorkDateRequestCheckForm;
import jp.co.kikin.pentity.MonthShift;
import jp.co.kikin.prepository.MonthShiftRepository;
import jp.co.kikin.service.ComboListUtilLogic;
import jp.co.kikin.service.CommonUtils;
import jp.co.kikin.service.MethodComparator;
import jp.co.kikin.service.MonthlyShiftLogic;
import jp.co.kikin.service.WorkDateRequestLogic;
import jp.co.kikin.CommonConstant.DayOfWeek;
import jp.co.kikin.bean.ResWorkDateRequestLogic;
import jp.co.kikin.constant.CommonConstant;
import jp.co.kikin.constant.RequestSessionNameConstant;
import jp.co.kikin.dto.LoginUserDto;
import jp.co.kikin.dto.WorkDateRequestCheckDto;
import jp.co.kikin.model.DateBean;
import jp.co.kikin.model.MonthlyShiftCheckBean;
import jp.co.kikin.model.WorkDateRequestCheckBean;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;



/**
 * 説明：出勤希望日確認画面
 * @author hashimoto
 */
// @Log4j2
@Controller
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
@RequestMapping(value = "/kikin")
public class WorkDateRequestCheckController {
	
	@Autowired
	CommonUtils commonUtils;

	@Autowired
	WorkDateRequestLogic workDateRequestLogic;

	@Autowired
	CommonConstant commonConstant;

	@Autowired
	ComboListUtilLogic comboListUtilLogic;

	@Autowired
	MonthShiftRepository mr;

    /** 画面URL */
    public static final String SCREEN_PATH = "/workDateRequestCheck";
    /** 「検索」押下時 */
    public static final String SCREEN_PATH_SEARCH = "/workDateRequestCheck/search";
    
    /** 「前へ」「次へ」押下時 */
    public static final String SCREEN_PATH_DOPAGE = "/workDateRequestCheck/dopage";
    
    public static final String PATH = "/kikin";
    
 // 1ページあたりの表示件数(古賀追加)
 	private final int ROW = 19;

    /**
     * Viewに共通URLを渡す.
     *
     * @return 保守画面共通URL
     */
    @ModelAttribute("path")
    public String getPath() {
        return PATH;
    }
    
 // ページング 古賀追記
 	public Page<MonthShift> page(Pageable pageable) {
 		Page<MonthShift> pageList = mr.findAll(pageable);
 		return pageList;
 	}

    /**
     * 説明：出勤希望日確認画面初期表示に動作する処理
     *
     * @param request リクエスト情報
     * @param model   リクエストスコープ上にオブジェクトを載せるためのmap
     * @return view名称
     * @throws Exception
     * @author hashimoto
     */
    @RequestMapping(value = SCREEN_PATH)
    public String init(HttpServletRequest request, HttpSession session, Model model, WorkDateRequestCheckForm form,
    		BindingResult result, @PageableDefault(page = 0, size = ROW) Pageable pageable) throws Exception {
        return view("init", request, session, model, form, result, pageable);
    }

    // doPage追加　古賀
    @RequestMapping(value = SCREEN_PATH_DOPAGE)
    public String doPage(HttpServletRequest request, HttpSession session, Model model, WorkDateRequestCheckForm form, 
    		BindingResult result, @PageableDefault(page = 0, size = ROW) Pageable pageable) throws Exception {
    	return view("page", request, session, model, form, result, pageable);
    }
    /**
     * 説明：出勤希望日確認画面表示処理
     *
     * @param request リクエスト情報
     * @param model   リクエストスコープ上にオブジェクトを載せるためのmap
     * @return view名称
     * @throws Exception
     * @author hashimoto
     */
    private String view(String processType, HttpServletRequest request, HttpSession session, Model model, WorkDateRequestCheckForm form, BindingResult bindingResult, Pageable pageable) throws Exception {

        // ログインユーザ情報をセッションより取得
        LoginUserDto loginUserDto = (LoginUserDto) session
                .getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_INFO);
        
        // 対象年月日
        String yearMonth;
        // 日付Benリスト
        List<DateBean> dateBeanList = new ArrayList<>();

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
            String initYearMonth = CommonUtils.changeFormat(yearMonth, CommonConstant.YEARMONTH_NOSL, CommonConstant.YEARMONTH);
            form.setYearMonth(initYearMonth);
            
            // ページングのためにセッションへ追加　古賀
            session.setAttribute("yearMonth", form.getYearMonth());
        } else {
        	// 共通部品で対象年月の1ヶ月分の日付情報格納クラスのリストを取得する。
        	// 年月をキープするためのif文追加　古賀
			String searchYearMonth = null;
			if (processType == "search") {
				searchYearMonth = form.getYearMonth();
				session.setAttribute("yearMonth", form.getYearMonth());
			} else {
				searchYearMonth = (String) session.getAttribute("yearMonth");
			}
            yearMonth = CommonUtils.changeFormat(searchYearMonth, CommonConstant.YEARMONTH, CommonConstant.YEARMONTH_NOSL);
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

        //----------------------
        // 曜日定数を取得
        //---------------------- 
        // 土曜日
        String saturday = DayOfWeek.SATURDAY.getWeekdayShort();
        // 日曜日
        String sunday   = DayOfWeek.SUNDAY.getWeekdayShort();

        //----------------------
        // 出勤希望データ取得
        //----------------------  
        //yearMonth = "202404";
        WorkDateRequestLogic workDateRequestLogic = new WorkDateRequestLogic();
        List<List<WorkDateRequestCheckDto>> workRequestCheckDtoList = workDateRequestLogic.getWorkDateRequestCheckDtoList(yearMonth);

        List<WorkDateRequestCheckBean> workDateRequestCheckBeanList = this.dtoToBean(workRequestCheckDtoList, loginUserDto);
        
        //----------------------
        // 画面出勤希望データ取得
        //---------------------- 
         List<ResWorkDateRequestLogic> resWorkDateRequestLogics = workDateRequestLogic.getWorkDateRequestScreenData(workRequestCheckDtoList, dateBeanList); // メソッド利用のためコメントアウトを外した　古賀
         
 		// ページング(細井)=============================================================================================
 		Page<MonthShift> p = page(pageable);
 		Page<MonthShift> page = new PageImpl<MonthShift>(p.getContent(), pageable, resWorkDateRequestLogics.size());
 		List<ResWorkDateRequestLogic> resWorkDateRequestLogicsNew = new ArrayList<ResWorkDateRequestLogic>();
 		// 表示しないデータを削除
 		String lstID = p.getContent().getLast().getEmployeeId();
 		String firstID = p.getContent().getFirst().getEmployeeId();
 		boolean bL = false;
 		boolean bF = false;
 		Iterator<ResWorkDateRequestLogic> iterator = resWorkDateRequestLogics.iterator();
 		while (iterator.hasNext()) {
 			ResWorkDateRequestLogic bean = iterator.next();
 			String id = bean.getEmployeeId();

 			if (id.equals(firstID)) {
 				bF = true;
 			}
 			if (bF && !bL) {
 				resWorkDateRequestLogicsNew.add(bean);
 			}
 			if (id.equals(lstID)) {
 				bL = true;
 			}
 		}
        //----------------
        // 画面への受渡し
        //----------------
        // 対象年月
        model.addAttribute("yearMonthValues", yearMonthValues);
        model.addAttribute("datebeanList", dateBeanList);
        model.addAttribute("workDateRequestCheckBeanList", workDateRequestCheckBeanList);
        model.addAttribute("resWorkDateRequestLogics", resWorkDateRequestLogicsNew); // modelにresWorkDateRequestLogicsを追加　古賀
        model.addAttribute("saturday", saturday);
        model.addAttribute("sunday", sunday);
        model.addAttribute("Page", page);
        return "workDateRequestCheck";
    }
    /**
     * 説明：出勤希望日入力画面表示アクションクラス
     *
     * @param mapping アクションマッピング
     * @param form アクションフォーム
     * @param req リクエスト
     * @param res レスポンス
     * @return アクションフォワード
     * @author naraki
     */
    @RequestMapping(value = SCREEN_PATH_SEARCH)
    public String search(HttpServletRequest request, HttpSession session, Model model, WorkDateRequestCheckForm form,
    		BindingResult result, @PageableDefault(page = 0, size = ROW) Pageable pageable) throws Exception {
        return view("search", request, session, model, form, result, pageable);
    }

    /**
     * DtoからBeanへ変換する
     *
     * @param workDateRequestInputDtoMap
     * @param loginUserDto
     * @return 一覧に表示するリスト
     * @author naraki
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    private List<WorkDateRequestCheckBean> dtoToBean(List<List<WorkDateRequestCheckDto>> workRequestCheckDtoNestedList,
            LoginUserDto loginUserDto)
            throws IllegalArgumentException,
            IllegalAccessException,
            InvocationTargetException {

        List<WorkDateRequestCheckBean> workDateRequestCheckBeanList = new ArrayList<WorkDateRequestCheckBean>();
        for (List<WorkDateRequestCheckDto> workRequestCheckDtoList : workRequestCheckDtoNestedList) {
            // 実行するオブジェクトの生成
            WorkDateRequestCheckBean workDateRequestCheckBean = new WorkDateRequestCheckBean();
            // メソッドの取得
            Method[] methods = workDateRequestCheckBean.getClass().getMethods();
            // メソッドのソートを行う
            Comparator<Method> sortAsc = new MethodComparator();
            Arrays.sort(methods, sortAsc); // 配列をソート
            int index = 0;
            int listSize = workRequestCheckDtoList.size();
            String employeeId = "";
            String employeeName = "";
            for (int i = 0; i < methods.length; i++) {
                // "setShiftIdXX" のメソッドを動的に実行する
                if (methods[i].getName().startsWith("setSymbol") && listSize > index) {
                    WorkDateRequestCheckDto workDateRequestCheckDto = workRequestCheckDtoList.get(index);
                    // メソッド実行
                    methods[i].invoke(workDateRequestCheckBean, workDateRequestCheckDto.getMyRequestSymbol());
                    employeeId = workDateRequestCheckDto.getEmployeeId();
                    employeeName = workDateRequestCheckDto.getEmployeeName();
                    index++;
                }
            }
            workDateRequestCheckBean.setEmployeeId(employeeId);
            workDateRequestCheckBean.setEmployeeName(employeeName);
            workDateRequestCheckBeanList.add(workDateRequestCheckBean);
        }
        return workDateRequestCheckBeanList;
    }

}
