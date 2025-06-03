package com.farmflow.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum FileType {
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    VIDEO_MP4("video/mp4"),
    VIDEO_MOV("video/quicktime");

    private final String mimeType;

    FileType(String mimeType) {
        this.mimeType = mimeType;
    }
    public static FileType fromMimeType(String mimeType) {
        return Arrays.stream(values())
                .filter(type -> type.mimeType.equalsIgnoreCase(mimeType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid file type: " + mimeType));
    }
}