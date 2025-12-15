package jp.co.kikin.service;

import java.sql.Timestamp;

import org.springframework.stereotype.Service;

import jp.co.kikin.dao.AttendanceDao;

//ホーム画面の打刻のサービス　長谷川

@Service //出勤
public class AttendanceLogic {
	public void setStartTime(String employeeId)throws Exception {
		AttendanceDao dao = new AttendanceDao();
		//今日の日付取得
		String yearMonthDay = CommonUtils.getFisicalDay();
		//現在の時間取得とフォーマット変更
		Timestamp now = CommonUtils.getCurrentTimestamp();
		String nowTime = CommonUtils.formatTimeToHHmm(now);
		//すでにDBに登録されているかの確認
		boolean existsRecord = dao.existsWorkRecord(employeeId, yearMonthDay);
		if (existsRecord) {
			String existStartTime = dao.getStartTime(employeeId, yearMonthDay);
			
			//nullと空文字以外の値がある
			if (existStartTime != null && !existStartTime.isEmpty()) {
				throw new Exception("E-MSG-000006");
			}
			//空文字かnullならupdate
			dao.updateStaretTime(employeeId, yearMonthDay, nowTime);
		}else {
			//レコード無しinsert
			dao.insertStartTime(employeeId, yearMonthDay, nowTime);
		}
	}
	
	//退勤
	public void setEndTime(String employeeId)throws Exception {
		AttendanceDao dao = new AttendanceDao();
		//今日の日付取得
		String yearMonthDay = CommonUtils.getFisicalDay();
		//現在の時間取得とフォーマット変更
		Timestamp now = CommonUtils.getCurrentTimestamp();
		String nowTime = CommonUtils.formatTimeToHHmm(now);
		//出勤しているかの確認
		String startTime = dao.getStartTime(employeeId, yearMonthDay);
		if (startTime == null) {
			throw new Exception("E-MSG-000007");
		}
		//すでに退勤済みかの確認
		String endTime = dao.getEndTime(employeeId, yearMonthDay);
		if (endTime != null) {
			throw new Exception("E-MSG-000006");
		}
		
		dao.updateEndTime(employeeId, yearMonthDay, nowTime);
	}

}
