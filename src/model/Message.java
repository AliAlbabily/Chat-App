package model;

import javax.swing.*;
import java.io.Serializable;
import java.util.Arrays;
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

    public Message(User user, String message, ImageIcon image)
    {
        this.sender = user;
        this.message = message;
        this.sentImage = image;
    }

    public Message(User sender, String message, ImageIcon sentImage, User[] arrayOfReceivers)
    {
        this.sender = sender;
        this.message = message;
        this.sentImage = sentImage;
        this.arrayOfReceivers = arrayOfReceivers;
    }

    public Message(User sender, ImageIcon sentImage)
    {
        this.sender = sender;
        this.sentImage = sentImage;
    }

    // filter the current array of receivers and keep only the given user
//    public User[] filterReceivers(User offlineUserToKeep) {
//        User[] tempArray = new User[1]; // new array of receivers
//
//        for( User userReceiver :  arrayOfReceivers ) {
//            if( userReceiver.getUsername().equals(offlineUserToKeep.getUsername()) ) {
//                tempArray[0] = userReceiver;
//            }
//        }
//
//        return tempArray;
//    }

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

    public User[] getArrayOfReceivers() {
        return arrayOfReceivers;
    }

    public void setArrayOfReceivers(User[] arrayOfReceivers) {
        this.arrayOfReceivers = arrayOfReceivers;
    }

    @Override
    public String toString() {
        if (sentImage != null && message != null) {
            return sender + ": " + message + " (attached image)";
        }
        if (sentImage != null) {
            return sender + ": (attached image)";
        }
        else {
            return sender+": " + message;
        }
    }

//</editor-fold>
}
