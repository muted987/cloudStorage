package com.muted987.cloudStorage.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PathUtil {

    private final static String BASE_USER_FOLDER_NAME = "user-%s-files/";

    public static String formatPath(int id, String... paths) {
        String result = BASE_USER_FOLDER_NAME.formatted(id);
        for (String path : paths) {
            result = result.concat(path);
        }
        return result;
    }

    public static String reformatPath(String path) {
        return path.substring(path.indexOf("/") + 1);
    }


    /*
        ПЕРЕДАВАТЬ БЕЗ ФОРМАТИРОВАНИЯ
     */
    public static String getParentPath(String path) {
        if ((path.length() - path.replace("/", "").length() == 1 && path.endsWith("/")) || !path.contains("/")) {
            return "";
        }
        if (path.endsWith("/")) {
            String pathWithoutLastSlash = path.substring(0, path.lastIndexOf("/"));
            return path.substring(0, pathWithoutLastSlash.lastIndexOf("/") + 1);
        }
        return path.substring(0, path.lastIndexOf("/") + 1);
    }

    /*
        ПЕРЕДАВАТЬ БЕЗ ФОРМАТИРОВАНИЯ
     */
    public static List<String> getParentPaths(String path) {
        List<String> paths = new ArrayList<>();
        while (!path.isEmpty()) {
            paths.add(path);
            path = getParentPath(path);
        }
        Collections.reverse(paths);
        return paths;
    }
}
