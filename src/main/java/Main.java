import helper.SocketCreator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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

      DataInputStream dataInputStream = new DataInputStream(clientSocket.getInputStream());
      if(dataInputStream.readLine().equals("$4\r\nping\r\n"))
      {
        //Do nothing for now
      }
      DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
      dataOutputStream.writeBytes("+PONG\r\n");
      dataOutputStream.flush();

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
