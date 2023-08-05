package helper;

import java.net.ServerSocket;

public class Server {
    private static ServerSocket serverSocket = null;

    public static ServerSocket getServerSocket(int port, boolean reuseAddress) {
        if (serverSocket == null) {
            try {
                serverSocket = new ServerSocket(port);
                serverSocket.setReuseAddress(reuseAddress);
            } catch (Exception exception) {
                System.out.println("Exception occurred : " + exception.getMessage());
            }

        }
        return serverSocket;
    }

}
