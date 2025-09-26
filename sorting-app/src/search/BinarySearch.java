package search;

import java.util.Comparator;
import java.util.List;

/**
 * Generic binary search — итеративная реализация.
 * Предполагает, что list отсортирован в порядке, совместимом с comparator (или natural).
 * Возвращает индекс найденного элемента или -1 если не найден.
 */
public class BinarySearch {
    public static <T> int binarySearch(List<T> list, T key, Comparator<T> comparator) {
        if (list == null) return -1;
        int lo = 0, hi = list.size() - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = compare(list.get(mid), key, comparator);
            if (cmp == 0) return mid;
            if (cmp < 0) lo = mid + 1;
            else hi = mid - 1;
        }
        return -1;
    }

    @SuppressWarnings("unchecked")
    private static <T> int compare(T a, T b, Comparator<T> comp) {
        if (comp != null) return comp.compare(a, b);
        return ((Comparable<T>) a).compareTo(b);
    }
}
