package org.myspring.jdbc;

import org.myspring.config.ConfigHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author yibozhang
 * @date 2020/3/30 9:57
 */
public class DBUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DBUtil.class);

    /********jdbc config**********/
    private static final String DRIVER = ConfigHelper.getJdbcDriver();
    private static final String URL = ConfigHelper.getJdbcUrl();
    private static final String USERNAME = ConfigHelper.getJdbcUsername();
    private static final String PASSWORD = ConfigHelper.getJdbcPassword();

    private static final ThreadLocal<Connection> CONNECTION_POOL = new ThreadLocal<>();

    public static Connection getConnection() {
        Connection connection = CONNECTION_POOL.get();
        try {
            if (connection == null) {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                CONNECTION_POOL.set(connection);
            }
        } catch (ClassNotFoundException |
                SQLException e) {
            LOG.error("get jdbc connection failure.", e);
        }
        return connection;
    }

    public static void closeConnection() {
        Connection connection = CONNECTION_POOL.get();
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            LOG.error("close jdbc connection failure.", e);
        } finally {
            CONNECTION_POOL.remove();
        }
    }
}
