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

                client = new Client("127.0.0.1", 2343, this); // skapar ny Client-instans

                clientMainGUI = new ClientMainGUI(this); // öppna klient fönstret
                clientGUI.closeClientConnectionWindow();

                client.sendUserToServer(user);
                break;
            case Send:

                String textMessage = clientMainGUI.getMessage();
                User[] allOnlineUsers = client.getAllOnlineUsers(); // TODO: just nu får man alla onlineUsers (alltså inte de som man väler)!!
                //ImageIcon Image = clientMainGUI.getImageIcon();

                Message newMessage = new Message(user, textMessage, null, allOnlineUsers); // create a new Message-object
                client.sendMessageToServer(newMessage);






//                if (message == null || Image == null) {
//                    JOptionPane.showMessageDialog(null,"Du måste skriva något för att skicka.");
//                    break;
//                }
//                if (Image == null && message != null) {
//                    clientMainGUI.updateChat(message,user);
//                }
//                else if (Image != null && message != null) {
//                    clientMainGUI.updateChat(message,user,Image);
//                }
//                else if (Image != null && message == null) {
//                    clientMainGUI.updateChat(user, Image);
//                }

                break;
            default:
                System.out.println("Något gick fel!");
                break;
        }
    }

    public void updateOnlineUsersListGUI(User[] onlineUsers) {
         clientMainGUI.updateOnlineJList(onlineUsers); // updatera onlineUsers-list in client main view
    }

    public void updateChatGUI(Message message) {
        clientMainGUI.updateChat(message.getMessage(), message.GetUser());
    }
}
