import java.util.LinkedList;
import java.util.Arrays;


// реализация HashSet, с динамическим расширением

class MyHashSet<T> {
    private LinkedList<T>[] buckets;
    private int capacity;
    private int size;
    private final double loadFactor = 0.75;

    @SuppressWarnings("unchecked")
    public MyHashSet() {
        this.capacity = 16; // начальная ёмкость
        this.buckets = new LinkedList[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LinkedList<>();
        }
        this.size = 0;
    }

    private int getIndex(T value) {
        return Math.abs(value.hashCode()) % capacity;
    }

    public boolean insert(T value) {
        int index = getIndex(value);
        LinkedList<T> bucket = buckets[index];
        if (!bucket.contains(value)) {
            bucket.add(value);
            size++;

            if ((double) size / capacity > loadFactor) {
                resize();
            }
            return true;
        }
        return false; // дубликат
    }

    public boolean remove(T value) {
        int index = getIndex(value);
        boolean removed = buckets[index].remove(value);
        if (removed) {
            size--;
        }
        return removed;
    }

    public boolean contains(T value) {
        int index = getIndex(value);
        return buckets[index].contains(value);
    }

    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        LinkedList<T>[] newBuckets = new LinkedList[newCapacity];
        for (int i = 0; i < newCapacity; i++) {
            newBuckets[i] = new LinkedList<>();
        }

        // перераспределяем элементы
        for (LinkedList<T> bucket : buckets) {
            for (T value : bucket) {
                int newIndex = Math.abs(value.hashCode()) % newCapacity;
                newBuckets[newIndex].add(value);
            }
        }

        this.capacity = newCapacity;
        this.buckets = newBuckets;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (LinkedList<T> bucket : buckets) {
            for (T value : bucket) {
                sb.append(value).append(", ");
            }
        }
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("]");
        return sb.toString();
    }
}



//------------------- дальше ArrayList



class MyArrayList<T> {
    private Object[] elements;
    private int size = 0;

    public MyArrayList() {
        elements = new Object[10]; // начальная ёмкость
    }

    public void add(T value) {
        ensureCapacity();
        elements[size++] = value;
    }

    public T get(int index) {
        checkIndex(index);
        return (T) elements[index];
    }

    public void remove(int index) {
        checkIndex(index);
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[--size] = null; // освобождаем ссылку
    }

    public void addAll(MyArrayList<T> other) {
        for (int i = 0; i < other.size; i++) {
            add(other.get(i));
        }
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            elements = Arrays.copyOf(elements, elements.length * 2);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс вне диапазона: " + index);
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(elements, size));
    }
}

public class Main {
    public static void main(String[] args) {

        System.out.println();
        System.out.println("-------");
        System.out.println("HashSet");
        System.out.println("-------");
        System.out.println();

        MyHashSet<String> set = new MyHashSet<>();
        set.insert("Анна");
        set.insert("Иван");
        set.insert("Мария");
        set.insert("Олег");

        System.out.println(set);  // [Анна, Иван, Мария, Олег]
        System.out.println("size = " + set.size()); // 4(динамическое)

        set.remove("Мария");
        System.out.println(set);  // [Анна, Иван, Олег]

        // Проверка содержимого
        System.out.println(set.contains("Анна")); // есть - true
        System.out.println(set.contains("Мария")); // нету - false

        // Тест расширения
        for (int i = 0; i < 100; i++) {
            set.insert("User" + i);
        }
        System.out.println("size = " + set.size()); // 103

        System.out.println();
        System.out.println("---------");
        System.out.println("ArrayList");
        System.out.println("---------");
        System.out.println();

        MyArrayList<String> list = new MyArrayList<>();
        list.add("А");
        list.add("Б");
        list.add("В");
        System.out.println(list); // [А, Б, В]

        list.remove(1);
        System.out.println(list); // [А, В]

        MyArrayList<String> more = new MyArrayList<>();
        more.add("Г");
        more.add("Д");

        list.addAll(more);
        System.out.println(list); // [А, В, Г, Д]

        String first = list.get(0);  // "А"
        String second = list.get(1); // "В"
        String third = list.get(2);  // "Г"
        String fourth = list.get(3);  // "Д"

        System.out.println("Первый элемент: " + first);
        System.out.println("Второй элемент: " + second);
        System.out.println("Третий элемент: " + third);
        System.out.println("Четвертый элемент: " + fourth);

        System.out.println("\nВыводим все элементы списка:");
        for (int i = 0; i < 4; i++) {
            System.out.println("list[" + i + "] = " + list.get(i));
        }

    }
}