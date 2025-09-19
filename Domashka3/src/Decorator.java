// Базовый интерфейс
interface Notifier {
    void send(String message);
}

// Базовая реализация
class EmailNotifier implements Notifier {
    public void send(String message) {
        System.out.println("Отправка email: " + message);
    }
}

// Декоратор — добавляет новый функционал (SMS)
class SMSDecorator implements Notifier {
    private Notifier wrapped; // "оборачиваем" исходный объект

    public SMSDecorator(Notifier wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void send(String message) {
        wrapped.send(message); // сначала отправляем email
        System.out.println("Отправка SMS: " + message); // затем SMS
    }
}

public class Decorator {
    public static void main(String[] args) {
        // Создаём EmailNotifier, а затем "декорируем" его отправкой SMS
        Notifier notifier = new SMSDecorator(new EmailNotifier());
        notifier.send("Всем привет!");
    }
}
