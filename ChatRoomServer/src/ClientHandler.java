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

    public ClientHandler(Socket socket, ChatRoomServer server, String userInterface) {
        this.socket = socket;
        this.server = server;
        // TODO handle duplicate interfaces
        this.userInterface = userInterface;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printWriter = new PrintWriter(socket.getOutputStream(),true);
        } catch (Exception e){
            System.out.println("Something went wrong in client initialization of " + this.userInterface);
        }
    }

    public void sendMessage(String message) {
        printWriter.println(this.userInterface + " : " + message);
    }

    public String getUserInterface() {
        return userInterface;
    }

    @Override
    public void run() {
        String message;
        while (true){
            try {
                message = bufferedReader.readLine();
                if(message.equals("QUIT")){
                    server.removeClient(this);
                    break;
                }
                server.broadCastMessage(message, this);
            } catch (Exception e){
                System.out.println("Something went wrong in getting message from user " + this.userInterface);
                if(socket.isClosed()) {
                    System.out.println("Connection is closed, removing user " + this.userInterface);
                    server.removeClient(this);
                    break;
                }
            }
        }
    }
}
