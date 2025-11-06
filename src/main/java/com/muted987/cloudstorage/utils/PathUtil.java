package com.muted987.cloudStorage.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PathUtil {

    private final static String BASE_USER_FOLDER_NAME = "user-%s-files/";

    public static String formatPath(int id, String... paths) {
        String result = BASE_USER_FOLDER_NAME.formatted(id);
        for (String path : paths) {
            result = result.concat(path);
        }
        return result;
    }


    /*
        Path - formatted path
        objectName - formatted path
        Извлекает из пути необходимые данные
        А именно путь и название файла.
        Результат - Map<String, String> с двумя ключами path, name
     */
    public static Map<String, String> extractData(String path, String objectName) {
        Map<String, String> result = new HashMap<>();
        String pathWithoutBaseFolderName = reformatPath(path);
        String resultName = objectName.substring(path.length());
        result.put("path", pathWithoutBaseFolderName);
        result.put("name", resultName);
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
