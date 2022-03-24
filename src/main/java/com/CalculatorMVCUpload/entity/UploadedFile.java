package com.CalculatorMVCUpload.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "uploadedfiles")
public class UploadedFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "path")
    private String path;

    @Column(name = "url")
    private String url;

    @Column(name = "uploaddate")
    private Date uploadDate;

    @Column(name = "size")
    private long size;

    @Column(name = "hashcode")
    private int hashCode;

    public UploadedFile() {
    }

    public UploadedFile(String name, String path, String url, Date uploadDate, long size, int hashCode) {
        this.name = name;
        this.path = path;
        this.url = url;
        this.uploadDate = uploadDate;
        this.size = size;
        this.hashCode = hashCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getHashCode() {
        return hashCode;
    }

    public void setHashCode(int hashCode) {
        this.hashCode = hashCode;
    }
}