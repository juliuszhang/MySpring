package org.myspring.jdbc;

import org.apache.commons.lang3.StringUtils;
import org.myspring.exception.TooManyResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yibozhang
 * @date 2020/3/30 10:15
 */
public class DataBaseHelper {

    private static final Logger LOG = LoggerFactory.getLogger(DataBaseHelper.class);

    public static void begin() {
        Connection connection = DBUtil.getConnection();
        try {
            if (connection != null) {
                connection.setAutoCommit(false);
            }
        } catch (SQLException e) {
            LOG.error("begin transaction failure.", e);
            throw new RuntimeException(e);
        }
    }

    public static void commit() {
        Connection connection = DBUtil.getConnection();
        try {
            if (connection != null) {
                connection.commit();
                connection.close();
            }
        } catch (SQLException e) {
            LOG.error("commit transaction failure.", e);
            throw new RuntimeException(e);
        }
    }

    public static void rollback() {
        Connection connection = DBUtil.getConnection();
        try {
            if (connection != null) {
                connection.rollback();
                connection.close();
            }
        } catch (SQLException e) {
            LOG.error("rollback transaction failure.", e);
            throw new RuntimeException(e);
        }
    }

    public static int insert(String sql) {
        Connection connection = DBUtil.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            return pst.execute(sql) ? 1 : -1;
        } catch (SQLException e) {
            LOG.error("insert into database failure.", e);
            throw new RuntimeException(e);
        }
    }

    public static int update(String sql, List<Object> params) {
        Connection connection = DBUtil.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            for (int i = 0; i < params.size(); i++) {
                Object value = params.get(i);
                if (value instanceof Integer) {
                    pst.setInt(i + 1, (Integer) value);
                } else if (value instanceof String) {
                    pst.setString(i + 1, (String) value);
                }
                //TODO
            }
            return pst.executeUpdate();
        } catch (SQLException e) {
            LOG.error("update data failure.", e);
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> queryList(String sql, Class<T> cls) {
        Connection connection = DBUtil.getConnection();
        try {
            PreparedStatement pst = connection.prepareStatement(sql);
            ResultSet resultSet = pst.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            return autoMappingResultSet(metaData, resultSet, cls);
        } catch (SQLException e) {
            LOG.error("query list from database failure.", e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T queryOne(String sql, Class<T> cls) {
        List<T> resultList = queryList(sql, cls);
        if (resultList.size() > 1) {
            throw new TooManyResultException();
        }
        return resultList.get(0);
    }

    public static <T> List<T> autoMappingResultSet(ResultSetMetaData metaData, ResultSet resultSet, Class<T> cls) {
        try {
            List<T> entityList = new ArrayList<>(resultSet.getFetchSize());
            while (resultSet.next()) {
                int columnCount = metaData.getColumnCount();
                T entity = cls.getConstructor().newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    ResultHandler<?> resultHandler = ResultHandlerMapping.getResultHandler(metaData.getColumnType(i));
                    Object value = resultHandler.get(resultSet, i);
                    String filedName = underline2Camel(columnName);
                    if (StringUtils.isNotEmpty(filedName)) {
                        Field field = cls.getDeclaredField(filedName);
                        field.setAccessible(true);
                        field.set(entity, value);
                    }
                }
                entityList.add(entity);
            }
            return entityList;
        } catch (Exception e) {
            LOG.error("mapping resultSet to entity failure.", e);
            throw new RuntimeException(e);
        }
    }

    public static String underline2Camel(String underlineName) {
        StringBuilder sb = new StringBuilder();
        boolean nextUpper = false;
        for (int i = 0; i < underlineName.length(); i++) {
            char c = underlineName.charAt(i);
            if (c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    sb.append(Character.toUpperCase(c));
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public static String camel2Underline(String camelName) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < camelName.length(); i++) {
            char c = camelName.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                sb.append("_").append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }
}
