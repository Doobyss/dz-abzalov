// 1) Общий интерфейс для стратегий — определяет метод, который все стратегии должны реализовать
interface PaymentStrategy {
    void pay(int amount);
}

// 2) Одна из реализаций стратегии — оплата картой
class CreditCardPayment implements PaymentStrategy {
    @Override
    public void pay(int amount) {
        System.out.println("Оплата " + amount + " с помощью кредитной карты");
    }
}

// 3) Другая стратегия — оплата через PayPal
class PayPalPayment implements PaymentStrategy {
    @Override
    public void pay(int amount) {
        System.out.println("Оплата " + amount + " через PayPal");
    }
}

// 4) Контекст — использует выбранную стратегию для выполнения операции
class PaymentContext {
    private PaymentStrategy strategy; // хранит выбранную стратегию

    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy; // динамически меняем стратегию
    }

    public void executePayment(int amount) {
        strategy.pay(amount); // делегируем выполнение выбранной стратегии
    }
}

public class Strategy {
    public static void main(String[] args) {
        PaymentContext context = new PaymentContext();

        // Используем стратегию "Оплата картой"
        context.setStrategy(new CreditCardPayment());
        context.executePayment(100);

        // Переключаем стратегию на PayPal
        context.setStrategy(new PayPalPayment());
        context.executePayment(200);
    }
}

