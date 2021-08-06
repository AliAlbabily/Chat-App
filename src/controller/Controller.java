package controller;

import model.*;
import view.ButtonType;
import view.ClientGUI;
import view.ClientMainGUI;

import javax.swing.*;

public class Controller {

    private ClientMainGUI clientMainGUI; // client main view
    private final ClientGUI clientGUI; // client connect view
    private User user;
    private Client client;

    public Controller() {
         clientGUI = new ClientGUI(this);
    }

    public void buttonPressed(ButtonType button) {

        switch (button) {
            case Connect:
                String username = clientGUI.getUsername();
                ImageIcon imageIcon = clientGUI.getImageIcon();
                user = new User(username, imageIcon); // skapar ny User-instans

                client = new Client("25.82.118.228", 2343, this); // skapar ny Client-instans

                clientMainGUI = new ClientMainGUI(this, user); // öppna klient fönstret
                clientGUI.closeClientConnectionWindow();

                client.sendUserToServer(user);
                break;
            case Send:
                String textMessage = clientMainGUI.getMessage();
                User[] selectedUsers = clientMainGUI.getSelectedUsers().toArray(new User[0]);
                ImageIcon uploadedImage = clientMainGUI.getUploadedImage();

                Message newMessage = new Message(user, textMessage, uploadedImage, selectedUsers); // create a new Message-object
                client.sendMessageToServer(newMessage);
                break;
            default:
                System.out.println("Något gick fel!");
                break;
        }
    }

    public void updateOnlineUsersListGUI(User[] onlineUsers) {
        clientMainGUI.updateOnlineJList(onlineUsers); // updatera onlineUsers-list in client main view
    }

    public void updateChatGUI(User user, String message) {
        clientMainGUI.updateChat(user, message);
    }

    public void updateChatGUI(User user, String textMessage, ImageIcon Image) {
        clientMainGUI.updateChat(user, textMessage, Image);
    }

    public void updateChatGUI(User user, ImageIcon Image) {
        clientMainGUI.updateChat(user, Image);
    }
}
