package helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;
import java.util.stream.Collectors;

public class Client extends Thread {

    private Socket clientSocket;

    private Map<String,String> redisStore;

    public Client(Socket clientSocket, Map<String,String> redisStore) {
        this.clientSocket = clientSocket;
        this.redisStore = redisStore;
    }

    @Override
    public void run() {
        try {
            if (clientSocket != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());

                String line;
                String temp = null;
                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println(line);
                    if (line.equals("ping")) {
                        dataOutputStream.writeBytes("+PONG\r\n");
                        dataOutputStream.flush();
                    }
                    if(line.equals("echo"))
                    {
                        String lengthOfString = bufferedReader.readLine();
                        String echoString = bufferedReader.readLine();
                        System.out.println(lengthOfString+echoString);
                        dataOutputStream.writeBytes("+"+echoString+"\r\n");
                        dataOutputStream.flush();
                    }
                    if(line.equals("set"))
                    {
                        String keyLength = bufferedReader.readLine();
                        String keyString = bufferedReader.readLine();

                        String valueLength = bufferedReader.readLine();
                        String valueString = bufferedReader.readLine();

                        this.redisStore.put(keyString,valueString);

                        dataOutputStream.writeBytes("+OK\r\n");
                        dataOutputStream.flush();
                    }
                    if(line.equals("get"))
                    {
                        String keyLength = bufferedReader.readLine();
                        String keyString = bufferedReader.readLine();

                        dataOutputStream.writeBytes("+"+this.redisStore.get(keyString)+"\r\n");
                        dataOutputStream.flush();
                    }
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
