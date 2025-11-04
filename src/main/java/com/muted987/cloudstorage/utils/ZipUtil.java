package com.muted987.cloudStorage.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    public static void addStreamToZip(ZipOutputStream zos, InputStream inputStream, String resourceName) throws IOException {

        ZipEntry zipEntry = new ZipEntry(resourceName);

        zos.putNextEntry(zipEntry);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            zos.write(buffer, 0, length);
        }

        zos.closeEntry();
        inputStream.close();

    }

}
