package net.dolpen.web.controllers;

import net.dolpen.gae.libs.cache.CacheEx;
import net.dolpen.gae.libs.servlet.Forms;
import net.dolpen.web.models.entities.Image;
import net.dolpen.web.models.forms.GyazoForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Gyazo関連のAPI
 */
public class GyazoAction {
    static final String CACHE_ENTRY_KEY = "gyazo-entry";
    static final int CACHE_DULATION = 3600;

    public String index(HttpServletRequest req, HttpServletResponse resp) {
        if (!req.getMethod().toLowerCase().equals("post")) return upload(req, resp);
        return "";
    }

    private String upload(HttpServletRequest req, HttpServletResponse resp) {
        final GyazoForm form = Forms.of(GyazoForm.class).bindFromMultipartReequest(req);
        if (form.imagedata == null) {
            return "";
        }
        Image image = Image.create(form.imagedata);
        CacheEx<Image> cache = new CacheEx<>(CACHE_ENTRY_KEY, CACHE_DULATION, form.id);
        cache.set(image);
        try {
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("text/plain");
            resp.getWriter().write( "http://dolpendotnet.appspot.com/gyazo/" + image.id);
            resp.flushBuffer();
        } catch (IOException e) {
        }
        return "";
    }

    public String download(HttpServletRequest req, HttpServletResponse resp) {
        final GyazoForm form = Forms.of(GyazoForm.class).bindFromRequest(req);
        if (form.id == null) return "";
        CacheEx<Image> cache = new CacheEx<>(CACHE_ENTRY_KEY, CACHE_DULATION, form.id);
        Image image = cache.getOrElse(new Callable<Image>() {
            @Override
            public Image call() throws Exception {
                return Image.findById(form.id);
            }
        });
        if (image == null) return "";
        try {
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("image/png");
            resp.getOutputStream().write(image.getFile());
            resp.flushBuffer();
        } catch (IOException e) {
        }
        return "";
    }
}
