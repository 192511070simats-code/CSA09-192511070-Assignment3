
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SemesterResultProcessor {

    private static final int TOTAL_FILES = 10_000;
    private static final int VALID_FILES = 9_850;
    private static final int CORRUPT_FILES = 100;
    private static final int MISSING_FILES = 50;

    private static final Path DATA_DIR = Paths.get("score_files");

    // Stores the outcome of processing one expected file.
    static class ProcessingResult {
        final String fileName;
        final boolean successful;
        final String message;
        final int score;

        ProcessingResult(String fileName, boolean successful,
                         String message, int score) {
            this.fileName = fileName;
            this.successful = successful;
            this.message = message;
            this.score = score;
        }
    }

    // One Callable task processes one score file.
    static class ScoreTask implements Callable<ProcessingResult> {
        private final Path file;

        ScoreTask(Path file) {
            this.file = file;
        }

        @Override
        public ProcessingResult call() {
            String fileName = file.getFileName().toString();

            try {
                String content = Files.readString(file).trim();
                String[] parts = content.split(",", -1);

                if (parts.length != 2 || parts[0].trim().isEmpty()) {
                    throw new IllegalArgumentException("Invalid file format");
                }

                String studentId = parts[0].trim();
                int score;

                try {
                    score = Integer.parseInt(parts[1].trim());
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Score is not numeric");
                }

                if (score < 0 || score > 100) {
                    throw new IllegalArgumentException("Score must be between 0 and 100");
                }

                String result = score >= 50 ? "PASS" : "FAIL";

                return new ProcessingResult(
                    fileName, true,
                    studentId + " - " + result, score
                );

            } catch (NoSuchFileException e) {
                return new ProcessingResult(
                    fileName, false, "Missing file", -1
                );

            } catch (IOException e) {
                return new ProcessingResult(
                    fileName, false, "File reading error: " + e.getMessage(), -1
                );

            } catch (IllegalArgumentException e) {
                return new ProcessingResult(
                    fileName, false, "Corrupt/invalid file: " + e.getMessage(), -1
                );

            } catch (Exception e) {
                return new ProcessingResult(
                    fileName, false, "Unexpected error: " + e.getMessage(), -1
                );
            }
        }
    }

    // Creates a clean dataset of 10,000 expected files.
    // 9,850 valid + 100 corrupt + 50 missing.
    private static void generateLargeDataset() throws IOException {
        Files.createDirectories(DATA_DIR);

        try (var paths = Files.list(DATA_DIR)) {
            paths.forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        for (int i = 1; i <= VALID_FILES; i++) {
            String studentId = String.format("STU%05d", i);
            int score = 40 + (i % 61);
            String content = studentId + "," + score;

            Files.writeString(
                DATA_DIR.resolve(String.format("score_%05d.txt", i)),
                content
            );
        }

        for (int i = VALID_FILES + 1;
             i <= VALID_FILES + CORRUPT_FILES; i++) {

            Files.writeString(
                DATA_DIR.resolve(String.format("score_%05d.txt", i)),
                "CORRUPTED DATA"
            );
        }

        // The final 50 expected files are deliberately not created.
        System.out.println("Dataset created: " + TOTAL_FILES + " expected files");
    }

    // Builds the list of all expected file paths, including missing files.
    private static List<Path> getExpectedFiles(Path directory) {
        List<Path> files = new ArrayList<>();

        for (int i = 1; i <= TOTAL_FILES; i++) {
            files.add(directory.resolve(
                String.format("score_%05d.txt", i)
            ));
        }

        return files;
    }

    // Processes every expected file using a fixed thread pool.
    private static void processFiles(List<Path> files, int poolSize)
            throws InterruptedException {

        ConcurrentLinkedQueue<ProcessingResult> results =
            new ConcurrentLinkedQueue<>();

        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        List<Future<ProcessingResult>> futures = new ArrayList<>();

        long startTime = System.nanoTime();

        try {
            // Submit all independent file-processing tasks.
            for (Path file : files) {
                futures.add(executor.submit(new ScoreTask(file)));
            }

            // Collect every task's outcome.
            for (int i = 0; i < futures.size(); i++) {
                try {
                    results.add(futures.get(i).get());

                } catch (ExecutionException e) {
                    results.add(new ProcessingResult(
                        files.get(i).getFileName().toString(),
                        false,
                        "Unexpected task failure: " + e.getCause(),
                        -1
                    ));
                }
            }

        } finally {
            executor.shutdown();

            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                executor.shutdownNow();
                executor.awaitTermination(1, TimeUnit.MINUTES);
            }
        }

        long endTime = System.nanoTime();

        int successes = 0;
        int failures = 0;
        int missing = 0;
        int corrupt = 0;

        for (ProcessingResult result : results) {
            if (result.successful) {
                successes++;
            } else {
                failures++;

                if (result.message.equals("Missing file")) {
                    missing++;
                } else {
                    corrupt++;
                }
            }
        }

        System.out.println("\n===== POOL SIZE: " + poolSize + " =====");
        System.out.println("Expected files: " + files.size());
        System.out.println("Successful files: " + successes);
        System.out.println("Failed files: " + failures);
        System.out.println("  Missing files: " + missing);
        System.out.println("  Corrupt/other files: " + corrupt);
        System.out.println("Total outcomes: " + results.size());
        System.out.printf("Processing time: %.3f ms%n",
            (endTime - startTime) / 1_000_000.0);

        if (successes + failures != files.size()
                || results.size() != files.size()) {
            System.out.println("WARNING: Summary count mismatch!");
        } else {
            System.out.println("Summary verification: PASSED");
        }
    }

    // Four-file hand-checked test: 2 valid, 1 corrupt, 1 missing.
    private static void runSmallTest() throws IOException, InterruptedException {
        Path smallDir = Paths.get("small_test");
        Files.createDirectories(smallDir);

        Files.writeString(smallDir.resolve("student1.txt"), "STU001,85");
        Files.writeString(smallDir.resolve("student2.txt"), "STU002,45");
        Files.writeString(smallDir.resolve("student3.txt"), "STU003,INVALID");
        // student4.txt is deliberately missing.

        List<Path> smallFiles = List.of(
            smallDir.resolve("student1.txt"),
            smallDir.resolve("student2.txt"),
            smallDir.resolve("student3.txt"),
            smallDir.resolve("student4.txt")
        );

        System.out.println("\n===== SMALL HAND-CHECKED TEST =====");

        ConcurrentLinkedQueue<ProcessingResult> results =
            new ConcurrentLinkedQueue<>();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<Future<ProcessingResult>> futures = new ArrayList<>();

        try {
            for (Path file : smallFiles) {
                futures.add(executor.submit(new ScoreTask(file)));
            }

            for (Future<ProcessingResult> future : futures) {
                try {
                    results.add(future.get());
                } catch (ExecutionException e) {
                    results.add(new ProcessingResult(
                        "Unexpected task", false,
                        "Unexpected task failure", -1
                    ));
                }
            }
        } finally {
            executor.shutdown();
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        }

        int success = 0;
        int failure = 0;

        for (ProcessingResult result : results) {
            System.out.println(
                result.fileName + " -> " + result.message
            );

            if (result.successful) {
                success++;
            } else {
                failure++;
            }
        }

        System.out.println("Small test successes: " + success + " (expected 2)");
        System.out.println("Small test failures: " + failure + " (expected 2)");
    }

    public static void main(String[] args) throws Exception {
        runSmallTest();

        System.out.println("\n===== GENERATING LARGE DATASET =====");
        generateLargeDataset();

        List<Path> expectedFiles = getExpectedFiles(DATA_DIR);

        System.out.println("\n===== PERFORMANCE MEASUREMENTS =====");

        int[] poolSizes = {1, 2, 4, 8};

        for (int poolSize : poolSizes) {
            processFiles(expectedFiles, poolSize);
        }

        System.out.println("\nAll processing experiments completed.");
    }
}
