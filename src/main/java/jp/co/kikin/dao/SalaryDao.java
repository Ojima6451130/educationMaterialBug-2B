package jp.co.kikin.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.kikin.constant.DbConstant.M_shift;
import jp.co.kikin.dto.SalaryInfoDto;
import jp.co.kikin.dto.ShiftMstMntDto;
import jp.co.kikin.pentity.SalaryInfoEntity;
import jp.co.kikin.service.CommonUtils;

public class SalaryDao extends Dao {

	// ログ出力クラス
	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * 給料マスタのデータを一部を取得する
	 *
	 */
	public SalaryInfoEntity getEntity(String postID) throws SQLException {

		// 戻り値
		SalaryInfoEntity data = new SalaryInfoEntity();

		try {
			// コネクション接続
			this.connect();

			StringBuffer strSql = new StringBuffer();
			strSql.append("SELECT * FROM ");
			strSql.append("m_salary_Info ");
			strSql.append(" where post_ID = ? ");

			PreparedStatement ps = connection.prepareStatement(strSql.toString());
			ps.setString(1, postID);

			// ログ出力
			log.info(ps);

			// SQLを実行する
			ResultSet rs = ps.executeQuery();

			// 取得結果セット
			while (rs.next()) {
				data.setPost_ID(rs.getString("post_ID"));
				data.setBasic_salary(rs.getInt("basic_salary"));
				data.setNight_price(rs.getInt("night_price"));
				data.setOvertime_price(rs.getInt("overtime_price"));
				data.setOther_price(rs.getInt("other_price"));
			}
		} catch (SQLException e) {
			// 例外発生
			throw e;
		} finally {
			// コネクション切断
			disConnect();
		}
		return data;
	}

	/**
	 * 給料マスタDELETE
	 */
	public boolean salaryInfoDelete(String postID) throws SQLException {

		try {
			// コネクション接続
			this.connect();

			StringBuffer strSql = new StringBuffer();
			strSql.append("DELETE FROM ");
			strSql.append("m_salary_Info ");
			strSql.append(" where post_ID = ? ");

			PreparedStatement ps = connection.prepareStatement(strSql.toString());
			ps.setString(1, postID);

			// ログ出力
			log.info(ps);

			// SQLを実行する
			int rs = ps.executeUpdate();

		} catch (SQLException e) {
			// 例外発生
			throw e;
		} finally {
			// コネクション切断
			disConnect();
		}
		return true;
	}

	/**
	 * 給料マスタUPDATE
	 */
	public boolean salaryInfoUpdate(SalaryInfoEntity SalaryInfoEntity) throws SQLException {

		try {
			// コネクション接続
			this.connect();

			StringBuffer strSql = new StringBuffer();
			strSql.append("UPDATE m_salary_info SET ");
			strSql.append("basic_salary = ?, ");
			strSql.append("night_price = ?, ");
			strSql.append("overtime_price = ?, ");
			strSql.append("other_price = ? ");
			strSql.append("WHERE post_ID = ? ");
 
			PreparedStatement ps = connection.prepareStatement(strSql.toString());
			ps.setInt(1, SalaryInfoEntity.getBasic_salary());
			ps.setInt(2, SalaryInfoEntity.getNight_price());
			ps.setInt(3, SalaryInfoEntity.getOvertime_price());
			ps.setInt(4, SalaryInfoEntity.getOther_price());
			ps.setString(5, SalaryInfoEntity.getPost_ID());

			// ログ出力
			log.info(ps);

			// SQLを実行する
			int rs = ps.executeUpdate();

		} catch (SQLException e) {
			// 例外発生
			throw e;
		} finally {
			// コネクション切断
			disConnect();
		}
		return true;
	}
	/**
	 * 給料マスタinsert
	 */
	public boolean salaryInfoInsert(SalaryInfoEntity SalaryInfoEntity) throws SQLException {

		try {
			// コネクション接続
			this.connect();
			
			StringBuffer strSql = new StringBuffer();
			strSql.append("INSERT INTO m_salary_info (basic_salary, night_price, overtime_price, other_price, post_ID) ");
			strSql.append("VALUES(");
			strSql.append("?, ?, ?, ?, ?)");

			PreparedStatement ps = connection.prepareStatement(strSql.toString());
			ps.setInt(1, SalaryInfoEntity.getBasic_salary());
			ps.setInt(2, SalaryInfoEntity.getNight_price());
			ps.setInt(3, SalaryInfoEntity.getOvertime_price());
			ps.setInt(4, SalaryInfoEntity.getOther_price());
			ps.setString(5, SalaryInfoEntity.getPost_ID());

			// ログ出力
			log.info(ps);

			// SQLを実行する
			int rs = ps.executeUpdate();

		} catch (SQLException e) {
			// 例外発生
			throw e;
		} finally {
			// コネクション切断
			disConnect();
		}
		return true;
	}
}
