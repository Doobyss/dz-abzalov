package model;

/**
 * Пример кастомного класса, у которого реализован Builder.
 * Поля: id (numeric), name, age.
 */
public class Person implements Comparable<Person> {
    private final int id;
    private final String name;
    private final int age;

    private Person(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.age = builder.age;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }

    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", name='" + name + '\'' + ", age=" + age + '}';
    }

    /**
     * Базовый Comparable: по name, затем age, затем id.
     */
    @Override
    public int compareTo(Person other) {
        int cmp = this.name.compareTo(other.name);
        if (cmp != 0) return cmp;
        cmp = Integer.compare(this.age, other.age);
        if (cmp != 0) return cmp;
        return Integer.compare(this.id, other.id);
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private int id;
        private String name;
        private int age;

        public Builder id(int id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder age(int age) { this.age = age; return this; }
        public Person build() {
            // минимальная валидация внутри билдера
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("name must not be empty");
            }
            if (age < 0 || age > 200) {
                throw new IllegalArgumentException("age out of range");
            }
            return new Person(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;
        return id == person.id && age == person.age && name.equals(person.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + age;
        return result;
    }
}
