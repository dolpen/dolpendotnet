package net.dolpen.web.controllers;

import net.dolpen.gae.libs.cache.CacheEx;
import net.dolpen.gae.libs.servlet.Forms;
import net.dolpen.web.models.entities.Text;
import net.dolpen.web.models.forms.TextForm;

import javax.servlet.http.*;
import java.util.concurrent.Callable;

/**
 * Text関連のAPI
 */
public class TextAction {
    static final String CACHE_ENTRY_KEY = "text-entry";
    static final int CACHE_DULATION = 3600;

    public String index(HttpServletRequest req, HttpServletResponse resp) {
        TextForm form = Forms.of(TextForm.class).bindFromRequest(req);
        req.setAttribute("entries", Text.findLatest(form.getPage()));
        req.setAttribute("page", form.getPage());
        return "/text/index.jsp";
    }

    public String detail(HttpServletRequest req, HttpServletResponse resp) {
        final TextForm form = Forms.of(TextForm.class).bindFromRequest(req);
        if (!form.isExistId()) return "/text/?redirect";
        CacheEx<Text> cache = new CacheEx<>(CACHE_ENTRY_KEY, CACHE_DULATION, form.id);
        Text text = cache.getOrElse(new Callable<Text>() {
            @Override
            public Text call() throws Exception {
                return Text.findById(form.id);
            }
        });
        if (text == null) return "/text/?redirect";
        req.setAttribute("entry", text);
        return "/text/detail.jsp";
    }

    public String create(HttpServletRequest req, HttpServletResponse resp) {
        return "/text/create.jsp";
    }

    public String createPost(HttpServletRequest req, HttpServletResponse resp) {
        final TextForm form = Forms.of(TextForm.class).bindFromRequest(req);
        if (!form.canCreate()) return "/text/?redirect";
        Text text = Text.create(form.title, form.source);
        CacheEx<Text> cache = new CacheEx<>(CACHE_ENTRY_KEY, CACHE_DULATION, text.getId());
        cache.set(text);
        return "/text/" + text.getId() + "?redirect";
    }

    public String edit(HttpServletRequest req, HttpServletResponse resp) {
        final TextForm form = Forms.of(TextForm.class).bindFromRequest(req);
        if (!form.isExistId()) return "/text/?redirect";
        Text text = Text.findById(form.id);
        if (text == null) return "/text/?redirect";
        req.setAttribute("entry", text);
        return "/text/edit.jsp";
    }

    public String editPost(HttpServletRequest req, HttpServletResponse resp) {
        final TextForm form = Forms.of(TextForm.class).bindFromRequest(req);
        if (!form.isExistId()) return "/text/?redirect";
        if (!form.canEdit()) return "/text/?redirect";
        Text text = Text.findById(form.id);
        if (text == null) return "/text/?redirect";
        text.edit(form.title, form.source);
        CacheEx<Text> cache = new CacheEx<>(CACHE_ENTRY_KEY, CACHE_DULATION, text.getId());
        cache.set(text);
        return "/text/" + text.getId() + "?redirect";
    }
}
