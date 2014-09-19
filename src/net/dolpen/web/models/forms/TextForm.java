package net.dolpen.web.models.forms;

import com.google.appengine.labs.repackaged.com.google.common.base.*;

/**
 * Created by yamada on 2014/09/19.
 */
public class TextForm {

    public Integer page;
    public Long id;

    public String title;
    public String source;

    public Integer getPage() {
        return Math.max(Optional.fromNullable(page).or(1), 1);
    }

    public boolean isExistId() {
        return id != null;
    }

    public boolean canCreate() {
        return !Strings.isNullOrEmpty(title) && !Strings.isNullOrEmpty(source);
    }

    public boolean canEdit() {
        return !Strings.isNullOrEmpty(title) || !Strings.isNullOrEmpty(source);
    }
}
