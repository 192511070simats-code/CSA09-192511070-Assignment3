
import java.io.IOException;

class ExamPortalException extends Exception {
    ExamPortalException(String message) {
        super(message);
    }

    ExamPortalException(String message, Throwable cause) {
        super(message, cause);
    }
}

class ExamWindowClosedException extends ExamPortalException {
    ExamWindowClosedException(String message) {
        super(message);
    }
}

class StudentNotRegisteredException extends ExamPortalException {
    StudentNotRegisteredException(String message) {
        super(message);
    }
}

class SubmissionStorageException extends ExamPortalException {
    SubmissionStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}

// Unchecked exception for invalid programmer/input usage
class InvalidSubmissionRequestException extends RuntimeException {
    InvalidSubmissionRequestException(String message) {
        super(message);
    }
}

class ExamStorage {
    void save(String fileName, long sizeKb)
            throws SubmissionStorageException {
        try {
            if (sizeKb > 500) {
                throw new IOException("Disk quota exceeded");
            }
            System.out.println("Submission saved: " + fileName);
        } catch (IOException e) {
            throw new SubmissionStorageException(
                "Could not store " + fileName, e
            );
        }
    }
}

class ExamService {
    ExamStorage storage = new ExamStorage();

    void submit(String fileName, long sizeKb,
                boolean examOpen, boolean registered)
                throws ExamPortalException {

        if (!examOpen) {
            throw new ExamWindowClosedException(
                "The examination window is closed."
            );
        }

        if (!registered) {
            throw new StudentNotRegisteredException(
                "Student is not registered for this course."
            );
        }

        if (sizeKb < 0) {
            throw new InvalidSubmissionRequestException(
                "File size cannot be negative."
            );
        }

        storage.save(fileName, sizeKb);
    }
}

class ExamController {
    ExamService service = new ExamService();

    void submitAnswer(String fileName, long sizeKb,
                      boolean examOpen, boolean registered) {
        try {
            service.submit(fileName, sizeKb, examOpen, registered);
        } catch (ExamPortalException e) {
            System.out.println("Student message: " + e.getMessage());

            if (e.getCause() != null) {
                System.out.println("Original cause: "
                    + e.getCause().getMessage());
            }
        }
    }
}

public class PortalExceptionDemo {
    public static void main(String[] args) {
        ExamController controller = new ExamController();

        System.out.println("TEST 1: Exam closed");
        controller.submitAnswer("answer01.pdf", 120, false, true);

        System.out.println("\nTEST 2: Student not registered");
        controller.submitAnswer("answer02.pdf", 120, true, false);

        System.out.println("\nTEST 3: File exceeds size limit");
        controller.submitAnswer("answer03.pdf", 800, true, true);
    }
}
