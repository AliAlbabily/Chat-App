package model;

import java.util.ArrayList;
import java.util.HashMap;

public class UnsentMessages {

    private HashMap<User, ArrayList<Message>> unsent = new HashMap<>();

    // Hämta ArrayList - om null skapa en och placera i unsend
    // Lägga till Message i ArrayList
    public synchronized void put(User user, Message message){
        if(getUnsentMessages(user) == null) { // if a user doesn't have an array list (unsent messages)
            ArrayList<Message> userUnsentMessages = new ArrayList<>(); // create a new array list
            userUnsentMessages.add(message); // save message in the new array list
            unsent.put(user, userUnsentMessages); // save in the hashmap
        }
        else { // if the given user already has an array list (unsent messages)
            getUnsentMessages(user).add(message); // add a new message to the array list of the given user
        }

        System.out.println("UnsentMessages: " + unsent.size());
    }

    public synchronized ArrayList<Message> getUnsentMessages(User user) {
        return unsent.get(user);
    }

    public synchronized void removeUnsentMessagesForUser(User user) {
        unsent.replace(user, null);
    }

    public HashMap<User, ArrayList<Message>> getUnsentHashMap() {
        return unsent;
    }
}




