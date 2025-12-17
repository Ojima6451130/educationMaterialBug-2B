package jp.co.kikin.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.kikin.dto.ActualWorkBaseDto;

//ホーム画面の打刻のDAO　長谷川
public class AttendanceDao extends Dao {
	private Log log = LogFactory.getLog(this.getClass());

	// DBにすでに登録されているかの確認
	public boolean existsWorkRecord(String employeeId, String yearMonthDay) throws Exception {
		boolean exists = false;

		try {
			this.connect();

			StringBuilder sql = new StringBuilder();

			sql.append(" select 1 ");
			sql.append(" from t_work_record ");
			sql.append(" where employee_id = ? ");
			sql.append(" and ");
			sql.append(" work_day = ? ");

			PreparedStatement ps = connection.prepareStatement(sql.toString());
			ps.setString(1, employeeId);
			ps.setString(2, yearMonthDay);

			log.info(ps);

			ResultSet rs = ps.executeQuery();
			exists = rs.next();
		} catch (SQLException e) {
			throw e;
		} finally {
			disConnect();
		}
		return exists;

	}

	// 出勤情報確認
	public String getStartTime(String employeeId, String yearMonthDay) throws Exception {
		String startTime = null;
		try {
			this.connect();

			StringBuilder sql = new StringBuilder();
			sql.append(" select start_time ");
			sql.append(" from t_work_record ");
			sql.append(" where employee_id = ? ");
			sql.append(" and ");
			sql.append(" work_day = ? ");

			PreparedStatement ps = connection.prepareStatement(sql.toString());
			ps.setString(1, employeeId);
			ps.setString(2, yearMonthDay);

			log.info(ps);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				startTime = rs.getString("start_time");
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			disConnect();
		}
		return startTime;

	}

	// 出勤打刻
	public void insertStartTime(String employeeId, String yearMonthDay, String startTime) throws Exception {
		try {
			this.connect();

			StringBuilder sql = new StringBuilder();
			sql.append(" insert into t_work_record ");
			sql.append(" ( employee_id, ");
			sql.append(" work_day, ");
			sql.append(" start_time, ");
			sql.append(" creator_employee_id, ");
			sql.append(" updater_employee_id, ");
			sql.append(" creation_datetime, ");
			sql.append(" update_datetime) ");
			sql.append(" values( ");
			sql.append(" ?, ?, ?, ?, ?, ");
			sql.append(" CURRENT_TIMESTAMP, CURRENT_TIMESTAMP ");
			sql.append(")");

			PreparedStatement ps = connection.prepareStatement(sql.toString());
			ps.setString(1, employeeId);
			ps.setString(2, yearMonthDay);
			ps.setString(3, startTime);
			ps.setString(4, employeeId);
			ps.setString(5, employeeId);

			log.info(ps);

			ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			disConnect();
		}
	}

	// 空文字で入力されている場合の出勤打刻
	public void updateStaretTime(String employeeId, String yearMonthDay, String startTime) throws Exception {
		try {
			this.connect();
			StringBuilder sql = new StringBuilder();
			sql.append(" update t_work_record ");
			sql.append(" set start_time = ? , ");
			sql.append(" updater_employee_id = ? , ");
			sql.append(" update_datetime = CURRENT_TIMESTAMP ");
			sql.append(" where employee_id = ? ");
			sql.append(" and ");
			sql.append(" work_day = ? ");

			PreparedStatement ps = connection.prepareStatement(sql.toString());
			ps.setString(1, startTime);
			ps.setString(2, employeeId);
			ps.setString(3, employeeId);
			ps.setString(4, yearMonthDay);

			log.info(ps);

			ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			disConnect();
		}
	}

	// 退勤情報確認
	public String getEndTime(String employeeId, String yearMonthDay) throws Exception {
		String endTime = null;
		try {
			this.connect();

			StringBuilder sql = new StringBuilder();
			sql.append(" select end_time ");
			sql.append(" from t_work_record ");
			sql.append(" where employee_id = ? ");
			sql.append(" and ");
			sql.append(" work_day = ? ");

			PreparedStatement ps = connection.prepareStatement(sql.toString());
			ps.setString(1, employeeId);
			ps.setString(2, yearMonthDay);

			log.info(ps);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				endTime = rs.getString("end_time");
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			disConnect();
		}
		return endTime;

	}

