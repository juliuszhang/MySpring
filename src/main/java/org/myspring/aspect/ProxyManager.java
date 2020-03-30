package org.myspring.aspect;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.util.List;

/**
 * @author yibozhang
 * @date 2020/3/29
 */
public class ProxyManager {

    public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList) {
        return (T) Enhancer.create(targetClass, (MethodInterceptor) (targetObject, targetMethod, params, methodProxy) ->
                new ProxyChain(targetClass, targetObject, targetMethod, methodProxy, params, proxyList).doProxyChain());
    }

}
