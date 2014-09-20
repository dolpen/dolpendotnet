package net.dolpen.web.models.cache;

import com.google.appengine.labs.repackaged.com.google.common.base.Function;
import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import net.dolpen.web.models.entities.Mml;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class MmlItem implements Serializable {

    public Long id;
    public Date updateAt;

    public Long getId() {
        return id;
    }


    public String getUpdateAt() {
        return updateAt.toString();
    }

    public static MmlItem build(Mml mml) {
        MmlItem resp = new MmlItem();
        resp.id = mml.id;
        resp.updateAt = mml.updateAt;
        return resp;
    }

    public static List<MmlItem> buildList(List<Mml> mmlList) {
        return Lists.newArrayList(Lists.transform(mmlList, new Function<Mml, MmlItem>() {
            @Override
            public MmlItem apply(Mml mml) {
                return build(mml);
            }
        }));
    }
}
