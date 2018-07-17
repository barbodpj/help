package Main;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;

public class Client {
    private Socket socket = new Socket();
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private String name;
    private IntegerProperty goldProperty = new SimpleIntegerProperty(0);
    private IntegerProperty elixirProperty = new SimpleIntegerProperty(0);
    private String chatText = "";
    private ArrayList<Thread> threads = new ArrayList<>();
    boolean flag = true;
    public String getName() {
        return name;
    }
    public Socket getSocket() {

        return socket;
    }

    public String getChatText() {
        return chatText;

    }

    @Override
    public String toString() {
        return socket.toString();
    }

    public void setGoldProperty(int goldProperty) {
        this.goldProperty.set(goldProperty);
    }

    public void setElixirProperty(int elixirProperty) {
        this.elixirProperty.set(elixirProperty);
    }

    public Client(Socket socket  ) throws IOException {
        this.socket = socket;
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        this.objectOutputStream.reset();
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        this.name = name;
        getChat();

    }

    public void sendMessageToServer(String text ) throws IOException {
        System.out.println("sending from client: " + text);
        objectOutputStream.reset();
        objectOutputStream.writeObject(text);
//        Server.sendMessage(text);
        objectOutputStream.flush();
    }

    public void getChat()
    {
        Thread thread = new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
//                        synchronized (objectInputStream) {
                        String text = "";
                            try {
                            text = ((String) objectInputStream.readObject());
                            }catch (java.net.SocketException exception){
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(flag) {
                                            flag = false;
                                            Alert alert = new Alert(Alert.AlertType.ERROR, "server is down");
                                            Optional<ButtonType> optional = alert.showAndWait();
                                            if (optional.isPresent()) {
                                                System.exit(1);
                                            }
                                            System.exit(1);
                                            alert.show();
                                            Thread.currentThread().stop();

                                        }
                                    }
                                });

                            }
                               if(text !=null)
                                   chatText = text;

                                System.out.println(chatText);
//                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        threads.add(thread);
        thread.start();

    }


    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

}
