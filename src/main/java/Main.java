import helper.Client;
import helper.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        int port = 6379;
        boolean reuseAddress = true;
        ServerSocket serverSocket = null;

        try {
            serverSocket = Server.getServerSocket(port, reuseAddress);
            Map<String,String> redisStore = new HashMap<>();

            while (true) {
                Client client = new Client(serverSocket.accept(),redisStore);
                client.start();
            }

        } catch (IOException ioException) {
            System.out.println("IOException occurred : " + ioException.getMessage());
        } catch (Exception exception) {
            System.out.println("Exception occurred : " + exception.getMessage());
        }

    }
}
