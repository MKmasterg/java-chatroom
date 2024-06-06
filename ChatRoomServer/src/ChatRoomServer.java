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
        // Starting serverSocket
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
        } catch (Exception e){
            System.out.println("Something went wrong in creating server socket!");
        }
        // Listening on the port for any clients who want ot join
        while (true){
            //TODO maybe handle the situation when servers shuts down
            System.out.println("Listening for new connection...");
            try {
                Socket socket = serverSocket.accept();

                // When a user tries to connect these codes will execute
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String userInterface = reader.readLine();

                // Creating an object from ClientHandler
                ClientHandler clientHandler = new ClientHandler(socket,this,userInterface);

                System.out.println("User " + clientHandler.getUserInterface() + " connected successfully!");

                // Sends out a broadcast message about joining the client
                broadCastMessage("User " + clientHandler.getUserInterface() + " joined the chat!", clientHandler);
                clients.add(clientHandler);

                new Thread(clientHandler).start();
            } catch (Exception e){
                System.out.println("Something went wrong in creating client socket!");
            }
        }
    }

    public void broadCastMessage(String message, ClientHandler sender){
        // Sends out a message to all the clients that are registered on the server
        for (ClientHandler client : clients){
            if(client != sender){
                client.sendMessage(sender.getUserInterface() + " : " +  message);
            } else {
                client.sendMessage("You : " + message);
            }
        }
    }

    public void removeClient(ClientHandler client){
        // Removes the client from the clients array
        clients.remove(client);
        System.out.println("User " + client.getUserInterface() + " removed successfully!");
    }

    public String showConnectedClients(ClientHandler currentClient) {
        // Returns a string of all connected clients on the server
        StringBuilder message = new StringBuilder();
        message.append("Connected users : \n");
        for (ClientHandler client : clients){
            if(client == currentClient){
                message.append(client.getUserInterface()).append("(You)\n");
            } else{
                message.append(client.getUserInterface()).append("\n");
            }
        }
        message.append("----------\n");
        return message.toString();
    }

    public boolean isInterfaceExists(String userInterface) {
        // Checks if an interface exists in the clients array
        for(ClientHandler client : clients){
            if(client.getUserInterface().equals(userInterface)){
                return true;
            }
        }
        return false;
    }

    public int getNumberOfClients() {
        return clients.size();
    }
}
