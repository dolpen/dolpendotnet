package net.dolpen.gae.libs.servlet;

import net.dolpen.gae.libs.servlet.binder.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by yamada on 2014/09/19.
 */
public class Forms<T> {

    private Class<T> backedType;

    public static <T> Forms<T> of(Class<T> clazz) {
        Forms<T> resp = new Forms<>();
        resp.backedType = clazz;
        return resp;
    }

    private T blankInstance() {
        try {
            return backedType.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate " + backedType + ". It must have a default constructor", e);
        }
    }

    @SuppressWarnings("unchecked")
    public T bindFromRequest(HttpServletRequest req) {
        return bindFromRequest((Map<String, String[]>) req.getParameterMap());
    }

    public T bindFromRequest(Map<String, String[]> requestData) {
        T obj = blankInstance();
        for (String key : requestData.keySet()) {
            String[] values = requestData.get(key);
            setProperty(obj, key, values);
        }
        return obj;
    }

    private void setProperty(Object obj, String name, String[] values) {
        Class c = obj.getClass();
        try {
            Field f = c.getField(name);
            Class t = f.getType();
            TypeBinder tb = Binders.map.get(t);
            f.set(obj,tb.bind(values));
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
    }


}
