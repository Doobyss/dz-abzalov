package collection;

import java.util.*;
import java.util.function.Consumer;

/**
 * Простейшая кастомная коллекция — динамический массив.
 * Поддерживает базовые операции: add/remove/get/size/iterator.
 * Реализована вручную (не наследует ArrayList).
 */
public class CustomList<T> implements Iterable<T> {
    private Object[] data;
    private int size;

    public CustomList() {
        this(10);
    }

    public CustomList(int initialCapacity) {
        if (initialCapacity < 0) throw new IllegalArgumentException("capacity < 0");
        this.data = new Object[initialCapacity];
        this.size = 0;
    }

    public int size() { return size; }

    public boolean isEmpty() { return size == 0; }

    public void add(T element) {
        ensureCapacity(size + 1);
        data[size++] = element;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        rangeCheck(index);
        return (T) data[index];
    }

    public T remove(int index) {
        rangeCheck(index);
        @SuppressWarnings("unchecked")
        T old = (T) data[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) System.arraycopy(data, index + 1, data, index, numMoved);
        data[--size] = null;
        return old;
    }

    public void clear() {
        Arrays.fill(data, 0, size, null);
        size = 0;
    }

    private void ensureCapacity(int required) {
        if (required <= data.length) return;
        int newCapacity = Math.max(required, data.length * 2);
        data = Arrays.copyOf(data, newCapacity);
    }

    private void rangeCheck(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("index: " + index);
    }

    public Object[] toArray() {
        return Arrays.copyOf(data, size);
    }

    public List<T> toList() {
        List<T> out = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
            T el = (T) data[i];
            out.add(el);
        }
        return out;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int cursor = 0;
            @Override public boolean hasNext() { return cursor < size; }
            @SuppressWarnings("unchecked")
            @Override public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                return (T) data[cursor++];
            }
        };
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
            T el = (T) data[i];
            action.accept(el);
        }
    }
}
