package model;

import java.io.Serializable;
import java.util.HashMap;

public class Clients implements Serializable {

    private HashMap<User, Server.ClientHandler> clients = new HashMap<User, Server.ClientHandler>();

    public synchronized void put(User user, Server.ClientHandler client) {
        clients.put(user,client);
    }

    public synchronized Server.ClientHandler get(User user) { // TODO : använd denna metod istället för den inbyggda metoden i servern
        return get(user);
    }

    // fler synchronized-metoder som behövs

    public synchronized HashMap<User, Server.ClientHandler> getHashMapList() { // TODO : testa denna metod med flera användare
        return clients;
    }

    public void setClients(HashMap<User, Server.ClientHandler> clients) {
        this.clients = clients;
    }
}




