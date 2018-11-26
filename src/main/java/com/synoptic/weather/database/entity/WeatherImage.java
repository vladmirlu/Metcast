package com.synoptic.weather.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "weather_images")
public class WeatherImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
