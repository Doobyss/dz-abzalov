package util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Простая утилита для чтения/записи. Поддерживает режим append.
 */
public class FileUtil {

    public static List<String> readLines(Path path) throws IOException {
        if (!Files.exists(path)) return new ArrayList<>();
        return Files.readAllLines(path);
    }

    /**
     * Запись в файл в режиме добавления (append).
     */
    public static void appendLines(Path path, List<String> lines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile(), true))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    public static void appendLine(Path path, String line) throws IOException {
        appendLines(path, List.of(line));
    }
}
