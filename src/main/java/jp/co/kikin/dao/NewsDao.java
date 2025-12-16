package jp.co.kikin.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.kikin.pentity.NewsRecode;
import jp.co.kikin.pentity.SalaryInfoEntity;

public class NewsDao extends Dao {

	// ログ出力クラス
	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * お知らせDELETE
	 */
	public boolean deleteNews(int id) throws SQLException {

		try {
			// コネクション接続
			this.connect();

			StringBuffer strSql = new StringBuffer();
			strSql.append("DELETE FROM ");
			strSql.append("t_news");
			strSql.append(" where id = ? ");

			PreparedStatement ps = connection.prepareStatement(strSql.toString());
			ps.setInt(1, id);

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
	 * お知らせUPDATE
	 */
	public boolean updateNews(NewsRecode newsRecode) throws SQLException {

		try {
			// コネクション接続
			this.connect();

			StringBuffer strSql = new StringBuffer();
			strSql.append("UPDATE t_news SET ");
			strSql.append("employee_id = ?, ");
			strSql.append("news_priority = ?, ");
			strSql.append("memo = ?, ");
			strSql.append("update_datetime = current_timestamp()");
			strSql.append("WHERE id = ? ");

			PreparedStatement ps = connection.prepareStatement(strSql.toString());
			ps.setString(1, newsRecode.getEmployeeId());
			ps.setString(2, newsRecode.getNewsPriority());
			ps.setString(3, newsRecode.getMemo());
			ps.setInt(4, newsRecode.getId());

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
	 * お知らせinsert
	 */
	public boolean insertNews(NewsRecode newsRecode) throws SQLException {

		try {
			// コネクション接続
			this.connect();

			StringBuffer strSql = new StringBuffer();
			strSql.append("INSERT INTO t_news (employee_id,news_priority,memo,update_datetime) ");
			strSql.append("VALUES(");
			strSql.append("?, ?, ?, current_timestamp())");

			PreparedStatement ps = connection.prepareStatement(strSql.toString());
			ps.setString(1, newsRecode.getEmployeeId());
			ps.setString(2, newsRecode.getNewsPriority());
			ps.setString(3, newsRecode.getMemo());

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
