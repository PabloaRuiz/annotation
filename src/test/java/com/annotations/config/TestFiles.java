package com.annotations.config;

import io.restassured.internal.util.IOUtils;
import lombok.RequiredArgsConstructor;

import java.io.FileInputStream;
import java.io.IOException;

@RequiredArgsConstructor
public enum TestFiles {

    ANNOTATION_REQUEST("src/test/jsonTest/annotation.json"),
    DESCRIPTION_JSON("src/test/jsonTest/description.json");

    private final String pathFile;

    public byte[] load() {
        try {
            var resource = new FileInputStream(pathFile);
            return IOUtils.toByteArray(resource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
