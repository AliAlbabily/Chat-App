package model;

import javax.swing.*;
import java.io.Serializable;

public class User implements Serializable {

    private String username;
    private ImageIcon imageIcon;

    public User(String username, ImageIcon imageIcon) {
        this.username = username;
        this.imageIcon = imageIcon;
    }

    //<editor-fold desc="Getters and setters">
    public String getUsername() {
        return username;
    }

    public int hashCode() {
        return username.hashCode();
    }

    public boolean equals(Object obj) {
        if(obj!=null && obj instanceof User)
            return username.equals(((User)obj).getUsername());
        return false;
    }
    //</editor-fold>

    @Override
    public String toString() {
        return username;
    }
}
