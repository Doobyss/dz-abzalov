public class TwoThreads {
    private final Object lock = new Object();
    private boolean turnOne = true; // чей ход сейчас

    public static void main(String[] args) {
        TwoThreads ap = new TwoThreads();
        ap.start();
    }

    public void start() {
        Thread t1 = new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    while (!turnOne) { // если сейчас не его очередь
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("1");
                    turnOne = false;
                    lock.notifyAll(); // будим второй поток
                }
            }
        });

        Thread t2 = new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    while (turnOne) { // если сейчас очередь первого
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("2");
                    turnOne = true;
                    lock.notifyAll(); // будим первый поток
                }
            }
        });

        t1.start();
        t2.start();
    }
}
