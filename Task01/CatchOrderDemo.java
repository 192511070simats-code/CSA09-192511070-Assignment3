
public class CatchOrderDemo {

    public static void main(String[] args) {

        try {
            throw new java.io.IOException("Demo error");

        } catch (Exception e) {
            System.out.println("General exception");

        } catch (java.io.IOException e) {
            System.out.println("IO exception");
        }
    }
}
