package parallel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

/**
 * Параллельная merge sort, использующая ExecutorService с фиксированным пулом.
 * Алгоритм разбивает список рекурсивно; для больших частей создаёт новые задачи.
 */
public class ParallelMergeSorter<T> {
    private final ExecutorService executor;
    private final int threshold = 1_000; // порог для параллельных задач (можно регулировать)

    public ParallelMergeSorter(int threadPoolSize) {
        this.executor = Executors.newFixedThreadPool(Math.max(2, threadPoolSize));
    }

    public void sort(List<T> list, Comparator<T> comparator) {
        if (list == null || list.size() < 2) return;
        try {
            mergeSort(list, comparator).get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }

    private Future<Void> mergeSort(List<T> list, Comparator<T> comp) {
        return executor.submit(() -> {
            mergeSortInternal(list, 0, list.size() - 1, comp);
            return null;
        });
    }

    private void mergeSortInternal(List<T> list, int left, int right, Comparator<T> comp) throws Exception {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        if (right - left + 1 <= threshold) {
            // сортируем в текущем потоке простым merge
            sequentialMergeSort(list, left, right, comp);
            return;
        }
        Future<?> leftF = executor.submit(() -> {
            try { mergeSortInternal(list, left, mid, comp); } catch (Exception e) { throw new RuntimeException(e); }
        });
        Future<?> rightF = executor.submit(() -> {
            try { mergeSortInternal(list, mid + 1, right, comp); } catch (Exception e) { throw new RuntimeException(e); }
        });
        leftF.get();
        rightF.get();
        merge(list, left, mid, right, comp);
    }

    private void sequentialMergeSort(List<T> list, int left, int right, Comparator<T> comp) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        sequentialMergeSort(list, left, mid, comp);
        sequentialMergeSort(list, mid + 1, right, comp);
        merge(list, left, mid, right, comp);
    }

    @SuppressWarnings("unchecked")
    private void merge(List<T> list, int left, int mid, int right, Comparator<T> comp) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        List<T> L = new ArrayList<>(n1);
        List<T> R = new ArrayList<>(n2);
        for (int i = 0; i < n1; i++) L.add(list.get(left + i));
        for (int j = 0; j < n2; j++) R.add(list.get(mid + 1 + j));
        int i = 0, j = 0, k = left;
        while (i < L.size() && j < R.size()) {
            if (compare(L.get(i), R.get(j), comp) <= 0) {
                list.set(k++, L.get(i++));
            } else {
                list.set(k++, R.get(j++));
            }
        }
        while (i < L.size()) list.set(k++, L.get(i++));
        while (j < R.size()) list.set(k++, R.get(j++));
    }

    private int compare(T a, T b, Comparator<T> comp) {
        if (comp != null) return comp.compare(a, b);
        @SuppressWarnings("unchecked")
        Comparable<T> ca = (Comparable<T>) a;
        return ca.compareTo(b);
    }

    public void shutdown() {
        executor.shutdown();
    }
}
