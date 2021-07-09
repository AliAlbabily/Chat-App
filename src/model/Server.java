package model;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Server {

    private Clients globalClientsObj = new Clients();

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
                        socket = serverSocket.accept(); // Lyssna efter anslutande klient

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

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            System.out.println("A new client has connected to the server!");
        }

        public void run() {
            try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());)
            {
                while(true) {

                    Object objRecieved = ois.readObject();

                    if(objRecieved instanceof User) {
                        // ClientHandler behöver sparas i hashmapen till sin motsvarande User
                        globalClientsObj.put((User)objRecieved, this); // FIXME : osäkert på om "this" referar till ClientHandler eller inte

                        System.out.println(globalClientsObj.getHashMapList().size());

                        // FIXME the 2 lines below are bugged !!
//                        oos.writeObject(globalClientsObj);
//                        oos.flush();
                        //
                    }

                }
            }
            catch (IOException | ClassNotFoundException e) {
                try {
                    socket.close();
                    System.out.println("Klient nerkopplad");
                } catch (Exception e2) {}
            }
        }
    }

    public static void main(String[] args) {
        try {
            new Server(2343);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}