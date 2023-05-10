package sbu.cs.CalculatePi;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class PiCalculator {

    /**
     * Calculate pi and represent it as a BigDecimal object with the given floating point number (digits after . )
     * There are several algorithms designed for calculating pi, it's up to you to decide which one to implement.
     * Experiment with different algorithms to find accurate results.
     *
     * You must design a multithreaded program to calculate pi. Creating a thread pool is recommended.
     * Create as many classes and threads as you need.
     * Your code must pass all of the test cases provided in the test folder.
     *
     * @param floatingPoint the exact number of digits after the floating point
     * @return pi in string format (the string representation of the BigDecimal object)
     */
    public String calculate(int floatingPoint) {
        // Set the precision to floatingPoint + 20 to ensure accuracy
        MathContext mathContext = new MathContext(floatingPoint + 20);

        // Use an ExecutorService to manage the threads
        ExecutorService threadPool = Executors.newFixedThreadPool(8);

        // Divide the calculation into batches
        int batchSize = 100000;
        int batches = 8000000 / batchSize;
        List<Future<BigDecimal>> results = new ArrayList<>(batches);

        for (int i = 0; i < batches; i++) {
            int start = i * batchSize;
            int end = start + batchSize - 1;
            Callable<BigDecimal> task = new PiTask(start, end, mathContext);
            results.add(threadPool.submit(task));
        }

        threadPool.shutdown();

        try {
            // Wait up to 20 seconds for the threads to finish
            threadPool.awaitTermination(20000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Combine the results of the threads
        BigDecimal sum = BigDecimal.ZERO;
        for (Future<BigDecimal> result : results) {
            try {
                sum = sum.add(result.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // Calculate pi from the sum and round to the desired precision
        BigDecimal pi = sum.multiply(BigDecimal.valueOf(4), mathContext);
        pi = pi.setScale(floatingPoint, RoundingMode.DOWN);
        return pi.toString();
    }

    private static class PiTask implements Callable<BigDecimal> {

        private final int start;
        private final int end;
        private final MathContext mathContext;

        public PiTask(int start, int end, MathContext mathContext) {
            this.start = start;
            this.end = end;
            this.mathContext = mathContext;
        }

        @Override
        public BigDecimal call() {
            BigDecimal localSum = BigDecimal.ZERO;
            for (int k = start; k <= end; k++) {
                BigDecimal numerator = BigDecimal.ONE;
                if (k % 2 != 0) {
                    numerator = numerator.negate();
                }
                BigDecimal denominator = BigDecimal.valueOf(2 * k + 1);
                BigDecimal term = numerator.divide(denominator, mathContext);
                localSum = localSum.add(term);
            }
            return localSum;
        }
    }
}