import java.util.concurrent.*;

public class ArraySum {
    public static void calculateArraySum(int[] arr) {
        int mid = arr.length / 2;

        Callable<Integer> firstHalfSum = () -> {
            int sum = 0;
            for (int i = 0; i < mid; i++) sum += arr[i];
            return sum;
        };

        Callable<Integer> secondHalfSum = () -> {
            int sum = 0;
            for (int i = mid; i < arr.length; i++) sum += arr[i];
            return sum;
        };

        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            Future<Integer> result1 = executor.submit(firstHalfSum);
            Future<Integer> result2 = executor.submit(secondHalfSum);
            int totalSum = result1.get() + result2.get();
            System.out.println("Сумма элементов массива: " + totalSum);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 100, 900, 2131};
        calculateArraySum(arr);
    }
}
