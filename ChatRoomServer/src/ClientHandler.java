import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket socket;
    private ChatRoomServer server;
    private String userInterface;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;
    private boolean dupFlag = false;
    private boolean interruptFlag = false ;

    public ClientHandler(Socket socket, ChatRoomServer server, String userInterface) {
        this.socket = socket;
        this.server = server;
        if(server.isInterfaceExists(userInterface)){
            // If the userInterface was already existed in the clients array then the server will assign a default to the user
            this.userInterface = "User" + server.getNumberOfClients();
            this.dupFlag = true;
        } else
            this.userInterface = userInterface;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(),true);
        } catch (Exception e){
            System.out.println("Something went wrong in client initialization of " + this.userInterface);
        }
    }

    public void sendMessage(String message) {
        printWriter.println(message);
    }

    public String getUserInterface() {
        return userInterface;
    }

    @Override
    public void run() {
        if(dupFlag){
            sendMessage("The name you've entered was already signed in the server;\nTherefore you've been registered under a default name by the server.");
        }
        // Showing connected people in the chat for the new connected client
        sendMessage(server.showConnectedClients(this));
        String message;
        while (true){
            try {
                message = bufferedReader.readLine();
                // Server log
                System.out.println("Messagse from user " + this.userInterface + " : " + message);
                if(message.equals("QUIT")){
                    server.removeClient(this);
                    // Sends out a message to all the clients connected to the server
                    server.broadCastMessage("User " + this.userInterface + " left the chat!", this);
                    break;
                }
                server.broadCastMessage(message, this);
            } catch (Exception e){
                System.out.println("Something went wrong in getting message from user " + this.userInterface);
                if(socket.isClosed()) {
                    System.out.println("Connection is closed, removing user " + this.userInterface);
                    server.removeClient(this);
                    break;
                } else if (interruptFlag){
                    break;
                }
            }
        }
    }
}
