package org.myspring.aspect;

/**
 * @author yibozhang
 * @date 2020/3/29
 */
public interface Proxy {

    Object doProxy(ProxyChain proxyChain) throws Throwable;

}
