package model;

import java.io.Serializable;
import java.util.HashMap;

public class Clients implements Serializable {

    private HashMap<User, Server.ClientHandler> clients = new HashMap<User, Server.ClientHandler>();

    public synchronized void put(User user, Server.ClientHandler client) {
        clients.put(user,client);
    }

    public synchronized Server.ClientHandler get(User user) {
        return get(user);
    }

    // fler synchronized-metoder som behövs

    public synchronized HashMap<User, Server.ClientHandler> getHashMapList() {
        return clients;
    }

    public void setClients(HashMap<User, Server.ClientHandler> clients) {
        this.clients = clients;
    }
}




