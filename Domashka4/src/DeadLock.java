public class DeadLock {
    public static void main(String[] args) {
        final Object lock1 = new Object();
        final Object lock2 = new Object();

        // Поток 1 блокирует lock1, потом пытается захватить lock2
        Thread t1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("Поток 1: захватил lock1");
                try {
                    Thread.sleep(100); // задержка, чтобы второй поток успел захватить lock2
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Поток 1: пытается захватить lock2...");
                synchronized (lock2) {
                    System.out.println("Поток 1: захватил lock2");
                }
            }
        });

        // Поток 2 блокирует lock2, потом пытается захватить lock1
        Thread t2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println("Поток 2: захватил lock2");
                try {
                    Thread.sleep(100); // задержка
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Поток 2: пытается захватить lock1...");
                synchronized (lock1) {
                    System.out.println("Поток 2: захватил lock1");
                }
            }
        });

        t1.start();
        t2.start();
    }
}
