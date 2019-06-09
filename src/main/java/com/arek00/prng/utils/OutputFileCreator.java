package com.arek00.prng.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OutputFileCreator {

    public static File createFile(final String parentDir, final String childDir, final String fileName) {
        final File dir = new File(parentDir, childDir);
        return new File(dir, fileName);
    }

    public static String withExtension(final String fileName, final String extension) {
        return fileName + "." + extension;
    }

}
