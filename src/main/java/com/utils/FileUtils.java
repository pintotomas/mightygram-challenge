package com.utils;

public class FileUtils {

    public static String generateFileName(String fileName) {
        //Attaches timestamp to a filename
        return fileName + "-" + Dateutils.toTimeStamp(Dateutils.now());
    }
}
