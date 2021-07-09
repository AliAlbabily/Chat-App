package model;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class Client extends Thread {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private Clients localClientsObj = new Clients();

    public Client(String ipAddress, int port) {
        System.out.println("Establishing connection. Please wait...");
        try {
            socket = new Socket(ipAddress,port);
            oos = new ObjectOutputStream( new BufferedOutputStream(socket.getOutputStream()) );
            ois = new ObjectInputStream( new BufferedInputStream(socket.getInputStream()) );
            System.out.println("Connected!");
        } catch (IOException e) {
            System.out.println("Could not connect");
            e.printStackTrace();
        }
        start();
    }

    public void run() {
        while(!Thread.interrupted()) {
            System.out.println("Running");

            // TODO : test
//            try {
//                while (true) {
//                    Object objRecieved = ois.readObject();
//
//                    if(objRecieved instanceof Clients) {
//                        System.out.println("Clients obj recieved");
//                    }
//                }
//            } catch (IOException | ClassNotFoundException e) {
//                e.printStackTrace();
//            }

        }
    }

    public void sendUserToServer(User user) {
        try {
            oos.writeObject(user);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}