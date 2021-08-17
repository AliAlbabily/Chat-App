package model;

import controller.Controller;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Client extends Thread {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private ArrayList<User> onlineUsers = new ArrayList<>();

    private final Controller controller;

    public Client(String ipAddress, int port, Controller controller) {
        this.controller = controller;
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

            while (true) {
                try {
                    Object objReceived = ois.readObject();

                    if(objReceived instanceof ArrayList) {
                        onlineUsers = (ArrayList<User>) objReceived;
                        User[] arrayUsers = onlineUsers.toArray(new User[0]); // konvertera arraylist till vanlig array
                        controller.updateOnlineUsersListGUI(arrayUsers);
                    }
                    else if(objReceived instanceof Message) {
                        Message messageReceived = (Message)objReceived;
                        System.out.println(messageReceived.GetUser().toString() + ": " + messageReceived.getMessage());
                        Message messageWithTime = getReceivedByClientTime(messageReceived);
                        updateChat(messageWithTime);
                    }

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        }
    }

    public void sendUserToServer(User newUser) { //Skickar värdet "user" till servern. Som sedan servern tar emot.
        try {
            oos.writeObject(newUser);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToServer(Message message) { //Skickar värdet "message" till servern. Som sedan servern tar emot.
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateChat(Message message) {
        if (message.getMessage().equals("") && message.getSentImage() == null) { // no text && no image
            // JOptionPane.showMessageDialog(null,"Du måste skriva något för att skicka.");
            System.out.println("You can't send an empty message!");
        }
        else if (!message.getMessage().equals("") && message.getSentImage() != null) { // text exists && image exists
            controller.updateChatGUI(message.GetUser(), message.getMessage(), message.getSentImage());
        }
        else if (!message.getMessage().equals("") && message.getSentImage() == null) { // text exists && no image
            controller.updateChatGUI(message.GetUser(), message.getMessage());
        }
        else if (message.getMessage().equals("") && message.getSentImage() != null) { // no text && image exists
            controller.updateChatGUI(message.GetUser(), message.getSentImage());
        }
    }

    private Message getReceivedByClientTime(Message message) {
        LocalDateTime now = LocalDateTime.now(); // time now
        message.setTimeReceivedByClient(now); // set a new time to the message
        return message;
    }
}
