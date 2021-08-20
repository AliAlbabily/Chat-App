package model;

import java.util.ArrayList;
import java.util.HashMap;

public class UnsentMessages {

    private HashMap<User, ArrayList<Message>> unsent = new HashMap<>();

    public synchronized void put(User user, Message message){
        if(unsent.get(user) == null) { // if a user doesn't have an array list (unsent messages)
            ArrayList<Message> userUnsentMessages = new ArrayList<>(); // create a new array list
            userUnsentMessages.add(message); // save message in the new array list
            unsent.put(user, userUnsentMessages); // save in the hashmap
        }
        else { // if the given user already has an array list (unsent messages)
            unsent.get(user).add(message); // add a new message to the array list of the given user
        }
    }

    public synchronized ArrayList<Message> getUnsentMessages(User user) {
        return unsent.get(user);
    }

    public HashMap<User, ArrayList<Message>> getUnsentHashMap() {
        return unsent;
    }
}




