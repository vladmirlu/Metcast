package com.synoptic.weather.database.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class WeatherImage {

    public  WeatherImage(String filename, byte[] file, String mimeType) {

        this.file = file;
        this.filename = filename;
        this.mimeType = mimeType;
    }

    public  WeatherImage() {
        // Default Constructor
    }

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private long id;

    @Column(name = "filename")
    private String filename;

    @Lob
    private byte[] file;

    @Column(name = "mime_type")
    private String mimeType;

    @Transient
    private String base64;
}
