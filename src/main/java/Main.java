import helper.SocketCreator;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args){

    int port = 6379;
    boolean reuseAddress = true;
    ServerSocket serverSocket = null;
    Socket clientSocket = null;

    try
    {
      serverSocket = SocketCreator.getServerSocket(port,reuseAddress);
      clientSocket = serverSocket.accept();

      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

      while(bufferedReader.readLine() != null)
      {
        dataOutputStream.writeBytes("+PONG\r\n");
        dataOutputStream.flush();
      }
    }
    catch (IOException ioException)
    {
      System.out.println("IOException occurred : " + ioException.getMessage());
    }
    catch(Exception exception)
    {
      System.out.println("Exception occurred : " + exception.getMessage());
    }
    finally {
      try
      {
        if(clientSocket != null)
        {
          clientSocket.close();
        }
      }
      catch (IOException ioException)
      {
        System.out.println("IOException occurred : " + ioException.getMessage());
      }
      catch(Exception exception)
      {
        System.out.println("Exception occurred : " + exception.getMessage());
      }

    }

  }
}
