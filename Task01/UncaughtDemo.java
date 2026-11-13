
public class UncaughtDemo {

    static void readAccount() {
        throw new IllegalArgumentException(
            "Demo: invalid account number"
        );
    }

    public static void main(String[] args) {
        readAccount();
    }
}
