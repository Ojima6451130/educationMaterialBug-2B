package jp.co.kikin.service;


import org.springframework.stereotype.Service;
import jp.co.kikin.dao.UserShiftDao;
import jp.co.kikin.dto.DailyShiftDto;

@Service
public class UserShiftLogic {
	
	
	public DailyShiftDto getTodayShift(String yearMonthDay, String employeeId) throws Exception{
		UserShiftDao dao = new UserShiftDao();
		DailyShiftDto dto = dao.getTodayShiftByUser(yearMonthDay, employeeId);
		return dto;
		
		
	}

}
