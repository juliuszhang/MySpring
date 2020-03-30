package org.myspring.jdbc;

import org.myspring.annotation.Transaction;
import org.myspring.aspect.Proxy;
import org.myspring.aspect.ProxyChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author yibozhang
 * @date 2020/3/30 10:22
 */
public class TransactionProxy implements Proxy {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionProxy.class);

    private static final ThreadLocal<Boolean> FLAG_HOLDER = ThreadLocal.withInitial(() -> Boolean.FALSE);

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Boolean flag = FLAG_HOLDER.get();
        Method targetMethod = proxyChain.getTargetMethod();
        if (!flag && targetMethod.isAnnotationPresent(Transaction.class)) {
            try {
                FLAG_HOLDER.set(Boolean.TRUE);
                DataBaseHelper.begin();
                LOG.debug("begin transaction");
                Object result = proxyChain.doProxyChain();
                DataBaseHelper.commit();
                LOG.debug("commit transaction");
                return result;
            } catch (Throwable t) {
                Transaction transactionAnn = targetMethod.getAnnotation(Transaction.class);
                if (needRollback(t, transactionAnn)) {
                    DataBaseHelper.rollback();
                    LOG.debug("rollback transaction", t);
                } else {
                    LOG.debug("execute method exception but not rollback.", t);
                }
                throw t;
            } finally {
                FLAG_HOLDER.remove();
            }
        } else {
            return proxyChain.doProxyChain();
        }
    }

    private boolean needRollback(Throwable t, Transaction transaction) {
        Class<? extends Throwable> rollbackFor = transaction.rollbackFor();
        return rollbackFor.isAssignableFrom(t.getClass());
    }
}
