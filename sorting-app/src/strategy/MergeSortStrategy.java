package strategy;

import parallel.ParallelMergeSorter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Merge sort strategy. Использует ParallelMergeSorter для выполнения сортировки
 * в пуле потоков. Если коллекция мала — сортируется в одном потоке.
 */
public class MergeSortStrategy<T> implements SortStrategy<T> {
    private final ParallelMergeSorter<T> sorter;

    public MergeSortStrategy(int threadPoolSize) {
        this.sorter = new ParallelMergeSorter<>(threadPoolSize);
    }

    @Override
    public List<T> sort(List<T> items, Comparator<T> comparator) {
        if (items == null) return null;
        // копируем вход, чтобы не менять исходник
        List<T> copy = new ArrayList<>(items);
        sorter.sort(copy, comparator);
        return copy;
    }
}
