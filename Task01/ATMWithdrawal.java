
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.InputMismatchException;
import java.util.Scanner;

// User-defined checked exception
class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

public class ATMWithdrawal {

    static double balance = 5000.00;

    static void withdraw(double amount)
            throws InsufficientFundsException {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Withdrawal amount must be positive."
            );
        }

        if (amount > balance) {
            throw new InsufficientFundsException(
                "Insufficient account balance."
            );
        }

        balance -= amount;

        System.out.printf(
            "Withdrawal successful. Remaining balance: %.2f%n",
            balance
        );
    }

    public static void main(String[] args) {

        System.out.println("BANK ATM SIMULATION");

        try {
            // Simulate reading the card-reader file
            String cardStatus = Files.readString(
                Path.of("card-reader.txt")
            ).trim();

            if (!cardStatus.equals("CARD-READY")) {
                throw new IOException("Card reader is not ready.");
            }

            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter account number: ");
            long accountNumber = Long.parseLong(scanner.next());

            if (accountNumber <= 0) {
                throw new IllegalArgumentException(
                    "Invalid account number."
                );
            }

            System.out.print("Enter withdrawal amount: ");
            int amount = scanner.nextInt();

            System.out.println("Account verified: " + accountNumber);

            withdraw(amount);

        } catch (InputMismatchException | NumberFormatException e) {
            System.out.println("Error: Please enter numeric values.");

        } catch (InsufficientFundsException e) {
            System.out.println("Error: " + e.getMessage());

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());

        } catch (IOException e) {
            System.out.println("Card reader error: " + e.getMessage());

        } finally {
            System.out.println("ATM session ended.");
        }
    }
}
