package org.myspring.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author yibozhang
 * @date 2020/3/29
 */
public class AspectProxy implements Proxy {

    private static final Logger LOG = LoggerFactory.getLogger(AspectProxy.class);

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Class<?> targetClass = proxyChain.getTargetClass();
        Method targetMethod = proxyChain.getTargetMethod();
        Object[] methodParams = proxyChain.getMethodParams();
        begin();
        Object result = null;
        try {
            if (intercept(targetClass, targetMethod, methodParams)) {
                before(targetClass, targetMethod, methodParams);
                result = proxyChain.doProxyChain();
                after(targetClass, targetMethod, methodParams, result);
            } else {
                result = proxyChain.doProxyChain();
            }
        } catch (Throwable t) {
            LOG.error("do proxy failure.", t);
            error(targetClass, targetMethod, methodParams, result);
            throw t;
        } finally {
            end();
        }
        return result;
    }

    public void begin() {

    }

    public boolean intercept(Class<?> cls, Method method, Object[] params) {
        return true;
    }

    public void before(Class<?> cls, Method method, Object[] params) {

    }

    public void after(Class<?> cls, Method method, Object[] params, Object result) {

    }

    public void end() {

    }

    public void error(Class<?> cls, Method method, Object[] params, Object result) {

    }
}
