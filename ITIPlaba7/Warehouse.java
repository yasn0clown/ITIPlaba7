import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.*;

public class Warehouse {
    private static final int MAX_WEIGHT = 150;
    private static final int NUM_WORKERS = 3;
    private static Queue<Integer> items = new LinkedList<>();
    private static AtomicInteger currentWeight = new AtomicInteger(0);
    private static CyclicBarrier barrier = new CyclicBarrier(NUM_WORKERS, () -> {
        System.out.println("Груз весом " + currentWeight.get() + " кг отправляется на другой склад");
        currentWeight.set(0);
    });

    static class Worker implements Runnable {
        @Override
        public void run() {
            while (!items.isEmpty()) {
                int item;
                synchronized (items) {
                    if (items.isEmpty()) break;
                    item = items.poll();
                }

                while (true) {
                    int weight = currentWeight.get();
                    if (weight + item > MAX_WEIGHT) {
                        try {
                            barrier.await();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (currentWeight.compareAndSet(weight, weight + item)) {
                        System.out.println(Thread.currentThread().getName() + " добавил груз весом " + item + " кг");
                        break;
                    }
                }
            }

            try {
                barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        int[] weights = {50, 30, 20, 40, 60, 10, 70, 80, 90};
        for (int weight : weights) {
            items.offer(weight);
        }

        for (int i = 0; i < NUM_WORKERS; i++) {
            new Thread(new Worker(), "Грузчик").start();
        }
    }
}
