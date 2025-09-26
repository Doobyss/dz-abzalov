package strategy;

import java.util.Comparator;
import java.util.List;

/**
 * Strategy интерфейс для сортировки.
 */
public interface SortStrategy<T> {
    /**
     * Возвращает новую отсортированную коллекцию (не мутирует входящую).
     * @param items входной список
     * @param comparator компаратор для сравнения (если null — ожидается, что T реализует Comparable)
     * @return новый отсортированный List<T>
     */
    List<T> sort(List<T> items, Comparator<T> comparator);
}
