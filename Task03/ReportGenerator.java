
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {

    static int openReaders = 0;
    static int openWriters = 0;
    static List<TrackedReader> activeReaders = new ArrayList<>();

    // Tracks input file readers.
    static class TrackedReader implements AutoCloseable {
        BufferedReader reader;
        boolean closed = false;

        TrackedReader(String fileName) throws IOException {
            reader = Files.newBufferedReader(Path.of(fileName));
            openReaders++;
            activeReaders.add(this);
        }

        String readLine() throws IOException {
            return reader.readLine();
        }

        public void close() throws IOException {
            if (!closed) {
                reader.close();
                closed = true;
                openReaders--;
                activeReaders.remove(this);
            }
        }
    }

    // Tracks the output report writer.
    static class TrackedWriter implements AutoCloseable {
        BufferedWriter writer;
        boolean closed = false;

        TrackedWriter(String fileName) throws IOException {
            writer = Files.newBufferedWriter(Path.of(fileName));
            openWriters++;
        }

        void write(String text) throws IOException {
            writer.write(text);
        }

        void newLine() throws IOException {
            writer.newLine();
        }

        public void close() throws IOException {
            if (!closed) {
                writer.close();
                closed = true;
                openWriters--;
            }
        }
    }

    // Version 1: Manual closing with an intentional resource leak.
    static void manualVersion(String[] files, String reportName) {
        TrackedWriter writer = null;
        String currentFile = "";

        try {
            writer = new TrackedWriter(reportName);
            writer.write("DAILY SALES REPORT");
            writer.newLine();

            for (String file : files) {
                currentFile = file;
                TrackedReader reader = new TrackedReader(file);

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");

                    if (data.length != 2) {
                        throw new IOException("Invalid record");
                    }

                    int sales = Integer.parseInt(data[1]);
                    writer.write(data[0] + " : " + sales);
                    writer.newLine();
                }

                // This line is skipped if reading fails.
                reader.close();
            }

            System.out.println("Manual version: Report generated.");

        } catch (IOException | NumberFormatException e) {
            System.out.println("Manual version failed at: " + currentFile);
            System.out.println("Error: " + e.getMessage());

        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                System.out.println("Writer close error: " + e.getMessage());
            }
        }
    }

    // Version 2: Automatic closing using try-with-resources.
    static void tryWithResourcesVersion(String[] files, String reportName) {
        String currentFile = "";

        try (TrackedWriter writer = new TrackedWriter(reportName)) {
            writer.write("DAILY SALES REPORT");
            writer.newLine();

            for (String file : files) {
                currentFile = file;

                try (TrackedReader reader = new TrackedReader(file)) {
                    String line;

                    while ((line = reader.readLine()) != null) {
                        String[] data = line.split(",");

                        if (data.length != 2) {
                            throw new IOException("Invalid record");
                        }

                        int sales = Integer.parseInt(data[1]);
                        writer.write(data[0] + " : " + sales);
                        writer.newLine();
                    }
                }
            }

            System.out.println("Try-with-resources: Report generated.");

        } catch (IOException | NumberFormatException e) {
            System.out.println("Try-with-resources failed at: " + currentFile);
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Closes readers leaked by the manual version.
    static void cleanupLeakedReaders() {
        for (TrackedReader reader : new ArrayList<>(activeReaders)) {
            try {
                reader.close();
            } catch (IOException e) {
                System.out.println("Cleanup error: " + e.getMessage());
            }
        }
    }

    static void showOpenResources() {
        System.out.println("Open readers: " + openReaders);
        System.out.println("Open writers: " + openWriters);
        System.out.println("Total open resources: "
                + (openReaders + openWriters));
    }

    // A resource whose close method deliberately fails.
    static class FailingCloseResource implements AutoCloseable {
        public void close() throws IOException {
            throw new IOException("Resource close failed");
        }
    }

    // Demonstrates a suppressed exception.
    static void demonstrateSuppressedException() {
        System.out.println("\nTEST 5: SUPPRESSED EXCEPTION");

        try (FailingCloseResource resource =
                     new FailingCloseResource()) {

            throw new IOException("Report processing failed");

        } catch (IOException e) {
            System.out.println("Main exception: " + e.getMessage());

            for (Throwable suppressed : e.getSuppressed()) {
                System.out.println("Suppressed exception: "
                        + suppressed.getMessage());
            }
        }
    }

    public static void main(String[] args) {

        String[] validFiles = {
            "store1.txt", "store2.txt", "store3.txt"
        };

        String[] failingFiles = {
            "store1.txt", "store-fail.txt", "store3.txt"
        };

        System.out.println("TEST 1: MANUAL VERSION - VALID INPUT");
        manualVersion(validFiles, "manual-report.txt");
        showOpenResources();

        System.out.println("\nTEST 2: TRY-WITH-RESOURCES - VALID INPUT");
        tryWithResourcesVersion(validFiles, "twr-report.txt");
        showOpenResources();

        System.out.println("\nTEST 3: MANUAL VERSION - FAILING INPUT");
        manualVersion(failingFiles, "manual-fail-report.txt");
        showOpenResources();

        System.out.println("\nCleaning up leaked readers:");
        cleanupLeakedReaders();
        showOpenResources();

        System.out.println("\nTEST 4: TRY-WITH-RESOURCES - FAILING INPUT");
        tryWithResourcesVersion(failingFiles, "twr-fail-report.txt");
        showOpenResources();

        demonstrateSuppressedException();
    }
}
