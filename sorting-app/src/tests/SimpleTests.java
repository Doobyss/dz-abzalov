package tests;

import collection.CustomList;
import model.Person;
import parallel.ParallelMergeSorter;
import search.BinarySearch;
import special.ParitySorter;
import strategy.MergeSortStrategy;
import strategy.QuickSortStrategy;
import util.CountOccurrences;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Ряд простых ручных тестов.
 */
public class SimpleTests {

    public static void runAll() {
        testQuickSort();
        testMergeSortParallel();
        testBinarySearch();
        testParitySort();
        testCountOccurrences();
    }

    private static void testQuickSort() {
        System.out.println("-- testQuickSort");
        List<Person> list = IntStream.range(0, 20)
                .mapToObj(i -> Person.builder().id(i).name("n" + (20 - i)).age(20 - i).build())
                .collect(Collectors.toList());
        QuickSortStrategy<Person> qs = new QuickSortStrategy<>();
        List<Person> sorted = qs.sort(list, Comparator.comparing(Person::getName));
        System.out.println("First 5 after quicksort:");
        sorted.stream().limit(5).forEach(System.out::println);
    }

    private static void testMergeSortParallel() {
        System.out.println("-- testMergeSortParallel");
        List<Person> list = IntStream.range(0, 5000)
                .mapToObj(i -> Person.builder().id(i).name("n" + (5000 - i)).age(i % 100).build())
                .collect(Collectors.toList());
        MergeSortStrategy<Person> ms = new MergeSortStrategy<>(4);
        List<Person> sorted = ms.sort(list, Comparator.comparingInt(Person::getId));
        System.out.println("Sorted first 3:");
        sorted.stream().limit(3).forEach(System.out::println);
    }

    private static void testBinarySearch() {
        System.out.println("-- testBinarySearch");
        List<Person> list = IntStream.range(0, 100)
                .mapToObj(i -> Person.builder().id(i).name("n"+i).age(i%50).build())
                .collect(Collectors.toList());
        int idx = BinarySearch.binarySearch(list, list.get(50), Comparator.comparingInt(Person::getId));
        System.out.println("Index of id=50: " + idx);
    }

    private static void testParitySort() {
        System.out.println("-- testParitySort");
        List<Person> list = IntStream.range(0, 10)
                .mapToObj(i -> Person.builder().id(i).name("n"+(9-i)).age(i).build())
                .collect(Collectors.toList());
        ParitySorter<Person> ps = new ParitySorter<>();
        List<Person> out = ps.paritySort(list, Person::getId, Comparator.comparingInt(Person::getId));
        System.out.println("Parity result:");
        out.forEach(System.out::println);
    }

    private static void testCountOccurrences() {
        System.out.println("-- testCountOccurrences");
        CustomList<Person> cl = new CustomList<>();
        for (int i = 0; i < 1000; i++) {
            cl.add(Person.builder().id(i % 10).name("n" + (i % 10)).age(i % 100).build());
        }
        Person target = Person.builder().id(3).name("n3").age(3).build();
        long cnt = CountOccurrences.countParallel(cl.toList(), target, Objects::equals, 4);
        System.out.println("Occurrences of id=3: " + cnt);
    }
}
