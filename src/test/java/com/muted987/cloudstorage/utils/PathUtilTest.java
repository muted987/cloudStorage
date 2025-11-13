package com.muted987.cloudStorage.utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PathUtilTest {

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
        List<String> exceptedList = new ArrayList<>(List.of("folder1/", "folder1/folder2/", "folder1/folder2/folder3/"));

        List<String> result = PathUtil.getParentPaths(testPath);
        result.forEach(System.out::println);

        assertEquals(exceptedList, result);

    }
}