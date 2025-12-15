package jp.co.kikin.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//ホーム画面の打刻のDAO　長谷川
public class AttendanceDao extends Dao{
	private Log log = LogFactory.getLog(this.getClass());
	
	//DBにすでに登録されているかの確認
	public boolean existsWorkRecord(String employeeId, String yearMonthDay) throws Exception{
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
		}finally {
			disConnect();
		}
		return false;
		
	}
	
	//出勤情報確認
	public String getStartTime(String employeeId, String yearMonthDay)throws Exception {
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
		}finally {
			disConnect();
		}
		return startTime;
		
	}
	
	//出勤打刻
	public void insertStartTime(String employeeId, String yearMonthDay, String startTime)throws Exception {
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
		}finally {
			disConnect();
		}
	}
	//空文字で入力されている場合の出勤打刻
	public void updateStaretTime(String employeeId, String yearMonthDay, String startTime)throws Exception {
		try {
			this.connect();
			StringBuilder sql = new StringBuilder();
			sql.append(" update t_work_record ");
			sql.append(" set start_time = ? , ");
			sql.append(" updater_employee_id = ? , ");
			sql.append(" update_datetime = CURRENT_TIMESTAMP ,");
			sql.append(" where employee_id = ? , ");
			sql.append(" and ");
			sql.append(" work_day = ? , ");
			
			PreparedStatement ps = connection.prepareStatement(sql.toString());
			ps.setString(1, startTime);
			ps.setString(2, employeeId);
			ps.setString(3, employeeId);
			ps.setString(4, yearMonthDay);
			
			log.info(ps);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}finally {
			disConnect();
		}
	}
	
	//退勤情報確認
		public String getEndTime(String employeeId, String yearMonthDay)throws Exception {
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
			}finally {
				disConnect();
			}
			return endTime;
			
		}
	//退勤打刻
	public void updateEndTime(String employeeId, String yearMonthDay, String endTime)throws Exception {
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
		}finally {
			disConnect();
		}
	}
}
