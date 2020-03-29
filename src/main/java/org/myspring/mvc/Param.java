package org.myspring.mvc;

import java.util.Collections;
import java.util.Map;

/**
 * @author yibozhang
 * @date 2020/3/29
 */
public class Param {

    private Map<String, Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public long getLong(String name) {
        return (long) paramMap.get(name);
    }

    public Map<String, Object> getMap() {
        return Collections.unmodifiableMap(paramMap);
    }
}
