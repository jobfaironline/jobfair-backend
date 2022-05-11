package org.capstone.job_fair.utils;

import java.util.Optional;

public class FileUtil {
    public static String getExtensionByStringHandling(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1)).orElse("");
    }
}
