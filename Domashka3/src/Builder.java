class User {
    private String name;
    private int age;
    private String email;

    // Конструктор приватный — нельзя создать User без билдера
    private User(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.email = builder.email;
    }

    // Статический вложенный класс — сам билдер
    public static class Builder {
        private String name;
        private int age;
        private String email;

        // Сеттеры возвращают сам билдер, чтобы можно было вызывать цепочкой
        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        // Итоговый метод — создаёт объект User
        public User build() {
            return new User(this);
        }
    }

    @Override
    public String toString() {
        return "User{name= '" + name + "', age= " + age + ", email= '" + email + "'}";
    }
}

public class Builder {
    public static void main(String[] args) {
        // Пошаговое создание объекта
        User user = new User.Builder()
                .setName("Денис")
                .setAge(25)
                .setEmail("abzalish2@yandex.ru")
                .build();

        System.out.println(user);
    }
}
