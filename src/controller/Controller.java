package controller;

import model.*;
import view.ButtonType;
import view.ClientGUI;
import view.ClientMainGUI;

import javax.swing.*;
import java.util.ArrayList;

public class Controller {

    private ClientMainGUI clientMainGUI; // client main view
    private final ClientGUI clientGUI; // client connect view
    private User user;
    private Client client;
    private Contacts contacts;
    private ArrayList<User> selectedUsers = new ArrayList<>(); // receivers

    public Controller() {
        clientGUI = new ClientGUI(this);
        contacts = new Contacts();
    }

    public void buttonPressed(ButtonType button) {

        switch (button) {
            case Connect:
                String username = clientGUI.getUsername(); // Hämtar username från GUI
                ImageIcon imageIcon = clientGUI.getImageIcon(); // Hämtar imageIcon från GUI
                user = new User(username, imageIcon); // skapar ny User-instans

                client = new Client("25.82.118.228", 2343, this); // skapar ny Client-instans


                clientMainGUI = new ClientMainGUI(this); // öppna klient fönstret
                clientGUI.closeClientConnectionWindow(); //Stänger ner inloggningsfönster

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
                System.out.println("Något gick fel!");
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

    public void removeContact(User user) //tar bort kontakt från fil
    {
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
