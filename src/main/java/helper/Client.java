package helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Client extends Thread {

    private Socket clientSocket;

    private Map<String, List<String>> redisStore;

    public Client(Socket clientSocket, Map<String, List<String>> redisStore) {
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
                    if (line.equals("echo")) {
                        String lengthOfString = bufferedReader.readLine();
                        String echoString = bufferedReader.readLine();
                        System.out.println(lengthOfString + echoString);
                        dataOutputStream.writeBytes("+" + echoString + "\r\n");
                        dataOutputStream.flush();
                    }
                    if (line.equals("set")) {
                        List<String> setList = new ArrayList<>();
                        String keyLength = bufferedReader.readLine();
                        String keyString = bufferedReader.readLine();

                        String valueLength = bufferedReader.readLine();
                        String valueString = bufferedReader.readLine();

                        setList.add(valueString);

                        if (bufferedReader.ready()) {
                            String args = bufferedReader.readLine();
                            if (args.equals("px")) {
                                String expiryInSeconds = bufferedReader.readLine();
                                setList.add(expiryInSeconds);
                                this.redisStore.put(keyString, setList);
                            }
                        }
                        else
                        {
                            setList.add("0");
                            this.redisStore.put(keyString, setList);
                        }
                        dataOutputStream.writeBytes("+OK\r\n");
                        dataOutputStream.flush();
                    }
                    if (line.equals("get")) {
                        String keyLength = bufferedReader.readLine();
                        String keyString = bufferedReader.readLine();

                        if(this.redisStore.containsKey(keyString))
                        {
                            System.out.println("Reaced here");
                            long currentTime = new Date().getTime();
                            long expiryTime = currentTime + Long.parseLong(this.redisStore.get(keyString).get(1));
                            System.out.println("Reaced here");
                            if (expiryTime != 0 || currentTime > expiryTime) {
                                dataOutputStream.writeBytes("$0\r\n\r\n");
                                dataOutputStream.flush();
                            } else {
                                System.out.println("Reaced here");
                                dataOutputStream.writeBytes("+" + this.redisStore.get(keyString).get(0) + "\r\n");
                                dataOutputStream.flush();
                            }
                        }
                        else
                        {
                            dataOutputStream.writeBytes("$0\r\n\r\n");
                            dataOutputStream.flush();
                        }


                    }
                }
                clientSocket.close();
            }
        } catch (IOException ioException) {
            System.out.println("IOException occurred : " + ioException);
        } catch (Exception exception) {
            System.out.println("Exception occurred : " + exception);
        }
    }
}
