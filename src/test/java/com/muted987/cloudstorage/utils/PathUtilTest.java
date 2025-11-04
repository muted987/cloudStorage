package com.muted987.cloudStorage.utils;

import com.muted987.cloudStorage.exception.UtilException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PathUtilTest {

    @Test
    void extractDataTest() {

        String testPath = "user-123-files/folder1/folder2/filename.txt";
        String requestPath = "user-123-files/folder1/folder2/";
        String exceptedPath = "folder1/folder2/";
        String exceptedName = "filename.txt";

        Map<String, String> result = PathUtil.extractData(requestPath, testPath);
        System.out.println(result.get("name"));
        System.out.println(result.get("path"));

        assertTrue(exceptedPath.equals(result.get("path")) && exceptedName.equals(result.get("name")));

    }

    @Test
    void getParentPathTest(){

        String testPath = "folder1/filename.txt";
        String exceptedPath = "folder1/";

        System.out.println("testPath = " + testPath);
        String result = PathUtil.getParentPath(testPath);
        System.out.println("result = " + result);

        assertEquals(exceptedPath, result);

    }

    @Test
    void getParentPathTest_withNotValidPath_shouldReturnEmptyString(){

        String testPath = "folder1/";

        String result = PathUtil.getParentPath(testPath);
        System.out.println("testPath = " + testPath);
        System.out.println("result = " + result);

        assertTrue(result.isEmpty());
    }

    @Test
    void getParentPathsTest(){

        String testPath = "folder1/folder2/folder3/";
        List<String> exceptedList = new ArrayList<>(List.of("folder1/folder2/folder3/", "folder1/folder2/", "folder1/"));

        List<String> result = PathUtil.getParentPaths(testPath);
        result.forEach(System.out::println);

        assertEquals(exceptedList, result);

    }
}