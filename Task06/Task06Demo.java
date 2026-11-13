
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class Task06Demo  {

    // ==========================================
    // PART A: WALLET TRANSFERS
    // ==========================================

    static class WalletAccount {
        private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

        final int id = NEXT_ID.getAndIncrement();
        final ReentrantLock lock = new ReentrantLock();
        int balance;

        WalletAccount(int balance) {
            this.balance = balance;
        }
    }

    // Deliberately creates deadlock by acquiring locks
    // in opposite orders.
    static void unsafeTransfer(
            WalletAccount from,
            WalletAccount to,
            CountDownLatch bothHaveFirstLock) {

        from.lock.lock();

        try {
            System.out.println(Thread.currentThread().getName()
                    + " locked wallet " + from.id);

            bothHaveFirstLock.countDown();
            bothHaveFirstLock.await();

            System.out.println(Thread.currentThread().getName()
                    + " waiting for wallet " + to.id);

            to.lock.lock();

            try {
                from.balance -= 100;
                to.balance += 100;
            } finally {
                to.lock.unlock();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            from.lock.unlock();
        }
    }

    static void demonstrateDeadlock() throws InterruptedException {

        System.out.println("\n===== PART A1: DEADLOCK DEMONSTRATION =====");

        WalletAccount accountA = new WalletAccount(1000);
        WalletAccount accountB = new WalletAccount(1000);

        CountDownLatch latch = new CountDownLatch(2);

        Thread t1 = new Thread(
                () -> unsafeTransfer(accountA, accountB, latch),
                "Transfer-A-to-B");

        Thread t2 = new Thread(
                () -> unsafeTransfer(accountB, accountA, latch),
                "Transfer-B-to-A");

        // Daemon threads prevent the demonstration
        // from keeping the JVM alive after deadlock detection.
        t1.setDaemon(true);
        t2.setDaemon(true);

        t1.start();
        t2.start();

        Thread.sleep(500);

        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long[] deadlocked = bean.findDeadlockedThreads();

        if (deadlocked != null) {
            System.out.println("Deadlock detected!");
            System.out.println("Number of deadlocked threads: "
                    + deadlocked.length);

            for (long id : deadlocked) {
                System.out.println(
                        bean.getThreadInfo(id).getThreadName());
            }
        } else {
            System.out.println("No deadlock detected.");
        }
    }

    // Prevents deadlock by always acquiring the lower ID first.
    static void safeTransfer(
            WalletAccount from,
            WalletAccount to,
            int amount) {

        WalletAccount first =
                from.id < to.id ? from : to;

        WalletAccount second =
                from.id < to.id ? to : from;

        first.lock.lock();

        try {
            second.lock.lock();

            try {
                from.balance -= amount;
                to.balance += amount;
            } finally {
                second.lock.unlock();
            }

        } finally {
            first.lock.unlock();
        }
    }

    static void demonstrateSafeTransfers()
            throws InterruptedException {

        System.out.println("\n===== PART A2: SAFE WALLET TRANSFERS =====");

        WalletAccount accountA = new WalletAccount(1000);
        WalletAccount accountB = new WalletAccount(1000);

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                safeTransfer(accountA, accountB, 1);
            }
        }, "Safe-A-to-B");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                safeTransfer(accountB, accountA, 1);
            }
        }, "Safe-B-to-A");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Wallet A balance: " + accountA.balance);
        System.out.println("Wallet B balance: " + accountB.balance);

        System.out.println("Total balance: "
                + (accountA.balance + accountB.balance));

        System.out.println("Safe transfers completed without deadlock.");
    }

    // ==========================================
    // PART B: PRODUCER-CONSUMER TOKEN COUNTER
    // ==========================================

    static class TokenCounter {

        private final int[] buffer = new int[5];

        private int count = 0;
        private int in = 0;
        private int out = 0;

        // TODO 1 COMPLETED
        public synchronized void put(int token)
                throws InterruptedException {

            // Wait while the counter is full.
            while (count == buffer.length) {
                wait();
            }

            buffer[in] = token;
            in = (in + 1) % buffer.length;
            count++;

            System.out.println("Cook produced token " + token
                    + " | Counter size: " + count);

            // Wake all waiting consumers and producers.
            notifyAll();
        }

        // TODO 2 COMPLETED
        public synchronized int take()
                throws InterruptedException {

            // Wait while the counter is empty.
            while (count == 0) {
                wait();
            }

            int token = buffer[out];

            out = (out + 1) % buffer.length;
            count--;

            System.out.println(Thread.currentThread().getName()
                    + " collected token " + token
                    + " | Counter size: " + count);

            // Wake all waiting consumers and producers.
            notifyAll();

            return token;
        }
    }

    static void demonstrateProducerConsumer()
            throws InterruptedException {

        System.out.println("\n===== PART B: PRODUCER-CONSUMER =====");

        TokenCounter counter = new TokenCounter();

        int[] collected = new int[2];

        // One cook produces 20 tokens.
        Thread cook = new Thread(() -> {
            try {
                for (int token = 1; token <= 20; token++) {
                    counter.put(token);
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Cook");

        // Two students collect 10 tokens each.
        Thread[] students = new Thread[2];

        for (int s = 0; s < 2; s++) {

            final int id = s;

            students[s] = new Thread(() -> {
                try {
                    for (int k = 0; k < 10; k++) {
                        collected[id] += counter.take();
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Student-" + (s + 1));
        }

        cook.start();

        for (Thread student : students) {
            student.start();
        }

        cook.join();

        for (Thread student : students) {
            student.join();
        }

        System.out.println("\nStudent 1 collected sum: "
                + collected[0]);

        System.out.println("Student 2 collected sum: "
                + collected[1]);

        System.out.println("Sum of tokens collected: "
                + (collected[0] + collected[1])
                + " (expected 210)");
    }

    // ==========================================
    // MAIN METHOD
    // ==========================================

    public static void main(String[] args)
            throws InterruptedException {

        demonstrateDeadlock();

        demonstrateSafeTransfers();

        demonstrateProducerConsumer();
    }
}


