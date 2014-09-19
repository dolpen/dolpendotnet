package net.dolpen.gae.libs.servlet;

import com.google.appengine.labs.repackaged.com.google.common.base.Strings;
import net.dolpen.gae.libs.utils.CaseConverter;

import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

@SuppressWarnings("rawtypes")
public class DolpenRequestProcessor extends HttpServlet {
    private static final long serialVersionUID = -1740509020012914425L;

    private static final String BASE_PACKAGE = "net.dolpen.web.controllers";

    private final static Class[] ARGS = {
            HttpServletRequest.class,
            HttpServletResponse.class
    };

    /**
     * メインメソッドです。
     */
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        doGet(req, resp);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            doResp(req, resp);
        } catch (Exception e) {
            Forwards.forwardError(503, req, resp);
        }
    }

    /**
     * パスからActionを特定します
     *
     * @param pathInfo
     */
    public String[] processPathInfo(String pathInfo) {
        if (pathInfo.endsWith("/")) {
            pathInfo = pathInfo.concat("index");
        }
        String[] str = pathInfo.split("/");
        String pkg = BASE_PACKAGE;
        String cls = "IndexAction";
        String method = "index";
        String path = "";
        for (int i = 0; i < str.length - 2; i++) {
            if (!Strings.isNullOrEmpty(str[i])) {
                pkg = pkg.concat(".".concat(str[i].toLowerCase()));
                path = path.concat("/".concat(str[i].toLowerCase()));
            }
        }
        if (str.length >= 2 && !Strings.isNullOrEmpty(str[str.length - 2])) {
            cls = CaseConverter.snakeToUpperCamel(str[str.length - 2]).concat("Action");
            path = path.concat("/".concat(str[str.length - 2].toLowerCase()));
        } else {
            path = path.concat("/index");
        }
        if (str.length >= 1 && !Strings.isNullOrEmpty(str[str.length - 1])) {
            method = str[str.length - 1];
        }
        return new String[]{pkg, cls, method, path};
    }

    /**
     * 全てのリクエストはここを通ります
     *
     * @param req
     * @param obj
     * @param clazz
     */
    private void doAfter(HttpServletRequest req, Object obj, Class clazz) {
        List<Field> fieldList = Arrays.asList(clazz.getFields());
        for (Field field : fieldList) {
            try {
                req.setAttribute(field.getName(), field.get(obj));
            } catch (Exception e) {
            }
        }
    }

    /**
     * 全てのリクエストはここを通ります
     *
     * @param req
     * @param resp
     */
    @SuppressWarnings("unchecked")
    public String doExec(HttpServletRequest req, HttpServletResponse resp) {
        String[] str = processPathInfo(Strings.nullToEmpty(req.getServletPath() + req.getPathInfo()));
        String ret = "";
        try {
            Class clazz = Class.forName(str[0] + "." + str[1]);
            Object obj = clazz.newInstance();
            ret = (String) clazz.getMethod(str[2], ARGS).invoke(obj, req, resp);
            doAfter(req, obj, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("NotFound :: " + str[0] + "." + str[1] + "#" + str[2]);
        }
        if (!Strings.isNullOrEmpty(ret) && !ret.startsWith("/")) {
            ret = str[3].concat("/".concat(ret));
        }
        //System.err.println(str[0] + "." + str[1] + "#" + str[2] + " :: " + ret);
        return ret;
    }

    /**
     * 全てのリクエストはここを通ります
     *
     * @param req
     * @param resp
     */
    @SuppressWarnings("unchecked")
    public void doResp(HttpServletRequest req, HttpServletResponse resp) {
        String forward = doExec(req, resp);
        Iterator<Entry<String, String[]>> iter = req.getParameterMap().entrySet().iterator();
        while (iter.hasNext()) {
            Entry<String, String[]> e = iter.next();
            if (e.getValue().length == 1) {
                req.setAttribute(e.getKey(), e.getValue()[0]);
            } else {
                req.setAttribute(e.getKey(), e.getValue());
            }
        }
        if (Strings.isNullOrEmpty(forward))
            return;
        if (forward.endsWith("?redirect")) {
            Forwards.redirect(forward.split("\\?redirect")[0], resp);
        }
        if (forward.endsWith(".jsp")) {
            Forwards.forward(forward, req, resp);
        }
    }
}