package strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Реализация быстрой сортировки (QuickSort) — однопоточная.
 * Не использует готовые реализации сортировки.
 */
public class QuickSortStrategy<T> implements SortStrategy<T> {

    @Override
    public List<T> sort(List<T> items, Comparator<T> comparator) {
        if (items == null) return null;
        List<T> copy = new ArrayList<>(items);
        quickSort(copy, 0, copy.size() - 1, comparator);
        return copy;
    }

    @SuppressWarnings("unchecked")
    private void quickSort(List<T> list, int lo, int hi, Comparator<T> comp) {
        if (lo >= hi) return;
        int p = partition(list, lo, hi, comp);
        quickSort(list, lo, p - 1, comp);
        quickSort(list, p + 1, hi, comp);
    }

    private int partition(List<T> list, int lo, int hi, Comparator<T> comp) {
        T pivot = list.get(hi);
        int i = lo;
        for (int j = lo; j < hi; j++) {
            if (compare(list.get(j), pivot, comp) <= 0) {
                swap(list, i, j);
                i++;
            }
        }
        swap(list, i, hi);
        return i;
    }

    private int compare(T a, T b, Comparator<T> comp) {
        if (comp != null) return comp.compare(a, b);
        @SuppressWarnings("unchecked")
        Comparable<T> ca = (Comparable<T>) a;
        return ca.compareTo(b);
    }

    private void swap(List<T> list, int i, int j) {
        T tmp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, tmp);
    }
}
