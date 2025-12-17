package jp.co.kikin.service;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.stereotype.Service;
import jp.co.kikin.dao.AttendanceDao;
import jp.co.kikin.dto.ActualWorkBaseDto;
import jp.co.kikin.dto.WorkRecordDto;
import jp.co.kikin.model.DateBean;

//ホーム画面の打刻のサービス　長谷川

@Service // 出勤
public class AttendanceLogic {

	private final LoginLogic loginLogic;

	AttendanceLogic(LoginLogic loginLogic) {
		this.loginLogic = loginLogic;
	}

	public void setStartTime(String employeeId) throws Exception {
		AttendanceDao dao = new AttendanceDao();
		// 今日の日付取得
		String yearMonthDay = CommonUtils.getFisicalDay();
		// 現在の時間取得とフォーマット変更
		Timestamp now = CommonUtils.getCurrentTimestamp();
		String nowTime = CommonUtils.formatTimeToHHmm(now);
		// すでにDBに登録されているかの確認
		boolean existsRecord = dao.existsWorkRecord(employeeId, yearMonthDay);
		if (existsRecord) {
			String existStartTime = dao.getStartTime(employeeId, yearMonthDay);

			// nullと空文字以外の値がある
			if (existStartTime != null && !existStartTime.isEmpty()) {
				throw new Exception("E-MSG-000006");
			}
			// 空文字かnullならupdate
			dao.updateStaretTime(employeeId, yearMonthDay, nowTime);
		} else {
			// レコード無しinsert
			dao.insertStartTime(employeeId, yearMonthDay, nowTime);
		}
	}

	// 退勤
	public void setEndTime(String employeeId, String breakTime) throws Exception {
		AttendanceDao dao = new AttendanceDao();
		// 今日の日付取得
		String yearMonthDay = CommonUtils.getFisicalDay();
		// 現在の時間取得とフォーマット変更
		Timestamp now = CommonUtils.getCurrentTimestamp();
		String nowTime = CommonUtils.formatTimeToHHmm(now);
		// 出勤しているかの確認
		String startTime = dao.getStartTime(employeeId, yearMonthDay);
		if (startTime == null || startTime.isEmpty()) {
			throw new Exception("E-MSG-000007");
		}
		// すでに退勤済みかの確認
		String endTime = dao.getEndTime(employeeId, yearMonthDay);
		if (endTime != null && !endTime.isEmpty()) {
			throw new Exception("E-MSG-000006");
		}

		// dtoへデータの格納
		ActualWorkBaseDto shiftdto = dao.getActualWorkBaseData(employeeId, yearMonthDay);

		// WorkRecordDtoに値をセット(細井)
		WorkRecordDto workRecodeDto = new WorkRecordDto();
		workRecodeDto.setStartTime(startTime);
		workRecodeDto.setEndTime(nowTime);
		workRecodeDto.setBreakTime(breakTime); // あとで条件分岐
		workRecodeDto.setStartTimeShift(shiftdto.getShiftStartTime());
		workRecodeDto.setEndTimeShift(shiftdto.getShiftEndTime());
		workRecodeDto.setBreakTimeShift(shiftdto.getBreakTime());

		// 細井
		WorkRecordLogic workRecordLogic = new WorkRecordLogic();
		WorkRecordDto workRecordDtoNew = workRecordLogic.calculation(workRecodeDto);

		dao.updateWorkTimes(employeeId, yearMonthDay, workRecordDtoNew.getActualWorkTime(),
				workRecordDtoNew.getOverTime(), workRecordDtoNew.getHolidayTime(), workRecordDtoNew.getBreakTime(),
				workRecodeDto.getEndTime());
	}

//	// 打刻ロジック
//	public void calculateAndUpdateActualWorkTime(String employeeId, String yearMonthDay) throws Exception {
//		AttendanceDao dao = new AttendanceDao();
//		// dtoへデータの格納
//		ActualWorkBaseDto dto = dao.getActualWorkBaseData(employeeId, yearMonthDay);
//		if (dto == null) {
//			throw new Exception("勤務データが存在しません");
//		}
//		// 出勤した時刻
//		long startSec = CommonUtils.getSecond(dto.getStartTime());
//		// 退勤した時刻
//		long endSec = CommonUtils.getSecond(dto.getEndTime());
//		// 日またぎ対策
//		if (endSec < startSec) {
//			endSec += 24 * 60 * 60;
//		}
//		// シフトスタート時間
//		long shiftStartSec = 0;
//		// シフトエンド時間
//		long shiftEndSec = 0;
//		if (!dto.isNoShift()) {
//			shiftStartSec = CommonUtils.getSecond(dto.getShiftStartTime());
//			shiftEndSec = CommonUtils.getSecond(dto.getShiftEndTime());
//		}
//		// 休日祝日判定
//		String yearMonth = yearMonthDay.substring(0, 6);
//		List<DateBean> dateList = CommonUtils.getDateBeanList(yearMonth);
//
//		DateBean today = dateList.stream().filter(d -> d.getYearMonthDay().equals(yearMonthDay)).findFirst()
//				.orElse(null);
//
//		if (today == null) {
//			throw new Exception("日付情報が取得できません");
//
//		}
//		boolean isHoliday = today.getPublicHolidayFlg()
//				|| today.getWeekDayEnum() == jp.co.kikin.constant.CommonConstant.DayOfWeek.SATURDAY
//				|| today.getWeekDayEnum() == jp.co.kikin.constant.CommonConstant.DayOfWeek.SUNDAY || dto.isNoShift();
//		// 拘束時間時間
//		long workSec = endSec - startSec;
//
//		// 5時間以下かどうかの判定 休憩があるかどうか
//		long breakSec = 0;
//		if (workSec >= 5 * 60 * 60) {
//			if (dto.getBreakTime() != null && !"".equals(dto.getBreakTime())) {
//				breakSec = CommonUtils.getSecond(dto.getBreakTime());
//			}
//
//		}
//
//		// 実働時間(負の数にならないように)
//		long actualWorkSec = Math.max(workSec - breakSec, 0);
//
//		// シフト自体の勤務時間(負の数にならないように)
//		long scheduledSec = 0;
//		if (!isHoliday) {
//			scheduledSec = Math.max(shiftEndSec - shiftStartSec - breakSec, 0);
//		}
//
//		// actual_work_time確定
//		String actualWorkTime = String.format("%02d:%02d", actualWorkSec / 3600, (actualWorkSec % 3600) / 60);
//
//		// break_time確定
//		String breakTime = String.format("%02d:%02d", breakSec / 3600, (breakSec % 3600) / 60);
//
//		// 残業休日出勤時間
//		String overTime = "00:00";
//		String holidayWorkTime = "00:00";
//
//		if (dto.isNoShift() || isHoliday) {
//			holidayWorkTime = actualWorkTime;
//		} else {
//
//			// 残業時間
//			long overSec = Math.max(actualWorkSec - scheduledSec, 0);
//			overTime = String.format("%02d:%02d", overSec / 3600, (overSec % 3600) / 60);
//		}
//		dao.updateWorkTimes(employeeId, yearMonthDay, actualWorkTime, overTime, holidayWorkTime, breakTime);
//	}

}
