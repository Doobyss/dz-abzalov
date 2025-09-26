package util;

import model.Person;

import java.util.Optional;

/**
 * Простейшие методы валидации входных данных (строки -> Person).
 */
public class Validator {

    /**
     * Валидирует строку и пытается разобрать Person в формате: id;name;age
     * Возвращает Optional.empty() если невалидно.
     */
    public static Optional<Person> parsePersonLine(String line) {
        if (line == null) return Optional.empty();
        String[] parts = line.split(";");
        if (parts.length != 3) return Optional.empty();
        try {
            int id = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            int age = Integer.parseInt(parts[2].trim());
            if (name.isEmpty() || age < 0 || age > 200) return Optional.empty();
            Person p = Person.builder().id(id).name(name).age(age).build();
            return Optional.of(p);
        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}

