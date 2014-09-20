package net.dolpen.web.models.entities;

import com.google.appengine.api.datastore.Blob;
import net.dolpen.gae.libs.jpa.JPAModel;
import org.apache.commons.fileupload.FileItem;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class Image extends JPAModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column
    public Blob file;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date createAt;

    public Long getId() {
        return id;
    }

    public void setFile(FileItem fileItem) {
        file = new Blob(fileItem.get());
    }

    public byte[] getFile() {
        return file.getBytes();
    }

    public static Image create(Blob blob) {
        Image resp = new Image();
        resp.file = blob;
        resp.createAt = new Date();
        resp.save();
        resp.merge();
        return resp;
    }

    public static Image findById(Long id) {
        return find(Image.class, "id = ?1", id).first();
    }

    public static List<Image> findAll() {
        return all(Image.class).fetch();
    }

    public static List<Image> findLatest(int page) {
        return find(Image.class, "order by createAt desc").fetch(page, 5);
    }

}
