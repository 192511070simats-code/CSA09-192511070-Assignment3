
import java.util.concurrent.atomic.AtomicInteger;

public class ScannerStationDemo {

    static final int[] SCRIPT_BATCHES = {10, 12, 8, 15};

    // Method 1: Creating threads by extending Thread
    static class StationThread extends Thread {
        private final int stationNumber;
        private final int scripts;
        private final AtomicInteger total;

        StationThread(int stationNumber, int scripts, AtomicInteger total) {
            this.stationNumber = stationNumber;
            this.scripts = scripts;
            this.total = total;
        }

        @Override
        public void run() {
            try {
                System.out.println("Station " + stationNumber
                        + " started scanning " + scripts + " scripts.");

                Thread.sleep(500 + (stationNumber * 300));

                total.addAndGet(scripts);

                System.out.println("Station " + stationNumber
                        + " finished scanning " + scripts + " scripts.");

            } catch (InterruptedException e) {
                System.out.println("Station " + stationNumber + " interrupted.");
                Thread.currentThread().interrupt();
            }
        }
    }

    // Method 2: Creating threads by implementing Runnable
    static class StationTask implements Runnable {
        private final int stationNumber;
        private final int scripts;
        private final AtomicInteger total;

        StationTask(int stationNumber, int scripts, AtomicInteger total) {
            this.stationNumber = stationNumber;
            this.scripts = scripts;
            this.total = total;
        }

        @Override
        public void run() {
            try {
                System.out.println("Station " + stationNumber
                        + " started scanning " + scripts + " scripts.");

                Thread.sleep(500 + (stationNumber * 300));

                total.addAndGet(scripts);

                System.out.println("Station " + stationNumber
                        + " finished scanning " + scripts + " scripts.");

            } catch (InterruptedException e) {
                System.out.println("Station " + stationNumber + " interrupted.");
                Thread.currentThread().interrupt();
            }
        }
    }

    // Experiment 1: Extend Thread
    static void runThreadStations() throws InterruptedException {
        System.out.println("\n=== EXTENDING THREAD ===");

        AtomicInteger total = new AtomicInteger(0);
        StationThread[] stations = new StationThread[4];

        for (int i = 0; i < 4; i++) {
            stations[i] = new StationThread(i + 1, SCRIPT_BATCHES[i], total);
            stations[i].start();
        }

        System.out.println("Station 1 alive: " + stations[0].isAlive());

        for (StationThread station : stations) {
            station.join();
        }

        System.out.println("All Thread stations finished.");
        System.out.println("Final total scanned: " + total.get());
    }

    // Experiment 2: Implement Runnable
    static void runRunnableStations() throws InterruptedException {
        System.out.println("\n=== IMPLEMENTING RUNNABLE ===");

        AtomicInteger total = new AtomicInteger(0);
        Thread[] stations = new Thread[4];

        for (int i = 0; i < 4; i++) {
            StationTask task =
                    new StationTask(i + 1, SCRIPT_BATCHES[i], total);

            stations[i] = new Thread(task);
            stations[i].start();
        }

        System.out.println("Station 1 alive: " + stations[0].isAlive());

        for (Thread station : stations) {
            station.join();
        }

        System.out.println("All Runnable stations finished.");
        System.out.println("Final total scanned: " + total.get());
    }

    // Experiment 3: run() versus start()
    static void runVsStart() throws InterruptedException {
        System.out.println("\n=== RUN() VERSUS START() ===");

        AtomicInteger total = new AtomicInteger(0);

        StationTask directTask = new StationTask(1, 10, total);

        System.out.println("Calling run() directly:");
        directTask.run();

        System.out.println("Calling start():");
        Thread newThread =
                new Thread(new StationTask(2, 10, total));

        newThread.start();
        newThread.join();

        System.out.println("Check the thread names in the output.");
    }

    // Experiment 4: Thread priorities
    static void priorityExperiment() throws InterruptedException {
        System.out.println("\n=== THREAD PRIORITY EXPERIMENT ===");

        AtomicInteger total = new AtomicInteger(0);
        Thread[] stations = new Thread[4];

        int[] priorities = {
            Thread.MIN_PRIORITY,
            Thread.NORM_PRIORITY,
            Thread.MAX_PRIORITY,
            Thread.NORM_PRIORITY
        };

        for (int i = 0; i < 4; i++) {
            stations[i] = new Thread(
                    new StationTask(i + 1, SCRIPT_BATCHES[i], total));

            stations[i].setPriority(priorities[i]);

            System.out.println("Station " + (i + 1)
                    + " priority: " + stations[i].getPriority());

            stations[i].start();
        }

        for (Thread station : stations) {
            station.join();
        }

        System.out.println("Total scanned: " + total.get());
    }

    // Experiment 5: Daemon thread
    static void daemonExperiment() throws InterruptedException {
        System.out.println("\n=== DAEMON THREAD EXPERIMENT ===");

        Thread daemonStation = new Thread(() -> {
            try {
                System.out.println("Daemon station started.");

                Thread.sleep(2000);

                System.out.println("Daemon station finished.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        daemonStation.setDaemon(true);
        daemonStation.start();

        System.out.println("Is daemon: " + daemonStation.isDaemon());
        System.out.println("Main thread is ending.");
    }

    public static void main(String[] args) throws InterruptedException {

        String mode = args.length > 0 ? args[0] : "normal";

        switch (mode.toLowerCase()) {
            case "normal":
                runThreadStations();
                runRunnableStations();
                break;

            case "run":
                runVsStart();
                break;

            case "priority":
                priorityExperiment();
                break;

            case "daemon":
                daemonExperiment();
                break;

            default:
                System.out.println(
                    "Use: normal, run, priority, or daemon");
        }
    }
}
