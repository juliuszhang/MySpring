package org.myspring.mvc;

import org.myspring.annotation.RequestMapping;
import org.myspring.bean.ClassHelper;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author yibozhang
 * @date 2020/3/29
 */
public class ControllerHelper {

    private static final Map<Request, Handler> REQUEST_MAPPING = new HashMap<>();

    static {
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        for (Class<?> controllerClass : controllerClassSet) {
            Method[] methods = controllerClass.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String url = requestMapping.value();
                    HttpMethod requestMethod = requestMapping.method();
                    Request request = new Request(requestMethod, url);
                    Handler handler = new Handler(controllerClass, method);
                    REQUEST_MAPPING.put(request, handler);
                }
            }
        }
    }

    public static Handler getHandler(HttpMethod requestMethod, String requestPath) {
        return REQUEST_MAPPING.get(new Request(requestMethod, requestPath));
    }

}
