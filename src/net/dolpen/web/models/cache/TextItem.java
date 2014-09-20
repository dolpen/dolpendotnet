package net.dolpen.web.models.cache;

import com.google.appengine.labs.repackaged.com.google.common.base.Function;
import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import net.dolpen.web.models.entities.Text;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class TextItem implements Serializable {

    public Long id;
    public String title;
    public Date updateAt;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUpdateAt() {
        return updateAt.toString();
    }

    public static TextItem build(Text text) {
        TextItem resp = new TextItem();
        resp.id = text.id;
        resp.title = text.title;
        resp.updateAt = text.updateAt;
        return resp;
    }

    public static List<TextItem> buildList(List<Text> textList) {
        return Lists.newArrayList(Lists.transform(textList, new Function<Text, TextItem>() {
            @Override
            public TextItem apply(Text text) {
                return build(text);
            }
        }));
    }
}
