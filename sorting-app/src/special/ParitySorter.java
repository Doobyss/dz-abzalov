package special;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

/**
 * Специальная сортировка: объекты, у которых числовое поле (extractor) — чётное,
 * должны быть отсортированы в натуральном порядке (по comparator или natural),
 * а объекты с нечётным — оставлены на своих исходных позициях.
 */
public class ParitySorter<T> {

    /**
     * Возвращает новый список, где элементы с even key отсортированы, а odd на тех же местах.
     */
    public List<T> paritySort(List<T> input, ToIntFunction<T> extractor, Comparator<T> comparator) {
        if (input == null) return null;
        List<T> output = new ArrayList<>(input); // копия
        List<Integer> evenIndices = new ArrayList<>();
        List<T> evenElements = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            T el = input.get(i);
            int val = extractor.applyAsInt(el);
            if (val % 2 == 0) {
                evenIndices.add(i);
                evenElements.add(el);
            }
        }
        // сортируем evenElements
        evenElements.sort((o1, o2) -> {
            if (comparator != null) return comparator.compare(o1, o2);
            @SuppressWarnings("unchecked")
            Comparable<T> c1 = (Comparable<T>) o1;
            return c1.compareTo(o2);
        });
        // поместим отсортированные значения обратно на позиции evenIndices
        for (int i = 0; i < evenIndices.size(); i++) {
            output.set(evenIndices.get(i), evenElements.get(i));
        }
        return output;
    }
}

