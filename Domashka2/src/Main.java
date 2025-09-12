import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

// Одно собственное исключение
class FileOperationException extends Exception {
    public FileOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileOperationException(String message) {
        super(message);
    }
}

public class Main {
    public static void main(String[] args) {
        Path path = Paths.get("file.txt");

        try {
            writeToFile(path, "Hello"); // теперь каждая запись идёт на новой строке
            String content = readFromFile(path);
            System.out.println("Содержимое файла:\n" + content);
        } catch (FileOperationException e) {
            System.out.println("Ошибка при работе с файлом: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Запись в файл
    public static void writeToFile(Path path, String text) throws FileOperationException {
        try {
            // Проверка существования файла и его содержимое
            if (Files.exists(path)) {
                List<String> lines = Files.readAllLines(path);
                if (lines.contains(text)) {
                    throw new FileOperationException("Файл уже содержит эту строку: \"" + text + "\"");
                }
            }

            Files.writeString(path, text + System.lineSeparator(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);

            System.out.println("Строка добавлена в файл.");
        } catch (IOException e) {
            throw new FileOperationException("Ошибка записи в файл: " + path, e);
        }
    }

    // Чтение файла
    public static String readFromFile(Path path) throws FileOperationException {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new FileOperationException("Ошибка чтения файла: " + path, e);
        }
    }
}
