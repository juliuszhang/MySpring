package org.myspring.jdbc;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yibozhang
 * @date 2020/3/30 11:05
 */
public class ResultHandlerMapping {

    private static final Map<Integer, ResultHandler<?>> MAPPING = new HashMap<>();


    static {
        MAPPING.put(Types.INTEGER, new IntegerHandler());
        MAPPING.put(Types.CHAR, new StringResultHandler());
        MAPPING.put(Types.VARCHAR, new StringResultHandler());
    }

    public static ResultHandler<?> getResultHandler(Integer type) {
        return MAPPING.get(type);
    }

}
