package bid.adonis.lau.utils;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.io.IOException;
import java.sql.*;

/**
 * @author: Adonis Lau
 * @email: adonis.lau.dev@gmail.com
 * @date: 2018/5/17 23:55
 */
public class Database {

    private final QueryRunner runner;
    private final String password;
    private final String user;
    private final String driverUrl;
    private final String driver;

    public Database() throws IOException {
        runner = new QueryRunner();
        this.driver = "com.mysql.jdbc.Driver";
        this.driverUrl = "jdbc:mysql://192.168.1.115:3306/adonis_test?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&useSSL=false";
        this.user = "root";
        this.password = "546085758";
    }

    /**
     * 设置是否获取只读的connection，若非只读模式，则最后需要commit,或者rollback
     *
     * @param isRead 是否只读
     * @return
     * @throws SQLException
     */
    public Connection getConnection(boolean isRead) throws SQLException {
        try {
            Class.forName(driver);
            Connection realConnection = DriverManager.getConnection(driverUrl, user, password);

            realConnection.setReadOnly(isRead);
            if (!isRead) {
                realConnection.setAutoCommit(false);
            }
            return realConnection;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取只读的connection
     *
     * @return
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return getConnection(false);
    }

    public int update(Connection connection, String sql, Object... params) throws SQLException {
        System.out.println("sql " + sql +"; params " + params);
        return runner.update(connection, sql, params);
    }

    public long insert(Connection connection, String sql, Object... params) throws SQLException {
        System.out.println("sql " + sql +"; params " + params);
        PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        runner.fillStatement(statement, params);
        statement.execute();
        ResultSet result = statement.getGeneratedKeys();
        result.next();
        long id;
        try {
            id = result.getLong(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage() + e);
            id = 0;
        }
        return id;

    }

    public <T> T query(Connection connection, String sql, ResultSetHandler<T> rsh, Object... params) throws SQLException {
        System.out.println("sql " + sql +"; params " + params);
        return runner.query(connection, sql, rsh, params);
    }

    public void close(Connection conn) {
        DbUtils.closeQuietly(conn);
    }

    public void commit(Connection conn) throws SQLException {
        if (conn != null) {
            conn.commit();
        }
    }

    public void rollback(Connection conn) {
        if (conn != null) {
            DbUtils.rollbackAndCloseQuietly(conn);
        }
    }
}
