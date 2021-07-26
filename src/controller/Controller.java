package controller;

import model.*;
import view.ButtonType;
import view.ClientGUI;
import view.ClientMainGUI;

import javax.swing.*;

public class Controller {

    ClientMainGUI clientMainGUI; // client main view
    ClientGUI clientGUI; // client connect view

    public Controller() {
         clientGUI = new ClientGUI(this);
    }

    public void buttonPressed(ButtonType button) {

        switch (button) {
            case Connect:
                String username = clientGUI.getUsername();
                ImageIcon imageIcon = clientGUI.getImageIcon();
                User newUser = new User(username, imageIcon); // skapar ny User-instans

                Client newClient = new Client("127.0.0.1", 2343, this); // skapar ny Client-instans

                clientMainGUI = new ClientMainGUI(this); // öppna klient fönstret
                clientGUI.closeClientConnectionWindow();

                newClient.sendUserToServer(newUser);
                break;
            case Send:

                // TODO : testa att skicka något till alla klienter (på global nivå)
                //Anrop till chatthämtning
//                String message = clientMainGUI.getMessage();
//                ImageIcon Image = clientMainGUI.getImageIcon();
//
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
}
