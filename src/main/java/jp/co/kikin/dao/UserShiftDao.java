package jp.co.kikin.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.kikin.constant.DbConstant.M_employee;
import jp.co.kikin.constant.DbConstant.M_shift;
import jp.co.kikin.dto.DailyShiftDto;
import jp.co.kikin.service.CommonUtils;

public class UserShiftDao extends Dao{
	private Log log = LogFactory.getLog(this.getClass());
	
	//ログインユーザー1人文の今日のシフトを取得
	//ホーム画面の今日のシフト表示のDAO 長谷川
	public DailyShiftDto getTodayShiftByUser(String yearMonthday, String employeeId) throws SQLException{
		DailyShiftDto dto = null;
		try {
			this.connect();
			StringBuilder sql = new StringBuilder();
			sql.append(" select");
			sql.append(" emp.employee_id, ");
			sql.append(" emp.employee_name, ");
			sql.append(" shift.start_time, ");
			sql.append(" shift.end_time, ");
			sql.append(" shift.break_time ");
			sql.append(" from ");
			sql.append(" m_employee emp inner join ");
			sql.append(" (select ");
			sql.append(" ts.employee_id, ");
			sql.append(" ms.start_time, ");
			sql.append(" ms.end_time, ");
			sql.append(" ms.break_time ");
			sql.append(" from ");
			sql.append(" t_shift ts left outer join ");
			sql.append(" m_shift ms ");
			sql.append(" on ts.shift_id = ms.shift_id ");
			sql.append(" where ");
			sql.append(" ts.year_month_day = ? ");
			sql.append(" and ts.employee_id = ? ");
			sql.append(" ) shift on emp.employee_id = shift.employee_id ");
			PreparedStatement ps = connection.prepareStatement(sql.toString());
			ps.setString(1, yearMonthday);
			ps.setString(2, employeeId);
			
			log.info(ps);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				dto = new DailyShiftDto();
				dto.setEmployeeName(CommonUtils.changeNullToBlank(rs.getString(M_employee.EMPLOYEE_NAME.getName())));
				dto.setStartTime(CommonUtils.changeNullToBlank(rs.getString(M_shift.START_TIME.getName())));
                dto.setEndTime(CommonUtils.changeNullToBlank(rs.getString(M_shift.END_TIME.getName())));
                dto.setBreakTime(CommonUtils.changeNullToBlank(rs.getString(M_shift.BREAK_TIME.getName())));
			}
		} catch (SQLException e) {
			throw e;
		}finally {
			disConnect();
		}
		
		return dto;
		
	}

}
