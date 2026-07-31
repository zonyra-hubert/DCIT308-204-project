import java.util.Random;

public class JumpSearch {

    /**
     * Jump search on a sorted array using a fixed block step of floor(sqrt(n)).
     * Returns the index of target, or -1 if not found.
     */
    public static int jumpSearch(int[] arr, int target) {
        int n = arr.length;
        int step = (int) Math.floor(Math.sqrt(n));

        int prev = 0;
        int curr = step;

        // Phase 1: jump ahead in fixed-size blocks until we find a block
        // whose last element is >= target, or we run off the array.
        while (curr < n && arr[curr - 1] < target) {
            prev = curr;
            curr += step;
        }

        // Phase 2: linear scan within the identified block.
        int end = Math.min(curr, n);
        for (int i = prev; i < end; i++) {
            if (arr[i] == target) {
                return i;
            }
        }

        return -1;
    }

    /** Linear search, for comparison. */
    public static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }

    // ---- Comparison-counting variants, used for the benchmark ----

    private static class Result {
        int comparisons;
        Result(int index, int comparisons) {
            this.comparisons = comparisons;
        }
    }

    private static Result jumpSearchCounted(int[] arr, int target) {
        int n = arr.length;
        int step = (int) Math.floor(Math.sqrt(n));
        int comparisons = 0;

        int prev = 0;
        int curr = step;

        while (curr < n) {
            comparisons++;
            if (arr[curr - 1] >= target) break;
            prev = curr;
            curr += step;
        }

        int end = Math.min(curr, n);
        for (int i = prev; i < end; i++) {
            comparisons++;
            if (arr[i] == target) {
                return new Result(i, comparisons);
            }
        }
        return new Result(-1, comparisons);
    }

    private static Result linearSearchCounted(int[] arr, int target) {
        int comparisons = 0;
        for (int i = 0; i < arr.length; i++) {
            comparisons++;
            if (arr[i] == target) {
                return new Result(i, comparisons);
            }
        }
        return new Result(-1, comparisons);
    }

    public static void main(String[] args) {
        // Quick correctness demo
        int[] demo = {1, 3, 5, 7, 9, 11, 13, 15, 17};
        System.out.println("Index of 13: " + jumpSearch(demo, 13)); // expect 6

        // Benchmark: jump search vs linear search
        int[] sizes = {100, 1_000, 10_000, 100_000, 1_000_000, 5_000_000};
        int trials = 200;
        Random rnd = new Random(42);

        System.out.printf("%10s | %6s | %13s | %15s | %15s | %17s%n",
                "n", "step", "Jump avg cmp", "Linear avg cmp", "Jump time(us)", "Linear time(us)");
        System.out.println("-".repeat(90));

        for (int n : sizes) {
            int[] arr = new int[n];
            for (int i = 0; i < n; i++) arr[i] = i * 2; // sorted even numbers

            long jumpTimeTotal = 0;
            long linearTimeTotal = 0;
            long jumpCmpTotal = 0;
            long linearCmpTotal = 0;

            for (int t = 0; t < trials; t++) {
                int idx = rnd.nextInt(n);
                int target = arr[idx];

                long t0 = System.nanoTime();
                Result jr = jumpSearchCounted(arr, target);
                long t1 = System.nanoTime();
                jumpTimeTotal += (t1 - t0);
                jumpCmpTotal += jr.comparisons;

                long t2 = System.nanoTime();
                Result lr = linearSearchCounted(arr, target);
                long t3 = System.nanoTime();
                linearTimeTotal += (t3 - t2);
                linearCmpTotal += lr.comparisons;
            }

            double jumpAvgTimeUs = (jumpTimeTotal / (double) trials) / 1000.0;
            double linearAvgTimeUs = (linearTimeTotal / (double) trials) / 1000.0;
            double jumpAvgCmp = jumpCmpTotal / (double) trials;
            double linearAvgCmp = linearCmpTotal / (double) trials;

            System.out.printf("%10d | %6d | %13.1f | %15.1f | %15.2f | %17.2f%n",
                    n, (int) Math.floor(Math.sqrt(n)), jumpAvgCmp, linearAvgCmp,
                    jumpAvgTimeUs, linearAvgTimeUs);
        }
    }
}
