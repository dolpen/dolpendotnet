package net.dolpen.web.controllers;

import net.dolpen.gae.libs.cache.CacheEx;
import net.dolpen.gae.libs.servlet.Forms;
import net.dolpen.gae.libs.utils.TimeUtils;
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
    static final int CACHE_DULATION = 60 * 60 * 24;

    public String index(HttpServletRequest req, HttpServletResponse resp) {
        if (!req.getMethod().toLowerCase().equals("post")) return "";
        return upload(req, resp);
    }

    private String upload(HttpServletRequest req, HttpServletResponse resp) {
        final GyazoForm form = Forms.of(GyazoForm.class).bindFromMultipartReequest(req);
        if (form.imagedata == null) {
            System.out.println("no image");
            return "";
        }
        Image image = Image.create(form.imagedata);
        CacheEx<Image> cache = new CacheEx<>(CACHE_ENTRY_KEY, CACHE_DULATION, form.id);
        cache.set(image);
        try {
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("text/plain");
            resp.getWriter().println("http://www.dolpen.net/gyazo/" + image.id);
        } catch (IOException e) {
            e.printStackTrace();
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
            resp.setHeader("Last-Modified", TimeUtils.toRFC1123Format(image.createAt, 0));
            resp.setHeader("Expires", TimeUtils.toRFC1123Format(image.createAt, CACHE_DULATION * 1000));
            resp.setHeader("Cache-Control", "public, max-age=" + CACHE_DULATION);
            resp.getOutputStream().write(image.getFile());
            resp.flushBuffer();
        } catch (IOException e) {
        }
        return "";
    }
}
