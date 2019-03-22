package org.jaiken.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @ClassName: DBUtil
 * 
 * @Description: TODO connected mysql-database and deal with check results
 * 
 * @author: JaikenWong
 * 
 * @date: 2019年1月19日 下午2:11:28
 */
public class DBUtil {

	private static String driver = "com.mysql.jdbc.Driver";
	private static String url = "jdbc:mysql://127.0.0.1:3306/fishnet?characterEncoding=utf8&useSSL=true";
	private static String name = "root";
	private static String passwd = "1234";

	public static Connection getConnection() {
		try {
			Class.forName(driver).newInstance();
			return DriverManager.getConnection(url, name, passwd);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static void CloseConn(Connection conn, Statement st, ResultSet rst) {
		if (rst != null) {
			try {
				rst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (st != null) {
			try {
				st.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {

		System.out.println(getConnection());
	}

}
