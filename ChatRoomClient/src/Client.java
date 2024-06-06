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
    private Thread serverMessageThread;
    private Scanner scanner;
    private boolean interrupt = false;

    public Client(String hostName, int port, String userInterface) throws Exception{
        try {
            socket = new Socket(hostName, port);
            printWriter = new PrintWriter(socket.getOutputStream(),true);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userInterface = userInterface;

            // Sending the userInterface to the server
            printWriter.println(this.userInterface);
            System.out.println("Connected to the server " + hostName + " on port " + port);

            // Creating a new thread that handles messages from the server
            serverMessageThread =  new Thread(this::handleServerMessages);
            serverMessageThread.start();

            scanner = new Scanner(System.in);
        } catch (Exception e){
            System.out.println("Something went wrong in connecting to the server " + hostName);
            throw new Exception("Client couldn't get established.");
        }

        // Add shutdown hook
        // https://www.geeksforgeeks.org/jvm-shutdown-hook-java/
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Signals to the server so that this client gets removed from server registry
                printWriter.println("QUIT");
            }
        });
    }

    public void run() {
        String message;
        while ( !interrupt && (message = scanner.nextLine()) != null){
            // Send the message to the server
            printWriter.println(message);

            // Handling the interruption keyword
            if(message.equals("QUIT")){
                serverMessageThread.interrupt();
                interrupt = true;
                try {
                    socket.close();
                    System.out.println("Connection closed successfully!");
                } catch (Exception e){
                    System.out.println("Something went wrong in closing the socket!");
                }
                break;
            }
        }
    }

    private void handleServerMessages(){
        String serverMessage;
        try {
            while ((serverMessage = bufferedReader.readLine()) != null){
                // Gets the server message and prints out to the terminal
                System.out.println(serverMessage);
            }
        } catch (Exception e){
            if(!socket.isClosed())
                System.out.println("Something went wrong in reading message from server!");
        }
    }

}
