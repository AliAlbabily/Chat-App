package client.controller;

import client.model.Client;
import client.model.Contacts;
import sharedResources.Message;
import sharedResources.User;
import client.view.ButtonType;
import client.view.ClientLoginGUI;
import client.view.ClientMainGUI;

import javax.swing.*;
import java.util.ArrayList;

public class Controller {

    private ClientMainGUI clientMainGUI; // client main-view
    private final ClientLoginGUI clientLoginGUI; // client login-view
    private User user;
    private Client client;
    private Contacts contacts;
    private ArrayList<User> selectedUsers = new ArrayList<>(); // receivers

    public Controller() {
        clientLoginGUI = new ClientLoginGUI(this);
        contacts = new Contacts();
    }

    public void buttonPressed(ButtonType button) {
        switch (button) {
            case Connect:
                String username = clientLoginGUI.getUsername(); // Hämtar username från GUI
                ImageIcon imageIcon = clientLoginGUI.getImageIcon(); // Hämtar imageIcon från GUI
                user = new User(username, imageIcon); // skapar ny User-instans

                client = new Client("127.0.0.1", 2343, this); // skapar ny Client-instans

                clientMainGUI = new ClientMainGUI(this); // öppna klient fönstret
                clientLoginGUI.closeClientConnectionWindow(); //Stänger ner inloggningsfönster

                ArrayList<User> savedContacts = contacts.fetchContactsFromFile(); //Laddar upp alla sparade kontakter från fil
                clientMainGUI.updateContactsJList(savedContacts); // display your contacts in GUI once you are logged in

                client.sendUserToServer(user); //Skickar skapade usern till servern
                break;
            case Send:
                String textMessage = clientMainGUI.getMessage(); //Hämtar message från clientMain gui
                User[] selectedUsersArr = selectedUsers.toArray(new User[0]); //Hämtar valda receivers som ska ta emot meddelande man skickar och sparar dem i array av User.
                ImageIcon uploadedImage = clientMainGUI.getUploadedImage(); //Hämtar den valda bilden som ska skickas.

                Message newMessage = new Message(user, textMessage, uploadedImage, selectedUsersArr); // Skapar ett nytt message objekt
                client.sendMessageToServer(newMessage); //Skickar message objektet till servern
                break;
            default:
                System.out.println("Something went wrong!");
                break;
        }
    }

    public void updateOnlineUsersListGUI(User[] onlineUsers) {
        clientMainGUI.updateOnlineJList(onlineUsers); // updatera onlineUsers-list in client main view
    }

    public void updateChatGUI(User user, String message) { //Lägger till ett nytt chattmeddelande i chatten
        clientMainGUI.updateChat(user, message);
    }

    public void updateChatGUI(User user, String textMessage, ImageIcon Image) { //Lägger till ett nytt chattmeddelande i chatten
        clientMainGUI.updateChat(user, textMessage, Image);
    }

    public void updateChatGUI(User user, ImageIcon Image) { //Lägger till ett nytt chattmeddelande i chatten
        clientMainGUI.updateChat(user, Image);
    }

    public void saveNewContact(User user) { //sparar ny kontakt, sparas i fil.
        contacts.saveNewContactInFile(user);
    }

    public void removeContact(User user) { //tar bort kontakt från fil
        contacts.removeContactInFile(user);
    }

    public void addSelectedUserToReceivers(User selectedUser) {
        selectedUsers.add(selectedUser); //lägg till den valda usern i vår receiver array
    }

    public void clearAllSelectedReceivers() {
        selectedUsers.clear(); //Ta bort alla valda receivers
    }

    //<editor-fold desc="Getters and setters">
    public User getUser() {
        return user;
    } // Hämta user

    public ArrayList<User> getSelectedUsers() {
        return selectedUsers;
    } //Hämta valda users / receivers

    public Contacts getContacts() {
        return contacts;
    } //Hämta kontakterna.
    //</editor-fold>
}
