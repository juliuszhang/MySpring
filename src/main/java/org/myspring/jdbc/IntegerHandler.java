package org.myspring.jdbc;


import java.sql.ResultSet;

/**
 * @author yibozhang
 * @date 2020/3/30 11:06
 */
public class IntegerHandler implements ResultHandler<Integer> {

    @Override
    public Integer get(ResultSet resultSet, int index) throws Exception {
        return resultSet.getInt(index);
    }
}
