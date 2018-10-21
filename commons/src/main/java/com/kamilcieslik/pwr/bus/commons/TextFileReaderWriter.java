package com.kamilcieslik.pwr.bus.commons;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TextFileReaderWriter {
    public static final String READ_FILE_ERROR = "READ_FILE_ERROR";
    public static final String WRITE_FILE_ERROR = "READ_FILE_ERROR";

    public String readFromFile(String filename) {
        try {
            return Files.readString(Path.of(filename));
        } catch (IOException e) {
            throw new InternalException(READ_FILE_ERROR, "Cannot read file: " + e.getMessage());
        }
    }

    public void writeToFile(String filename, String content) {
        try {
            Files.writeString(Path.of(filename), content);
        } catch (IOException e) {
            throw new InternalException(WRITE_FILE_ERROR, "Cannot save file: " + e.getMessage());
        }
    }
}
