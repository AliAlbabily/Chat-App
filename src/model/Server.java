package model;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private Clients globalOnlineUsers = new Clients(); // lista av anslutna användare (online users)

    public Server(int port) throws IOException {
        new Connection(port).start();
    }

    private class Connection extends Thread {
        private int port;

        public Connection(int port) {
            this.port = port;
        }

        public void run() {
            Socket socket = null;
            System.out.println("Server startad");
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while(true) {
                    try {
                        socket = serverSocket.accept(); // Lyssna efter anslutande klienter
                        new ClientHandler(socket).start();
                    } catch(IOException e) {
                        System.err.println(e);
                        if(socket != null)
                            socket.close();
                    }
                }
            } catch(IOException e) {
                System.err.println(e);
            }
            System.out.println("Server stoppad");
        }
    }

    public class ClientHandler extends Thread {
        private Socket socket;
        ObjectOutputStream oos;
        ObjectInputStream ois;

        User key = null;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            System.out.println("A new client has connected to the server!");
        }

        public void run() {
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());

                while(true) {
                    Object objRecieved = ois.readObject();

                    if(objRecieved instanceof User) {
                        key = (User)objRecieved;
                        globalOnlineUsers.put((User)objRecieved, this); // spara en referens av ClientHandler i hashmapen med sin motsvarande User
                        notifyAllClients(objRecieved);
                    }

                }
            }
            catch (IOException | ClassNotFoundException e) {
                try {
                    socket.close();
                    System.out.println("Client disconnected!");
                    globalOnlineUsers.getHashMapList().remove(key); // uppdatera listan av anslutna användare när någon kopplar av
                } catch (Exception e2) {}
            }
        }

        public ObjectOutputStream getOos() {
            return oos;
        }
    }

    public static void main(String[] args) {
        try {
            new Server(2343);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // meddela andra klienter om en ny ansluten användare
    private void notifyAllClients(Object user) throws IOException {
        // få tag på alla ClientHandlers i hashmappen
        for ( User key : globalOnlineUsers.getHashMapList().keySet() ) {
            ClientHandler client = globalOnlineUsers.getHashMapList().get(key);
            // Skicka den nya koppplade user till alla klienter
            client.getOos().writeObject( user );
            client.getOos().flush();
        }
    }
}