package jp.co.kikin.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.kikin.constant.CommonConstant;
import jp.co.kikin.dao.SalaryDao;
import jp.co.kikin.dto.SalaryInfoDto;
import jp.co.kikin.exception.CommonException;
import jp.co.kikin.pentity.PostEntity;
import jp.co.kikin.pentity.SalaryInfoEntity;
import jp.co.kikin.prepository.PostRepository;
import jp.co.kikin.prepository.SalaryInfoRepository;
import jp.co.kikin.service.ComboListUtilLogic;
import jp.co.kikin.service.CommonUtils;

@Controller
@RequestMapping(value = "/kikin")
public class SalaryController {

	/** 画面URL */
	public static final String PATH = "/kikin";

	@Autowired
	SalaryInfoRepository repositorySalaryInfo;

	@Autowired
	PostRepository repositoryPost;

	/** サービス機能名={@value} */
	public static final String CONTENTS = "給料情報";

	// メニュー画面でボタン押下後
	@GetMapping("/salaryInformation")
	public String inputInfo(Model model) throws SQLException, CommonException {

		// 役職情報の取得
		List<PostEntity> postList = repositoryPost.findAll();

		// t_monthly_salaryの情報取得
		List<SalaryInfoEntity> salaryInfoList = repositorySalaryInfo.findAll();

		// リストの編集
		List<SalaryInfoDto> salaryInfoListNew = new ArrayList<SalaryInfoDto>();
		for (SalaryInfoEntity list : salaryInfoList) {
			SalaryInfoDto salaryInfo = new SalaryInfoDto();
			for (PostEntity post : postList) {
				if (list.getPost_ID().equals(post.getPostID())) {
					salaryInfo.setPostName(post.getPostName());
					salaryInfo.setBasicSalary(list.getBasic_salary());
					salaryInfo.setNightPrice(list.getNight_price());
					salaryInfo.setOvertimePrice(list.getOvertime_price());
					salaryInfo.setOtherPrice(list.getOther_price());
					salaryInfoListNew.add(salaryInfo);
					break;
				}
			}
		}

		List<String> insertPost = new ArrayList<>();
		for (PostEntity post : postList) {
			boolean exists = false;
			for (SalaryInfoDto dto : salaryInfoListNew) {
				if (dto.getPostName().equals(post.getPostName())) {
					exists = true; // 一致するものがある
					break; // これ以上調べる必要なし
				}
			}
			if (!exists) {
				insertPost.add(post.getPostName());
			}
		}

		model.addAttribute("postList", postList);
		model.addAttribute("insertPostList", insertPost);
		model.addAttribute("salaryInfoList", salaryInfoListNew);
		return "salaryInformation";
	}

	// 新規登録
	@PostMapping("/salaryInformation/insert")
	public String insertInfo(SalaryInfoDto salaryInfoDto, RedirectAttributes redirectAttributes) throws SQLException {

		// 役職情報の取得
		List<PostEntity> postList = repositoryPost.findAll();

		// 登録用のdataを修正
		SalaryInfoEntity updateData = new SalaryInfoEntity();
		for (PostEntity post : postList) {
			if (post.getPostName().equals(salaryInfoDto.getPostName())) {
				updateData.setPost_ID(post.getPostID());
				break;
			}
		}
		updateData.setBasic_salary(salaryInfoDto.getBasicSalary());
		updateData.setNight_price(salaryInfoDto.getNightPrice());
		updateData.setOvertime_price(salaryInfoDto.getOvertimePrice());
		updateData.setOther_price(salaryInfoDto.getOtherPrice());

		SalaryDao dao = new SalaryDao();
		dao.salaryInfoInsert(updateData);

		return "redirect:/kikin/salaryInformation";
	}

	// 削除
	@PostMapping("/salaryInformation/delete")
	public String deleteInfo(@RequestParam("postName") String postName, RedirectAttributes redirectAttributes)
			throws SQLException {

		// 役職情報の取得
		List<PostEntity> postList = repositoryPost.findAll();

		// postIDを取得
		String postID = null;
		for (PostEntity post : postList) {
			if (post.getPostName().equals(postName)) {
				postID = post.getPostID();
			}
		}

		SalaryDao dao = new SalaryDao();
		dao.salaryInfoDelete(postID);
		redirectAttributes.addFlashAttribute("message", "削除しました");
		return "redirect:/kikin/salaryInformation";
	}

	// 更新
	@PostMapping("/salaryInformation/update")
	public String updateInfo(SalaryInfoDto salaryInfoDto, RedirectAttributes redirectAttributes) throws SQLException {

		// 役職情報の取得
		List<PostEntity> postList = repositoryPost.findAll();

		// 更新用のdataを修正
		SalaryInfoEntity updateData = new SalaryInfoEntity();
		for (PostEntity post : postList) {
			if (post.getPostName().equals(salaryInfoDto.getPostName())) {
				updateData.setPost_ID(post.getPostID());
				break;
			}
		}
		updateData.setBasic_salary(salaryInfoDto.getBasicSalary());
		updateData.setNight_price(salaryInfoDto.getNightPrice());
		updateData.setOvertime_price(salaryInfoDto.getOvertimePrice());
		updateData.setOther_price(salaryInfoDto.getOtherPrice());

		SalaryDao dao = new SalaryDao();
		dao.salaryInfoUpdate(updateData);

		return "redirect:/kikin/salaryInformation";
	}

}
