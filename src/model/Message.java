package model;

import javax.swing.*;
import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private User sender;
    private String message;
    private ImageIcon sentImage;
    private User[] arrayOfReceivers = new User[5];
    private Date timeReceivedByServer;
    private Date timeReceivedByClient;

    public Message(User user, String message)
    {
        this.sender = user;
        this.message = message;
    }

    public Message(User sender, String message, ImageIcon sentImage)
    {
        this.sender = sender;
        this.message = message;
        this.sentImage = sentImage;
    }

    public Message(User sender, ImageIcon sentImage)
    {
        this.sender = sender;
        this.sentImage = sentImage;
    }

    //<editor-fold desc="Getters and setters">
    public ImageIcon GetUserPicture()
    {
        return sender.getImageIcon();
    }

    public String GetUsername()
    {
        return sender.getUsername();
    }

    public User GetUser()
    {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public ImageIcon getSentImage() {
        return sentImage;
    }

    @Override
    public String toString() {
        if (sentImage != null) {
            return sender + " | " + message + " + attached image.";
        }
        else {
            return "textMessage: " + message + " & image: " + sentImage;
        }
    }

//</editor-fold>
}
