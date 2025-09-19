// Интерфейс сервиса
interface Service {
    void operation();
}

// Сервис — выполняет "тяжёлую" работу
class RealService implements Service {
    public void operation() {
        System.out.println("Сервис выполняет операцию");
    }
}

// Прокси — оборачивает вызов реального сервиса
class ProxyService implements Service {
    private RealService realService; // лениво инициализируем

    @Override
    public void operation() {
        // Дополнительная логика — проверка доступа
        System.out.println("Прокси: проверка доступа...");
        if (realService == null) {
            realService = new RealService();
        }
        realService.operation(); // вызов реального объекта
    }
}

public class Proxy {
    public static void main(String[] args) {
        Service service = new ProxyService();
        service.operation();
    }
}

