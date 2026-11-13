
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.List;

public class AlternativeBooking {

    static final int SEATS = 1000;
    static final int ATTEMPTS = 10000;
    static final int THREADS = 20;

    interface BookingStrategy {
        boolean book(int seat);
        int getSeatsSold();
    }

    // Approach 1: AtomicBoolean array
    static class AtomicBooking implements BookingStrategy {
        private final AtomicBoolean[] seats = new AtomicBoolean[SEATS];
        private final AtomicInteger sold = new AtomicInteger();

        AtomicBooking() {
            for (int i = 0; i < SEATS; i++) {
                seats[i] = new AtomicBoolean(false);
            }
        }

        public boolean book(int seat) {
            if (seats[seat].compareAndSet(false, true)) {
                sold.incrementAndGet();
                return true;
            }
            return false;
        }

        public int getSeatsSold() {
            return sold.get();
        }
    }

    // Approach 2: ReentrantLock for each seat
    static class LockBooking implements BookingStrategy {
        private final boolean[] seats = new boolean[SEATS];
        private final ReentrantLock[] locks = new ReentrantLock[SEATS];
        private final AtomicInteger sold = new AtomicInteger();

        LockBooking() {
            for (int i = 0; i < SEATS; i++) {
                locks[i] = new ReentrantLock();
            }
        }

        public boolean book(int seat) {
            locks[seat].lock();
            try {
                if (!seats[seat]) {
                    seats[seat] = true;
                    sold.incrementAndGet();
                    return true;
                }
                return false;
            } finally {
                locks[seat].unlock();
            }
        }

        public int getSeatsSold() {
            return sold.get();
        }
    }

// Approach 3: ConcurrentHashMap.putIfAbsent()
    static class MapBooking implements BookingStrategy {
        private final ConcurrentHashMap<Integer, Boolean> seats =
                new ConcurrentHashMap<>();

        public boolean book(int seat) {
            return seats.putIfAbsent(seat, true) == null;
        }

        public int getSeatsSold() {
            return seats.size();
        }
    }

    static void runTest(String name, BookingStrategy strategy)
            throws InterruptedException, ExecutionException {

        ExecutorService pool = Executors.newFixedThreadPool(THREADS);
        List<Future<?>> futures = new ArrayList<>();

        long startTime = System.nanoTime();

        for (int i = 0; i < ATTEMPTS; i++) {
            final int seat = i % SEATS;

            futures.add(pool.submit(() -> strategy.book(seat)));
        }

        for (Future<?> future : futures) {
            future.get();
        }

        long endTime = System.nanoTime();
        pool.shutdown();

        double timeMs = (endTime - startTime) / 1_000_000.0;

        System.out.println("\n" + name);
        System.out.println("Booking attempts: " + ATTEMPTS);
        System.out.println("Seats sold: " + strategy.getSeatsSold());
        System.out.printf("Time: %.3f ms%n", timeMs);
    }

    public static void main(String[] args)
            throws InterruptedException, ExecutionException {

        System.out.println("=== ALTERNATIVE BOOKING APPROACHES ===");

        runTest("1. ATOMIC BOOLEAN ARRAY", new AtomicBooking());
        runTest("2. REENTRANT LOCK", new LockBooking());
        runTest("3. CONCURRENT HASH MAP", new MapBooking());
    }
}

