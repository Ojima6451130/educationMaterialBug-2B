package jp.co.kikin.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jp.co.kikin.constant.RequestSessionNameConstant;
import jp.co.kikin.dao.NewsDao;
import jp.co.kikin.dto.SalaryInfoDto;
import jp.co.kikin.exception.CommonException;
import jp.co.kikin.pentity.NewsRecode;
import jp.co.kikin.prepository.NewsRepository;

@Controller
@RequestMapping(value = "/kikin")
public class NewsController {

	/** 画面URL */
	public static final String PATH = "/kikin";

	@Autowired
	NewsRepository newsRepository;

	@GetMapping("/news")
	public String viewNews(HttpSession session, HttpServletRequest req, Model model)
			throws SQLException, CommonException {

		List<NewsRecode> newsList = newsRepository.findAll();
		String employeeId = (String) session.getAttribute(RequestSessionNameConstant.SESSION_CMN_LOGIN_USER_ID);

		model.addAttribute("employeeId", employeeId);
		model.addAttribute("newsList", newsList);

		return "news";
	}

	// 新規登録
	@PostMapping("/news/insert")
	public String insertNews(NewsRecode newsRecode, RedirectAttributes redirectAttributes) throws SQLException {
		NewsDao dao = new NewsDao();
		dao.insertNews(newsRecode);

		return "redirect:/kikin/news";
	}

	// 更新
	@PostMapping("/news/update")
	public String updateNews(NewsRecode newsRecode, RedirectAttributes redirectAttributes) throws SQLException {
		NewsDao dao = new NewsDao();
		dao.updateNews(newsRecode);
		return "redirect:/kikin/news";
	}

	// 削除
	@PostMapping("/news/delete")
	public String deleteNews(NewsRecode newsRecode, RedirectAttributes redirectAttributes) throws SQLException {
		NewsDao dao = new NewsDao();
		dao.deleteNews(newsRecode.getId());
		return "redirect:/kikin/news";
	}
}