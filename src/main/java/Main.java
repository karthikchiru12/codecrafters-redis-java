import helper.Client;
import helper.Server;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) {

        int port = 6379;
        boolean reuseAddress = true;
        ServerSocket serverSocket = null;

        try {
            serverSocket = Server.getServerSocket(port, reuseAddress);

            while (true) {
                Client client = new Client(serverSocket.accept());
                client.start();
            }

        } catch (IOException ioException) {
            System.out.println("IOException occurred : " + ioException.getMessage());
        } catch (Exception exception) {
            System.out.println("Exception occurred : " + exception.getMessage());
        }

    }
}
