package net.dolpen.web.controllers;

import net.dolpen.gae.libs.cache.CacheEx;
import net.dolpen.gae.libs.servlet.Forms;
import net.dolpen.web.models.cache.MmlItem;
import net.dolpen.web.models.entities.Mml;
import net.dolpen.web.models.forms.MmlForm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * MML関連のAPI
 */
public class MmlAction {

    static final String CACHE_TITLES_KEY = "mml-index";
    static final int CACHE_TITLES_DURATION = 0;
    static final String CACHE_ENTRY_KEY = "mml-entry";
    static final int CACHE_ENTRY_DURATION = 3600;

    public String index(HttpServletRequest req, HttpServletResponse resp) {
        MmlForm form = Forms.of(MmlForm.class).bindFromRequest(req);
        req.setAttribute("entries", Mml.findLatest(form.getPage()));
        req.setAttribute("page", form.getPage());
        return "/mml/index.jsp";
    }

    public String detail(HttpServletRequest req, HttpServletResponse resp) {
        final MmlForm form = Forms.of(MmlForm.class).bindFromRequest(req);
        if (!form.isExistId()) return "/mml/?redirect";
        CacheEx<Mml> cache = new CacheEx<>(CACHE_ENTRY_KEY, CACHE_ENTRY_DURATION, form.id);
        Mml mml = cache.getOrElse(new Callable<Mml>() {
            @Override
            public Mml call() throws Exception {
                return Mml.findById(form.id);
            }
        });
        if (mml == null) return "/mml/?redirect";
        req.setAttribute("entry", mml);
        return "/mml/detail.jsp";
    }

    public String create(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getMethod().toLowerCase().equals("post")) return createPost(req, resp);
        return "/mml/create.jsp";
    }

    private String createPost(HttpServletRequest req, HttpServletResponse resp) {
        final MmlForm form = Forms.of(MmlForm.class).bindFromRequest(req);
        if (!form.canEdit()) return "/mml/?redirect";
        Mml mml = Mml.create(form.source);
        CacheEx<Mml> cache = new CacheEx<>(CACHE_ENTRY_KEY, CACHE_ENTRY_DURATION, mml.getId());
        cache.set(mml);
        CacheEx<List<MmlItem>> cacheList = new CacheEx<>(CACHE_TITLES_KEY, CACHE_TITLES_DURATION);
        cacheList.delete();
        return "/mml/" + mml.getId() + "?redirect";
    }

    public String edit(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getMethod().toLowerCase().equals("post")) return editPost(req, resp);
        return editScreen(req, resp);
    }

    private String editScreen(HttpServletRequest req, HttpServletResponse resp) {
        final MmlForm form = Forms.of(MmlForm.class).bindFromRequest(req);
        if (!form.isExistId()) return "/mml/?redirect";
        Mml mml = Mml.findById(form.id);
        if (mml == null) return "/mml/?redirect";
        req.setAttribute("entry", mml);
        return "/mml/edit.jsp";
    }

    private String editPost(HttpServletRequest req, HttpServletResponse resp) {
        final MmlForm form = Forms.of(MmlForm.class).bindFromRequest(req);
        if (!form.isExistId()) return "/mml/?redirect";
        if (!form.canEdit()) return "/mml/?redirect";
        Mml mml = Mml.findById(form.id);
        if (mml == null) return "/mml/?redirect";
        mml.edit(form.source);
        CacheEx<Mml> cache = new CacheEx<>(CACHE_ENTRY_KEY, CACHE_ENTRY_DURATION, mml.getId());
        cache.set(mml);
        return "/mml/" + mml.getId() + "?redirect";
    }
}