	// 退勤打刻
	public void updateEndTime(String employeeId, String yearMonthDay, String endTime) throws Exception {
		try {
			this.connect();

			StringBuilder sql = new StringBuilder();
			sql.append(" update t_work_record ");
			sql.append(" set end_time = ? , ");
			sql.append(" updater_employee_id = ? , ");
			sql.append(" update_datetime = CURRENT_TIMESTAMP ");
			sql.append(" where employee_id = ? ");
			sql.append(" and ");
			sql.append(" work_day = ? ");

			PreparedStatement ps = connection.prepareStatement(sql.toString());
			ps.setString(1, endTime);
			ps.setString(2, employeeId);
			ps.setString(3, employeeId);
			ps.setString(4, yearMonthDay);

			log.info(ps);

			ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			disConnect();
		}
	}

	// 実労働時間計算用データ取得
	public ActualWorkBaseDto getActualWorkBaseData(String employeeId, String yearMonthDay) throws Exception {
		ActualWorkBaseDto dto = null;
		try {
			this.connect();

			StringBuilder sql = new StringBuilder();
			sql.append(" select ");
			sql.append(" wr.start_time as work_start_time , ");
			sql.append(" wr.end_time as work_end_time , ");
			sql.append(" ms.break_time as break_time , ");
			sql.append(" ms.start_time as shift_start_time , ");
			sql.append(" ms.end_time as shift_end_time , ");
			sql.append(" ms.break_time as shift_break_time , ");
			sql.append(" case ");
			sql.append(" when ts.shift_id is null then 1 ");
			sql.append(" else 0 ");
			sql.append(" end as no_shift_flg ");
			sql.append(" from t_work_record wr ");
			sql.append(" left join t_shift ts ");
			sql.append(" on wr.employee_id = ts.employee_id ");
			sql.append(" and wr.work_day = ts.year_month_day ");
			sql.append(" left join m_shift ms ");
			sql.append(" on ts.shift_id = ms.shift_id ");
			sql.append(" where wr.employee_id = ? ");
			sql.append(" and ");
			sql.append(" wr.work_day = ? ");

			PreparedStatement ps = connection.prepareStatement(sql.toString());
			ps.setString(1, employeeId);
			ps.setString(2, yearMonthDay);

			log.info(ps);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				dto = new ActualWorkBaseDto();
				dto.setStartTime(rs.getString("work_start_time"));
				dto.setEndTime(rs.getString("work_end_time"));
				dto.setBreakTime(rs.getString("break_time"));
				dto.setShiftStartTime(rs.getString("shift_start_time"));
				dto.setShiftEndTime(rs.getString("shift_end_time"));
				dto.setNoShift(rs.getInt("no_shift_flg") == 1);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			disConnect();
		}
		return dto;

	}

	// 退勤押したときにすべての登録
	public void updateWorkTimes(String employeeId, String yearMonthDay, String actualWorkTime, String overTime,
			String holidayWorkTime, String breakTime, String endTime) throws Exception {
		try {
			this.connect();

			StringBuilder sql = new StringBuilder();

			sql.append(" update t_work_record ");
			sql.append(" set ");
			sql.append(" actual_work_time = ? , ");
			sql.append(" over_time = ? , ");
			sql.append(" holiday_work_time = ? , ");
			sql.append(" break_time = ? , ");
			sql.append(" updater_employee_id = ? , ");
			sql.append(" end_time = ? , ");
			sql.append(" update_datetime =  CURRENT_TIMESTAMP ");
			sql.append(" where employee_id =  ? ");
			sql.append(" and work_day = ? ");

			PreparedStatement ps = connection.prepareStatement(sql.toString());

			int idx = 1;
			ps.setString(idx++, actualWorkTime);
			ps.setString(idx++, overTime);
			ps.setString(idx++, holidayWorkTime);
			ps.setString(idx++, breakTime);
			ps.setString(idx++, employeeId);
			ps.setString(idx++, endTime);
			ps.setString(idx++, employeeId);
			ps.setString(idx++, yearMonthDay);

			log.info(ps);

			ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			disConnect();
		}
	}
}
