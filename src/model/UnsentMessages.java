package model;

import java.util.ArrayList;
import java.util.HashMap;

public class UnsentMessages {

    private HashMap<User, ArrayList<Message>> unsent = new HashMap<>();

    // Hämta ArrayList - om null skapa en och placera i unsend
    // Lägga till Message i ArrayList
    public synchronized void put(User user, ArrayList<Message> messages){
        unsent.put(user, messages);
    }

    public synchronized ArrayList<Message> getUnsentMessages(User user) {
        return unsent.get(user);
    }
}




