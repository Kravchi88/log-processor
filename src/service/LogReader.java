package service;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class LogReader {

    public static List<String> readAllLogLines(Path directory) throws IOException {
        List<String> result = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*.log")) {
            for (Path file : stream) {
                List<String> lines = Files.readAllLines(file);
                result.addAll(lines);
            }
        }

        return result;
    }
}