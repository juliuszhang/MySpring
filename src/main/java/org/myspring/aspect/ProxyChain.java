package org.myspring.aspect;

import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yibozhang
 * @date 2020/3/29
 */
public class ProxyChain {

    private static final Logger LOG = LoggerFactory.getLogger(ProxyChain.class);

    private final Class<?> targetClass;

    private final Object targetObject;

    private final Method targetMethod;

    private final MethodProxy methodProxy;

    private final Object[] methodParams;

    private List<Proxy> proxyList = new ArrayList<>();

    private int proxyIndex = 0;

    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetMethod = targetMethod;
        this.targetObject = targetObject;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyList = proxyList;

    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public Object doProxyChain() throws Throwable {
        Object methodResult;
        if (proxyIndex < proxyList.size()) {
            methodResult = proxyList.get(proxyIndex++).doProxy(this);
        } else {
            methodResult = methodProxy.invokeSuper(targetObject, methodParams);
        }
        return methodResult;
    }

}
