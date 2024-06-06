
# ChatRoom Project
A basic text-based chatroom written in Java.

## Overview
The ChatRoom project is a simple client-server application for real-time text-based chat. It is divided into two main components:
- **ChatRoomServer**: Manages connections from multiple clients.
- **ChatRoomClient**: Allows users to connect to the server and chat with others.


### ChatRoomServer
- **ChatRoomServer.java**: Main server class.
- **ClientHandler.java**: Manages individual client connections.
- **Main.java**: Entry point to start the server.

### ChatRoomClient
- **Client.java**: Main client class for handling connections and user input/output.
- **Main.java**: Entry point to start the client.

## Getting Started

### Prerequisites
- Java Development Kit (JDK) installed (preferably JDK 8 or higher).
- A terminal or command prompt.

### Setting Up the Server
1. Navigate to the `ChatRoomServer` directory:
   ```sh
   cd ChatRoom/ChatRoomServer
   ```
2.   Compile the server code.
3. Run the server.

### Setting Up the Client
1. Navigate to the `ChatRoomClient` directory.
2.   Compile the server code.
3. Run the server.

## Usage
 1. Start the server first to listen for client connections.
 2. Start one or more clients to connect to the server.
 3. Clients can send messages which will be broadcast to all connected clients.
 
*This is a part of the Advanced Programming course assignment at Amirkabir University of Technology.*
