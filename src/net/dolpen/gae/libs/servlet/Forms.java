package net.dolpen.gae.libs.servlet;

import com.google.appengine.api.datastore.Blob;
import net.dolpen.gae.libs.servlet.binder.Binders;
import net.dolpen.gae.libs.servlet.binder.TypeBinder;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;

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

    public static final long MAX_FILE_SIZE = 50 * 1024 * 1024L;

    public T bindFromMultipartReequest(HttpServletRequest req) {
        if (!ServletFileUpload.isMultipartContent(req)) {
            return bindFromRequest(req);
        }
        T obj = blankInstance();

        ServletFileUpload upload = new ServletFileUpload();
        upload.setFileSizeMax(MAX_FILE_SIZE);
        upload.setSizeMax(MAX_FILE_SIZE);
        try {
            req.setCharacterEncoding("utf-8");
            FileItemIterator itemIterator = upload.getItemIterator(req);
            while (itemIterator.hasNext()) {
                FileItemStream item = itemIterator.next();
                String key = item.getFieldName();
                InputStream stream = item.openStream();
                if (item.isFormField()) {
                    setProperty(obj, key, new String[]{Streams.asString(stream)});
                } else {
                    System.out.println("detect "+key+" as file");
                    setFile(obj, key, stream);
                }
            }
        } catch (Exception e) {
        }
        return obj;

    }

    private void setProperty(Object obj, String name, String[] values) {
        Class c = obj.getClass();
        try {
            Field f = c.getField(name);
            Class t = f.getType();
            TypeBinder tb = Binders.map.get(t);
            f.set(obj, tb.bind(values));
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
    }

    private void setFile(Object obj, String name, InputStream stream) {
        Class c = obj.getClass();
        try {
            Field f = c.getField(name);
            Class t = f.getType();
            if (t == Blob.class)
                f.set(obj, toBlob(stream));
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
    }


    private Blob toBlob(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        try {
            while (true) {
                int len = inputStream.read(buffer);
                if (len < 0) {
                    break;
                }
                baos.write(buffer, 0, len);
            }
            return new Blob(baos.toByteArray());
        } catch (Exception e) {
            return null;
        }
    }

}
