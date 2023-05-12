public class Philosophers {

    public static class Philosopher implements Runnable {

        private final Object leftFork;
        private final Object rightFork;

        Philosopher(Object left, Object right) {
            this.leftFork = left;
            this.rightFork = right;
        }

        private void doAction(String action) throws InterruptedException {
            System.out.println(Thread.currentThread().getName() + " " + action);
            Thread.sleep(((int) (Math.random() * 100)));
        }

        @Override
        public void run() {
            try {
                while (true) {
                    doAction(System.nanoTime() + ": Thinking"); // thinking
                    synchronized (leftFork) {
                        doAction(System.nanoTime() + ": Picked up left fork");
                        synchronized (rightFork) {
                            doAction(System.nanoTime() + ": Picked up right fork - eating"); // eating
                            doAction(System.nanoTime() + ": Put down right fork");
                        }
                        doAction(System.nanoTime() + ": Put down left fork. Returning to thinking");
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        

        Philosopher[] philosophers = new Philosopher[5];
        Object[] forks = new Object[philosophers.length];

        for (int i = 0; i < forks.length; i++) {
            forks[i] = new Object();
        }

        for (int i = 0; i < philosophers.length; i++) {

            Object leftFork = forks[i];
            Object rightFork = forks[(i + 1) % forks.length];

            if (i == philosophers.length - 1) {
                philosophers[i] = new Philosopher(rightFork, leftFork);
            } else {
                philosophers[i] = new Philosopher(leftFork, rightFork);
            }

            Thread t = new Thread(philosophers[i], "Philosopher " + (i + 1));
            t.start();
        }
    }
}
