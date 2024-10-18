package com.saferent.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "tbl_imagefile")
public class ImageFile {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;

    private String type;

    private long length;

    @OneToOne(cascade = CascadeType.ALL) // File classına ne yaparsan Data classına da aynısını yapar
    private ImageData imageData;

    public ImageFile(String name, String type, ImageData imageData) {
        this.name = name;
        this.type = type;
        this.imageData = imageData;
        this.length = imageData.getData().length;
    }
}
