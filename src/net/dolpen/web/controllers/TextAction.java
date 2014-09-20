package net.dolpen.web.controllers;

import net.dolpen.gae.libs.cache.CacheEx;
import net.dolpen.gae.libs.servlet.Forms;
import net.dolpen.web.models.cache.TextItem;
import net.dolpen.web.models.entities.Text;
import net.dolpen.web.models.forms.TextForm;

import javax.servlet.http.*;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Text関連のAPI
 */
public class TextAction {
    static final String CACHE_TITLES_KEY = "text-index";
    static final int CACHE_TITLES_DURATION = 0;
    static final String CACHE_ENTRY_KEY = "text-entry";
    static final int CACHE_ENTRY_DURATION = 3600;

    public String index(HttpServletRequest req, HttpServletResponse resp) {
        CacheEx<List<TextItem>> cache = new CacheEx<>(CACHE_TITLES_KEY, CACHE_TITLES_DURATION);
        List<TextItem> textItems = cache.getOrElse(new Callable<List<TextItem>>() {
            @Override
            public List<TextItem> call() throws Exception {
                return TextItem.buildList(Text.findAllOrderByLatest());
            }
        });
        req.setAttribute("entries", textItems);
        return "/text/index.jsp";
    }

    public String detail(HttpServletRequest req, HttpServletResponse resp) {
        final TextForm form = Forms.of(TextForm.class).bindFromRequest(req);
        if (!form.isExistId()) return "/text/?redirect";
        CacheEx<Text> cache = new CacheEx<>(CACHE_ENTRY_KEY, CACHE_ENTRY_DURATION, form.id);
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
        if (req.getMethod().toLowerCase().equals("post")) return createPost(req,resp);
        return "/text/create.jsp";
    }

    private String createPost(HttpServletRequest req, HttpServletResponse resp) {
        final TextForm form = Forms.of(TextForm.class).bindFromRequest(req);
        if (!form.canCreate()) return "/text/?redirect";
        Text text = Text.create(form.title, form.source);
        CacheEx<Text> cache = new CacheEx<>(CACHE_ENTRY_KEY, CACHE_ENTRY_DURATION, text.getId());
        cache.set(text);
        CacheEx<List<TextItem>> cacheList = new CacheEx<>(CACHE_TITLES_KEY, CACHE_TITLES_DURATION);
        cacheList.delete();
        return "/text/" + text.getId() + "?redirect";
    }

    public String edit(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getMethod().toLowerCase().equals("post")) return editPost(req,resp);
        return editScreen(req,resp);
    }

    private String editScreen(HttpServletRequest req, HttpServletResponse resp) {
        final TextForm form = Forms.of(TextForm.class).bindFromRequest(req);
        if (!form.isExistId()) return "/text/?redirect";
        Text text = Text.findById(form.id);
        if (text == null) return "/text/?redirect";
        req.setAttribute("entry", text);
        return "/text/edit.jsp";
    }

    private String editPost(HttpServletRequest req, HttpServletResponse resp) {
        final TextForm form = Forms.of(TextForm.class).bindFromRequest(req);
        if (!form.isExistId()) return "/text/?redirect";
        if (!form.canEdit()) return "/text/?redirect";
        Text text = Text.findById(form.id);
        if (text == null) return "/text/?redirect";
        text.edit(form.title, form.source);
        CacheEx<Text> cache = new CacheEx<>(CACHE_ENTRY_KEY, CACHE_ENTRY_DURATION, text.getId());
        cache.set(text);
        CacheEx<List<TextItem>> cacheList = new CacheEx<>(CACHE_TITLES_KEY, CACHE_TITLES_DURATION);
        cacheList.delete();
        return "/text/" + text.getId() + "?redirect";
    }
}
