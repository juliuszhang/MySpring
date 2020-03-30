package org.myspring.jdbc;

import java.sql.ResultSet;

/**
 * @author yibozhang
 * @date 2020/3/30 11:29
 */
public class StringResultHandler implements ResultHandler<String> {
    @Override
    public String get(ResultSet resultSet, int index) throws Exception {
        return resultSet.getString(index);
    }
}
