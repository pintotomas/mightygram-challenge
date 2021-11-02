package com.utils;

public class FileUtils {

    public static String generateFileName(String fileName) {
        return fileName + "-" + Dateutils.toTimeStamp(Dateutils.now());
    }
}
