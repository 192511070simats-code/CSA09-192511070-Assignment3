
public class RunVsStartDemo {

    static class ScannerTask implements Runnable {
        private final String station;

        ScannerTask(String station) {
            this.station = station;
        }

        @Override
        public void run() {
            System.out.println(
                station + " is running on thread: "
                + Thread.currentThread().getName()
            );
        }
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== RUN() DIRECTLY ===");

        ScannerTask task1 = new ScannerTask("Station 1");
        task1.run();

        System.out.println("\n=== START() ===");

        Thread thread = new Thread(
            new ScannerTask("Station 2")
        );

        thread.start();
        thread.join();

        System.out.println("\nMain thread: "
                + Thread.currentThread().getName());
    }
}
