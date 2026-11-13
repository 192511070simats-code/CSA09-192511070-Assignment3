
import java.util.Scanner;

public class ATMErrorCodeDemo {

    static double balance = 5000.00;

    // Error codes:
    //  0 = Success
    // -1 = Invalid account number
    // -2 = Invalid withdrawal amount
    // -3 = Amount must be positive
    // -4 = Insufficient balance

    static int withdraw(long account, int amount) {

        if (account <= 0) {
            return -1;
        }

        if (amount <= 0) {
            return -3;
        }

        if (amount > balance) {
            return -4;
        }

        balance -= amount;
        return 0;
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter account number: ");

        if (!scanner.hasNextLong()) {
            System.out.println("Error: Invalid account number.");
            return;
        }

        long account = scanner.nextLong();

        System.out.print("Enter withdrawal amount: ");

        if (!scanner.hasNextInt()) {
            System.out.println("Error: Invalid withdrawal amount.");
            return;
        }

        int amount = scanner.nextInt();

        int result = withdraw(account, amount);

        switch (result) {
            case 0:
                System.out.printf(
                    "Withdrawal successful. Remaining balance: %.2f%n",
                    balance
                );
                break;
            case -1:
                System.out.println("Error: Invalid account number.");
                break;
            case -2:
                System.out.println("Error: Invalid withdrawal amount.");
                break;
            case -3:
                System.out.println("Error: Amount must be positive.");
                break;
            case -4:
                System.out.println("Error: Insufficient balance.");
                break;
        }

        scanner.close();
    }
}
