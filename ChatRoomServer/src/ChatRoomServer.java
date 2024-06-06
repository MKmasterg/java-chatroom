import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatRoomServer {
    private int port;
    private ArrayList<ClientHandler> clients = new ArrayList<>();

    public ChatRoomServer(int port){
        this.port = port;
        System.out.println("Object created successfully, now you may start the server...");
    }

    public void start() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
        } catch (Exception e){
            System.out.println("Something went wrong in creating server socket!");
        }
        while (true){
            System.out.println("Listening for new connection...");
            try {
                Socket socket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String userInterface = reader.readLine();
                ClientHandler clientHandler = new ClientHandler(socket,this,userInterface);
                System.out.println("User : " + userInterface + " connected successfully!");
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            } catch (Exception e){
                System.out.println("Something went wrong in creating client socket!");
            }
        }
    }

    public void broadCastMessage(String message, ClientHandler sender){
        for (ClientHandler client : clients){
            if(client != sender){
                client.sendMessage(message);
            }
        }
    }

    public void removeClient(ClientHandler client){
        clients.remove(client);
        System.out.println("User " + client.getUserInterface() + " removed successfully!");
    }

}
