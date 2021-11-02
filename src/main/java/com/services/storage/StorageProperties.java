package com.services.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties("storage")
@Configuration
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "post-images";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}