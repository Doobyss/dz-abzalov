import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Student> students = List.of(
                new Student("Алиса", List.of(
                        new Book("Book A1", 200, 2001),
                        new Book("Book A2", 150, 1999),
                        new Book("Book A3", 300, 2010),
                        new Book("Book A4", 120, 2005),
                        new Book("Book A5", 220, 2018)
                )),
                new Student("Валера", List.of(
                        new Book("Book B1", 180, 2002),
                        new Book("Book B2", 250, 2015),
                        new Book("Book B3", 200, 1995),
                        new Book("Book B4", 300, 2003),
                        new Book("Book B5", 100, 2020)
                ))
        );

        students.stream()
                .peek(System.out::println) // Вывести студентов
                .flatMap(s -> s.getBooks().stream()) // Получить список книг каждого студента
                .sorted(Comparator.comparingInt(Book::getPages)) // Сортировка по страницам
                .distinct() // Только уникальные книги
                .filter(book -> book.getYear() > 2000) // Только книги после 2000 года
                .limit(3) // Ограничить 3 элементами
                .map(Book::getYear) // Получить годы выпуска
                .findFirst() // Короткое замыкание: получаем Optional
                .ifPresentOrElse(
                        year -> System.out.println("Год выпуска найденной книги: " + year),
                        () -> System.out.println("Книга отсутствует")
                );
    }
}
