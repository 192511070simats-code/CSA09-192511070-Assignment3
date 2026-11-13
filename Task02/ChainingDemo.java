
import java.io.IOException;

class ChainedExamException extends Exception {
    ChainedExamException(String message, Throwable cause) {
        super(message, cause);
    }
}

class ChainedStorageException extends ChainedExamException {
    ChainedStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}

public class ChainingDemo {

    public static void main(String[] args) {

        System.out.println("TEST 1: Exception with original cause");

        try {
            try {
                throw new IOException("Disk quota exceeded");
            } catch (IOException e) {
                throw new ChainedStorageException(
                    "Could not store answer.pdf", e
                );
            }

        } catch (ChainedExamException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cause: " + e.getCause().getMessage());

            System.out.println("\nFull chained stack trace:");
            e.printStackTrace(System.out);
        }

        System.out.println("\nTEST 2: Exception without original cause");

        ChainedStorageException e =
            new ChainedStorageException(
                "Could not store answer.pdf", null
            );

        System.out.println("Error: " + e.getMessage());
        System.out.println("Cause: " + e.getCause());
    }
}
