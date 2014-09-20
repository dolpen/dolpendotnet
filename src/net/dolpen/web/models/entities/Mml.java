package net.dolpen.web.models.entities;

import com.google.appengine.labs.repackaged.com.google.common.base.Strings;
import net.dolpen.gae.libs.jpa.JPAModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
public class Mml extends JPAModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Lob
    @Column
    public com.google.appengine.api.datastore.Text source;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date createAt;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date updateAt;

    public Long getId() {
        return id;
    }

    public String getSource() {
        return source.getValue();
    }

    public String getUpdateAt() {
        return updateAt.toString();
    }

    public void setSource(String source) {
        this.source = new com.google.appengine.api.datastore.Text(source);
    }

    public void edit(String source) {
        if (Strings.isNullOrEmpty(source)) return;
        setSource(source);
        updateAt = new Date();
        save();
    }

    public static Mml create(String source) {
        Mml resp = new Mml();
        resp.setSource(source);
        resp.createAt = resp.updateAt = new Date();
        resp.save();
        resp.merge();
        return resp;
    }

    public static Mml findById(Long id) {
        return find(Mml.class, "id = ?1", id).first();
    }

    public static List<Mml> findAll() {
        return all(Mml.class).fetch();
    }

    public static List<Mml> findAllOrderByLatest() {
        return find(Mml.class, "order by createAt desc").fetch(100);
    }

}
