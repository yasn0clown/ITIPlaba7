import java.util.concurrent.*;
import java.util.*;

public class MatrixMax {
    public static void findMaxInMatrix(int[][] matrix) {
        int rows = matrix.length;
        ExecutorService executor = Executors.newFixedThreadPool(rows);

        List<Future<Integer>> futures = new ArrayList<>();
        for (int[] row : matrix) {
            Callable<Integer> maxInRow = () -> {
                int max = Integer.MIN_VALUE;
                for (int num : row) max = Math.max(max, num);
                return max;
            };
            futures.add(executor.submit(maxInRow));
        }

        try {
            int globalMax = Integer.MIN_VALUE;
            for (Future<Integer> future : futures) {
                globalMax = Math.max(globalMax, future.get());
            }
            System.out.println("Наибольший элемент в матрице: " + globalMax);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    public static void main(String[] args) {
        int[][] matrix = {
                {1, 2, 3},
                {4, 52, 6},
                {7, 8, 9}
        };
        findMaxInMatrix(matrix);
    }
}
