package util;

import java.util.List;
import java.util.concurrent.*;
import java.util.function.BiPredicate;

/**
 * Многопоточный подсчёт количества вхождений элемента target в коллекцию.
 * Сравнение проводится через переданный BiPredicate<T,T> (например, equals).
 */
public class CountOccurrences {

    public static <T> long countParallel(List<T> list, T target, BiPredicate<T, T> equalsPredicate, int threads) {
        if (list == null || list.isEmpty()) return 0;
        int n = list.size();
        ExecutorService executor = Executors.newFixedThreadPool(Math.max(2, threads));
        int chunk = (n + threads - 1) / threads;
        Future<Long>[] futures = new Future[threads];
        for (int t = 0; t < threads; t++) {
            final int start = t * chunk;
            final int end = Math.min(n, start + chunk);
            futures[t] = executor.submit(() -> {
                long cnt = 0;
                for (int i = start; i < end; i++) {
                    if (equalsPredicate.test(list.get(i), target)) cnt++;
                }
                return cnt;
            });
        }
        long total = 0;
        for (Future<Long> f : futures) {
            try { total += f.get(); } catch (Exception e) { throw new RuntimeException(e); }
        }
        executor.shutdown();
        return total;
    }
}
