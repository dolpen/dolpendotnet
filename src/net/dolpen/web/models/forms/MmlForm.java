package net.dolpen.web.models.forms;

import com.google.appengine.labs.repackaged.com.google.common.base.*;

/**
 * Created by yamada on 2014/09/19.
 */
public class MmlForm {

    public Integer page;
    public Long id;

    public String source;

    public Integer getPage() {
        return Math.max(Optional.fromNullable(page).or(1), 1);
    }

    public boolean isExistId() {
        return id != null;
    }

    public boolean canEdit() {
        return !Strings.isNullOrEmpty(source);
    }
}
