package sharedResources;

import javax.swing.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message implements Serializable {

    private User sender;
    private String message;
    private ImageIcon sentImage;
    private User[] arrayOfReceivers = new User[5]; // you can select up to 5 users at a time
    private LocalDateTime timeReceivedByServer;
    private LocalDateTime timeReceivedByClient;

    public Message(User user, String message) {
        this.sender = user;
        this.message = message;
    }

    public Message(User user, String message, ImageIcon image) {
        this.sender = user;
        this.message = message;
        this.sentImage = image;
    }

    public Message(User sender, String message, ImageIcon sentImage, User[] arrayOfReceivers) {
        this.sender = sender;
        this.message = message;
        this.sentImage = sentImage;
        this.arrayOfReceivers = arrayOfReceivers;
    }

    public Message(User sender, ImageIcon sentImage) {
        this.sender = sender;
        this.sentImage = sentImage;
    }

    public String printMessageInfoWithTime(LocalDateTime time) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String receiversStr = "";

        if(arrayOfReceivers.length != 0) { // when the array is not empty
            for ( int i = 0; i < arrayOfReceivers.length; i++ ) {
                User userTemp = arrayOfReceivers[i];
                receiversStr = receiversStr + userTemp.toString() + ", ";
            }
        }

        return toString() + " | Sent to: " + receiversStr + "| Received by server at: " + dtf.format(time);
    }

    //<editor-fold desc="Getters and setters">
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

    public LocalDateTime getTimeReceivedByServer() {
        return timeReceivedByServer;
    }

    public void setReceivedByServerTime(LocalDateTime timeReceivedByServer) {
        this.timeReceivedByServer = timeReceivedByServer;
    }

    public void setTimeReceivedByClient(LocalDateTime timeReceivedByClient) {
        this.timeReceivedByClient = timeReceivedByClient;
    }
    //</editor-fold>

    @Override
    public String toString() {
        if (sentImage != null && message != null) {
            return sender + ": " + message + " (attached image)";
        }
        if (sentImage != null) {
            return sender + ": (attached image)";
        }
        else {
            return sender + ": " + message;
        }
    }
}
