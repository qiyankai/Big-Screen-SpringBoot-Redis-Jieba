package com.datav.resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "file")
@PropertySource("classpath:file-upload-prod.properties")
public class FileUpload {
    private String imageUserFileLocation;
    private String imageServerUrl;

    public String getImageServerUrl() {
        return imageServerUrl;
    }

    public void setImageServerUrl(String imageServerUrl) {
        this.imageServerUrl = imageServerUrl;
    }

    public String getImageUserFileLocation() {
        return imageUserFileLocation;
    }

    public void setImageUserFileLocation(String imageUserFileLocation) {
        this.imageUserFileLocation = imageUserFileLocation;
    }
}
