package com.validations.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.MediaType;

@Getter
@AllArgsConstructor
public enum FileType {
    IMAGE_JPEG(MediaType.IMAGE_JPEG_VALUE),
    IMAGE_PNG(MediaType.IMAGE_PNG_VALUE);

    private final String mediaType;
}