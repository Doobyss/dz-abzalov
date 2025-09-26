public class LiveLock {

    // Общий ресурс
    static class Tool {
        private Worker owner;

        public Tool(Worker d) {
            this.owner = d;
        }

        public Worker getOwner() {
            return owner;
        }

        public synchronized void setOwner(Worker d) {
            this.owner = d;
        }

        public synchronized void use() {
            System.out.println(owner.getName() + " использует инструмент!");
        }
    }

    // Рабочий
    static class Worker {
        private final String name;
        private boolean needsWork;

        public Worker(String name) {
            this.name = name;
            this.needsWork = true;
        }

        public String getName() {
            return name;
        }

        public boolean needsWork() {
            return needsWork;
        }

        public void workWith(Tool tool, Worker partner) {
            while (needsWork) {
                // Если инструмент не у меня → жду
                if (tool.getOwner() != this) {
                    try { Thread.sleep(1); } catch (InterruptedException ignored) {}
                    continue;
                }

                // Если партнёр тоже нуждается - уступаю
                if (partner.needsWork()) {
                    System.out.println(name + ": Уступаю инструмент " + partner.getName());
                    tool.setOwner(partner);
                    continue;
                }

                // Работаю с инструментом
                tool.use();
                needsWork = false;
                System.out.println(name + ": Завершил работу");
                tool.setOwner(partner);
            }
        }
    }

    public static void main(String[] args) {
        final Worker worker1 = new Worker("Рабочий-1");
        final Worker worker2 = new Worker("Рабочий-2");
        final Tool tool = new Tool(worker1);

        new Thread(() -> worker1.workWith(tool, worker2)).start();
        new Thread(() -> worker2.workWith(tool, worker1)).start();
    }
}

