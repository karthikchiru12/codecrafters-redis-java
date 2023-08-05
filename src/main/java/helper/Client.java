package helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client extends Thread {

    private Socket clientSocket;

    public Client(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            if (clientSocket != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

                String line;
                int respResponseArrayLength = 0;
                boolean isRespArray = false;
                boolean isEcho = true;
                String echoResponse = "";
                while ((line = bufferedReader.readLine()) != null) {
                    if (line.startsWith("*")) {
                        isRespArray = true;
                    }
                    if (line.equals("ping")) {
                        dataOutputStream.writeBytes("+PONG\r\n");
                        dataOutputStream.flush();
                    }
                    if(isEcho)
                    {
                        echoResponse  = echoResponse + "$" + line.length() + line +"\r\n";
                        respResponseArrayLength += 1;
                    }
                    if (line.toLowerCase().equals("echo")) {
                        isEcho = true;
                    }
                    System.out.println(line);
                }
                if (isRespArray && isEcho) {
                    System.out.println(echoResponse);
                    dataOutputStream.writeBytes("*" + respResponseArrayLength + "\r\n" + echoResponse);
                    dataOutputStream.flush();
                }
                clientSocket.close();
            }
        } catch (IOException ioException) {
            System.out.println("IOException occurred : " + ioException.getMessage());
        } catch (Exception exception) {
            System.out.println("Exception occurred : " + exception.getMessage());
        }
    }
}
