package server.model;

import sharedResources.User;

import java.io.Serializable;
import java.util.HashMap;

public class Clients implements Serializable {

    private HashMap<User, Server.ClientHandler> clients = new HashMap<User, Server.ClientHandler>();

    public synchronized void put(User user, Server.ClientHandler client) {
        clients.put(user,client);
    }

    public synchronized HashMap<User, Server.ClientHandler> getHashMapList() {
        return clients;
    }
}




