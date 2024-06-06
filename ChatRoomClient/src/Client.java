import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Client {
    private String userInterface;
    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private Scanner scanner;

    public Client(String hostName, int port, String userInterface){
        try {
            socket = new Socket(hostName, port);
            printWriter = new PrintWriter(socket.getOutputStream(),true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userInterface = userInterface;
            printWriter.println(this.userInterface);
            System.out.println("Connected to the server " + hostName + " on port " + port);
            new Thread(this::handleServerMessages);
            scanner = new Scanner(System.in);
            String message;
            while ((message = scanner.nextLine()) != null){
                printWriter.println(message);
            }
        } catch (Exception e){
            System.out.println("Something went wrong in connecting to the server " + hostName);
        }
    }
    private void handleServerMessages(){
        String serverMessage;
        try {
            while ((serverMessage = bufferedReader.readLine()) != null){
                System.out.println(serverMessage);
            }
        } catch (Exception e){
            System.out.println("Something went wrong in reading message from server!");
        }
    }

}
