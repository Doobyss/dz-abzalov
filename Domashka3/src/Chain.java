// Базовый класс обработчика
abstract class Handler {
    protected Handler next; // ссылка на следующий обработчик

    public Handler setNext(Handler next) {
        this.next = next;
        return next;
    }

    public void handle(String request) {
        // базовая реализация — передать дальше
        if (next != null) next.handle(request);
    }
}

// Обработчик для авторизации
class AuthHandler extends Handler {
    @Override
    public void handle(String request) {
        if (request.contains("auth")) {
            System.out.println("Авторизация прошла");
        }
        // передаем дальше
        super.handle(request);
    }
}

// Обработчик для логирования
class LogHandler extends Handler {
    @Override
    public void handle(String request) {
        System.out.println("Логируем запрос: " + request);
        super.handle(request);
    }
}

public class Chain {
    public static void main(String[] args) {
        // Строим цепочку: AuthHandler - LogHandler
        Handler chain = new AuthHandler();
        chain.setNext(new LogHandler());

        // Отправляем запрос, он проходит через всю цепочку
        chain.handle("auth + some data");
    }
}

