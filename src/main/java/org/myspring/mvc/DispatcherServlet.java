package org.myspring.mvc;

import org.apache.commons.lang3.StringUtils;
import org.myspring.HelperLoader;
import org.myspring.bean.BeanHelper;
import org.myspring.config.ConfigHelper;
import org.myspring.util.CodecUtil;
import org.myspring.util.JSONUtil;
import org.myspring.util.ReflectionUtil;
import org.myspring.util.StreamUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yibozhang
 * @date 2020/3/29
 */
@WebServlet(urlPatterns = "/", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        HelperLoader.init();
        ServletContext servletContext = servletConfig.getServletContext();
        //jsp
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getJspPath() + "*");
        //静态资源
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAssetPath() + "*");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        String path = req.getServletPath();
        Handler handler = ControllerHelper.getHandler(HttpMethod.parse(method), path);
        if (handler == null) return;
        Class<?> controllerClass = handler.getControllerClass();
        Object controllerBean = BeanHelper.getBean(controllerClass);
        Enumeration<String> paramNames = req.getParameterNames();
        Map<String, Object> paramMap = new HashMap<>();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = req.getParameter(paramName);
            paramMap.put(paramName, paramValue);
        }

        String body = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
        if (!StringUtils.isEmpty(body)) {
            String[] params = body.split("&");
            for (String param : params) {
                String[] paramPair = param.split("=");
                String name = paramPair[0];
                String value = paramPair[1];
                paramMap.put(name, value);
            }
        }

        Param param = new Param(paramMap);
        Method requestMethod = handler.getRequestMethod();
        Object result = ReflectionUtil.invokeMethod(controllerBean, requestMethod, param);
        if (result instanceof View) {
            View view = (View) result;
            String viewPath = view.getPath();
            if (StringUtils.isNotEmpty(path)) {
                if (viewPath.startsWith("/")) {
                    resp.sendRedirect(req.getContextPath() + path);
                } else {
                    Map<String, Object> model = view.getModel();
                    for (Map.Entry<String, Object> modelEntry : model.entrySet()) {
                        req.setAttribute(modelEntry.getKey(), modelEntry.getValue());
                    }
                }
            }
        } else {
            Data data = (Data) result;
            Object model = data.getModel();
            if (model != null) {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                PrintWriter writer = resp.getWriter();
                String json = JSONUtil.toJson(model);
                writer.write(json);
                writer.flush();
                writer.close();
            }
        }
    }
}
