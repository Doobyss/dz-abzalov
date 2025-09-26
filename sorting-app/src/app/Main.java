package app;

import collection.CustomList;
import model.Person;
import search.BinarySearch;
import special.ParitySorter;
import strategy.MergeSortStrategy;
import strategy.QuickSortStrategy;
import strategy.SortStrategy;
import util.CountOccurrences;
import util.FileUtil;
import util.Validator;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Простой консольный UI — программа работает в цикле до выбора выхода пользователем.
 * Поддерживает:
 *  - заполнение коллекции: из файла, рандом, вручную
 *  - выбор длины
 *  - выбор алгоритма сортировки (merge parallel, quick)
 *  - поиск бинарным поиском
 *  - запись результатов в файл (append)
 *  - parity-sort по id
 *  - подсчёт вхождений в несколько потоков
 *
 * В интересах компактности — минимальная реализация CLI.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Path DEFAULT_SAVE = Path.of("sorted_output.txt");

    public static void main(String[] args) {
        System.out.println("=== Sorting App ===");
        CustomList<Person> customList = new CustomList<>();
        boolean running = true;
        while (running) {
            try {
                System.out.println("\nВыберите действие:");
                System.out.println("1) Заполнить коллекцию");
                System.out.println("2) Показать коллекцию");
                System.out.println("3) Отсортировать (выбрать алгоритм)");
                System.out.println("4) Parity sort (even values by id sorted, odd remain)");
                System.out.println("5) Бинарный поиск");
                System.out.println("6) Записать коллекцию в файл (append)");
                System.out.println("7) Подсчитать вхождения элемента (параллельно)");
                System.out.println("8) Запустить простые тесты");
                System.out.println("0) Выход");
                System.out.print("Ваш выбор: ");
                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1" -> customList = fillMenu();
                    case "2" -> showCollection(customList);
                    case "3" -> sortMenu(customList);
                    case "4" -> parityMenu(customList);
                    case "5" -> binarySearchMenu(customList);
                    case "6" -> writeToFileMenu(customList);
                    case "7" -> countOccurrencesMenu(customList);
                    case "8" -> runSimpleTests();
                    case "0" -> running = false;
                    default -> System.out.println("Неверный выбор.");
                }
            } catch (Exception ex) {
                System.err.println("Ошибка: " + ex.getMessage());
                ex.printStackTrace(System.err);
            }
        }
        System.out.println("Выход.");
    }

    private static CustomList<Person> fillMenu() {
        System.out.println("Выберите способ заполнения:");
        System.out.println("1) Из файла (каждая строка: id;name;age)");
        System.out.println("2) Рандом");
        System.out.println("3) Вручную");
        System.out.print("Ваш выбор: ");
        String c = scanner.nextLine().trim();
        CustomList<Person> list = new CustomList<>();
        switch (c) {
            case "1" -> {
                System.out.print("Путь к файлу: ");
                String path = scanner.nextLine().trim();
                try {
                    List<String> lines = FileUtil.readLines(Path.of(path));
                    for (String line : lines) {
                        Validator.parsePersonLine(line).ifPresent(list::add);
                    }
                } catch (Exception ex) {
                    System.err.println("Не удалось прочитать файл: " + ex.getMessage());
                }
            }
            case "2" -> {
                int n = askForLength();
                Random rnd = new Random();
                // Используем стримы для заполнения
                List<Person> generated = IntStream.range(0, n)
                        .mapToObj(i -> Person.builder()
                                .id(rnd.nextInt(1_000_000))
                                .name("Name" + rnd.nextInt(1000))
                                .age(rnd.nextInt(100))
                                .build())
                        .collect(Collectors.toList());
                generated.forEach(list::add);
                System.out.println("Сгенерировано " + n + " элементов.");
            }
            case "3" -> {
                int n = askForLength();
                for (AtomicInteger i = new AtomicInteger(); i.get() < n; i.getAndIncrement()) {
                    System.out.print("Введите строку (id;name;age): ");
                    String line = scanner.nextLine();
                    Validator.parsePersonLine(line).ifPresentOrElse(
                            list::add,
                            () -> {
                                System.out.println("Строка невалидна, повторите ввод.");
                                i.getAndDecrement();
                            }
                    );
                }
            }
            default -> System.out.println("Неверно.");
        }
        return list;
    }

    private static int askForLength() {
        while (true) {
            System.out.print("Длина коллекции (положительное число): ");
            String s = scanner.nextLine().trim();
            try {
                int n = Integer.parseInt(s);
                if (n >= 0) return n;
            } catch (NumberFormatException ignored) {}
            System.out.println("Неверное число.");
        }
    }

    private static void showCollection(CustomList<Person> list) {
        System.out.println("Коллекция (size=" + list.size() + "):");
        int i = 0;
        for (Person p : list) {
            System.out.println(i++ + ": " + p);
        }
    }

    private static void sortMenu(CustomList<Person> customList) {
        if (customList == null || customList.size() == 0) {
            System.out.println("Коллекция пустая.");
            return;
        }
        System.out.println("Выберите сортировку:");
        System.out.println("1) Merge sort (parallel)");
        System.out.println("2) Quick sort (single-thread)");
        System.out.print("Ваш выбор: ");
        String c = scanner.nextLine().trim();
        SortStrategy<Person> strategy;
        switch (c) {
            case "1" -> {
                System.out.print("Размер пула потоков для сортировки (min 2): ");
                int threads = Integer.parseInt(scanner.nextLine().trim());
                strategy = new MergeSortStrategy<>(threads);
            }
            case "2" -> strategy = new QuickSortStrategy<>();
            default -> {
                System.out.println("Неверный выбор."); return;
            }
        }
        // Выбор поля сортировки
        System.out.println("Поле для сортировки: 1) name 2) age 3) id");
        String field = scanner.nextLine().trim();
        Comparator<Person> comp = switch (field) {
            case "1" -> Comparator.comparing(Person::getName).thenComparing(Person::getAge).thenComparing(Person::getId);
            case "2" -> Comparator.comparingInt(Person::getAge).thenComparing(Person::getName).thenComparingInt(Person::getId);
            case "3" -> Comparator.comparingInt(Person::getId).thenComparing(Person::getName).thenComparingInt(Person::getAge);
            default -> null;
        };
        List<Person> sorted = strategy.sort(customList.toList(), comp);
        System.out.println("Результат (первые 30):");
        for (int i = 0; i < Math.min(sorted.size(), 30); i++) System.out.println(i + ": " + sorted.get(i));
        System.out.print("Сохранить результат как основной список? (y/n): ");
        if ("y".equalsIgnoreCase(scanner.nextLine().trim())) {
            // заменить содержимое customList
            customList.clear();
            sorted.forEach(customList::add);
            System.out.println("Заменено.");
        }
    }

    private static void parityMenu(CustomList<Person> customList) {
        if (customList == null || customList.size() == 0) { System.out.println("Пусто."); return;}
        ParitySorter<Person> ps = new ParitySorter<>();
        // извлекаем по id
        ToIntFunction<Person> extractor = Person::getId;
        // в качестве comparator используем natural (Comparable implemented in Person)
        List<Person> original = customList.toList();
        List<Person> result = ps.paritySort(original, extractor, null);
        System.out.println("Parity-sorted (even-id sorted; odd-id in-place). Первые 30:");
        for (int i = 0; i < Math.min(result.size(), 30); i++) System.out.println(i + ": " + result.get(i));
        System.out.print("Заменить основной список? (y/n): ");
        if ("y".equalsIgnoreCase(scanner.nextLine().trim())) {
            customList.clear();
            result.forEach(customList::add);
            System.out.println("Заменено.");
        }
    }

    private static void binarySearchMenu(CustomList<Person> customList) {
        if (customList == null || customList.size() == 0) { System.out.println("Пусто."); return; }
        System.out.println("Бинарный поиск: коллекция должна быть отсортирована по тем же критериям.");
        System.out.print("Введите элемент для поиска (id;name;age): ");
        String line = scanner.nextLine();
        Optional<Person> maybe = Validator.parsePersonLine(line);
        if (maybe.isEmpty()) { System.out.println("Неверный ввод."); return; }
        Person key = maybe.get();
        System.out.println("Выберите компаратор (тот же, что использовался при сортировке): 1)name 2)age 3)id");
        String f = scanner.nextLine().trim();
        Comparator<Person> comp = switch (f) {
            case "1" -> Comparator.comparing(Person::getName).thenComparing(Person::getAge).thenComparing(Person::getId);
            case "2" -> Comparator.comparingInt(Person::getAge).thenComparing(Person::getName).thenComparingInt(Person::getId);
            case "3" -> Comparator.comparingInt(Person::getId).thenComparing(Person::getName).thenComparingInt(Person::getAge);
            default -> null;
        };
        int idx = BinarySearch.binarySearch(customList.toList(), key, comp);
        if (idx >= 0) System.out.println("Найдено на индексе " + idx + ": " + customList.get(idx));
        else System.out.println("Не найдено.");
    }

    private static void writeToFileMenu(CustomList<Person> customList) {
        System.out.print("Путь файла (ENTER для default 'sorted_output.txt'): ");
        String path = scanner.nextLine().trim();
        Path p = path.isEmpty() ? DEFAULT_SAVE : Path.of(path);
        List<String> lines = customList.toList().stream()
                .map(person -> person.getId() + ";" + person.getName() + ";" + person.getAge())
                .collect(Collectors.toList());
        try {
            FileUtil.appendLines(p, lines);
            System.out.println("Данные записаны в " + p.toAbsolutePath());
        } catch (Exception ex) {
            System.err.println("Ошибка записи: " + ex.getMessage());
        }
    }

    private static void countOccurrencesMenu(CustomList<Person> customList) {
        if (customList == null || customList.size() == 0) { System.out.println("Пусто."); return; }
        System.out.print("Введите элемент для поиска (id;name;age): ");
        String line = scanner.nextLine();
        Optional<Person> maybe = Validator.parsePersonLine(line);
        if (maybe.isEmpty()) { System.out.println("Неверный ввод."); return; }
        Person target = maybe.get();
        System.out.print("Сколько потоков использовать?: ");
        int threads = Integer.parseInt(scanner.nextLine().trim());
        BiPredicate<Person, Person> eq = Objects::equals;
        long count = CountOccurrences.countParallel(customList.toList(), target, eq, threads);
        System.out.println("Число вхождений: " + count);
    }

    private static void runSimpleTests() {
        System.out.println("=== Запуск простых тестов ===");
        tests.SimpleTests.runAll();
        System.out.println("=== Тесты завершены ===");
    }
}
