package Main;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Application {
    public static final int port = 25655;
    private ServerSocket serverSocket;
    private static String chatText = "";
    private ArrayList<Client> clients;
    public static final String localIP = "127.0.0.1";
    private ArrayList<Thread> threads = new ArrayList<>();

    public Server() throws IOException {

    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }


    private void addClient(Socket socket) throws IOException {

        Client client = new Client(socket );
        try {
            readchatText(client);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        clients.add(client);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        serverSocket = new ServerSocket(port);
        clients = new ArrayList<>();
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        System.out.println("waiting");
                        Socket newClientSocket = serverSocket.accept();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION , "ali joined");
                        alert.show();

                        addClient(newClientSocket);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        threads.add(thread);
        thread.start();

    }

    public void readchatText(Client client) throws IOException, ClassNotFoundException {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String text = "";
                        synchronized (client.getObjectInputStream()) {
                            System.out.println("reading from this socket:        " + client.getSocket());
                             text = ((String) client.getObjectInputStream().readObject());
                        }
                            addText(text);
                            sendchatMessage();
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

    public void sendchatMessage() {
        for (int i = 0; i < clients.size(); i++) {
            try {
                clients.get(i).getObjectOutputStream().reset();
                clients.get(i).getObjectOutputStream().writeObject(chatText);
                clients.get(i).getObjectOutputStream().flush();
                System.out.println("sending: ... " + chatText);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static void addText(String text) {
            if (!chatText.equals(""))
                chatText = chatText + "\n\n" + text;
            else
                chatText = text;


    }



    public static void sendMessage(String chatText){
        addText(chatText);
    }



}
