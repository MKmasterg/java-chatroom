public class Main {
    private static final int port = 4444;
    public static void main(String[] args) {
        ChatRoomServer chatRoomServer = new ChatRoomServer(port);
        chatRoomServer.start();
    }
}
