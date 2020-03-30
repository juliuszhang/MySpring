package org.myspring.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;

/**
 * @author yibozhang
 * @date 2020/3/30 11:01
 */
public interface ResultHandler<T> {

    Logger LOG = LoggerFactory.getLogger(ResultHandler.class);

    T get(ResultSet resultSet, int index) throws Exception;

    default T getResult(ResultSet resultSet, int index) {
        try {
            return get(resultSet, index);
        } catch (Exception e) {
            LOG.error("result handler handle result failure.", e);
            throw new RuntimeException(e);
        }
    }
}
