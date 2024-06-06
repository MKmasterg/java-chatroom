import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please provide a name : ");
        String userInterface = scanner.nextLine();
        new Client("localhost",4444,userInterface);
    }
}