package helper;

import java.io.IOException;
import java.net.ServerSocket;

public class SocketCreator {
    private static ServerSocket serverSocket = null;

    public static ServerSocket getServerSocket(int port, boolean reuseAddress)
    {
        if(serverSocket == null)
        {
            try
            {
                serverSocket = new ServerSocket(port);
                serverSocket.setReuseAddress(reuseAddress);
            }
            catch(Exception exception)
            {
                System.out.println("Exception occurred : " + exception.getMessage());
            }

        }
        return serverSocket;
    }



}
