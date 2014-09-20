package net.dolpen.web.models.entities;

import com.google.appengine.labs.repackaged.com.google.common.base.Strings;
import net.dolpen.gae.libs.jpa.JPAModel;
import org.markdown4j.Markdown4jProcessor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
public class Text extends JPAModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column
    public String title;

    @Lob
    @Column
    public com.google.appengine.api.datastore.Text source;

    @Lob
    @Column
    public com.google.appengine.api.datastore.Text compiled;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date createAt;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date updateAt;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCompiled() {
        return compiled.getValue();
    }

    public String getSource() {
        return source.getValue();
    }

    public String getUpdateAt() {
        return updateAt.toString();
    }


    public void setCompiled(String compiled) {
        this.compiled = new com.google.appengine.api.datastore.Text(compiled);
    }

    public void setSource(String source) {
        this.source = new com.google.appengine.api.datastore.Text(source);
    }

    public void edit(String title, String source) {
        if (!Strings.isNullOrEmpty(title)) this.title = title;
        if (!Strings.isNullOrEmpty(source)) {
            setSource(source);
            compile();
        }
        updateAt = new Date();
        save();
    }

    public void compile() {
        try {
            setCompiled(new Markdown4jProcessor().process(getSource()));
        } catch (Exception e) {
            setCompiled("");
        }
    }

    public static Text create(String title, String source) {
        Text resp = new Text();
        resp.title = title;
        resp.setSource(source);
        resp.createAt = resp.updateAt = new Date();
        resp.compile();
        resp.save();
        resp.merge();
        return resp;
    }

    public static Text findById(Long id) {
        return find(Text.class, "id = ?1", id).first();
    }

    public static List<Text> findAll() {
        return all(Text.class).fetch();
    }

    public static List<Text> findAllOrderByLatest() {
        return find(Text.class, "order by createAt desc").fetch(100);
    }

}
