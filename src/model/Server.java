package model;

import view.ServerLogsGUI;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Server {

    private Clients globalOnlineUsers = new Clients(); // lista av anslutna användare (online users)
    private UnsentMessages unsentMessagesObj = new UnsentMessages();
    private ServerLogsGUI serverLogsGUI;

    private ArrayList<Message> loggedMessages = new ArrayList<>();

    public Server(int port) throws IOException {
        serverLogsGUI = new ServerLogsGUI(); // open Server logs window
        new Connection(port).start();
    }

    private class Connection extends Thread {
        private int port;

        public Connection(int port) {
            this.port = port;
        }

        public void run() {
            Socket socket = null;
            System.out.println("Server startad");
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while(true) {
                    try {
                        socket = serverSocket.accept(); // Lyssna efter anslutande klienter
                        new ClientHandler(socket).start();
                    } catch(IOException e) {
                        System.err.println(e);
                        if(socket != null)
                            socket.close();
                    }
                }
            } catch(IOException e) {
                System.err.println(e);
            }
            System.out.println("Server stoppad");
        }
    }

    public class ClientHandler extends Thread {
        private Socket socket;
        ObjectOutputStream oos;
        ObjectInputStream ois;

        User key = null;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            System.out.println("A new client has connected to the server!");
        }

        public void run() {
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());

                while(true) {
                    Object objReceived = ois.readObject();

                    if(objReceived instanceof User) {
                        key = (User)objReceived;
                        globalOnlineUsers.put((User)objReceived, this); // spara en referens av ClientHandler i hashmapen med sin motsvarande User
                        updateOnlineUsersList();

                        checkIfUserHasUnsentMessages((User)objReceived);
                    }
                    else if(objReceived instanceof Message) {
                        Message messageReceived = (Message)objReceived;

                        Message messageWithTime = getReceivedByServerTime(messageReceived);
                        //
                        sendMessageToReceivers(messageWithTime);
                        logMessage(messageWithTime);
                    }

                }
            }
            catch (IOException | ClassNotFoundException e) {
                try {
                    ois.close();
                    oos.close();
                    socket.close();
                    System.out.println("Client disconnected!");
                    globalOnlineUsers.getHashMapList().remove(key); // tar bort användaren som avslutar sin anslutning från listan
                    updateOnlineUsersList();

                } catch (Exception e2) {}
            }
        }

        public ObjectOutputStream getOos() {
            return oos;
        }

        public Socket getSocket() {
            return socket;
        }
    }

    public static void main(String[] args) {
        try {
            new Server(2343);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // meddela andra klienter om nuvarande anslutna användare
    private void updateOnlineUsersList() throws IOException {
        ArrayList<User> onlineUsers = new ArrayList<>();

        for ( User key : globalOnlineUsers.getHashMapList().keySet() ) {
            onlineUsers.add(key);
        }

        for ( User key : globalOnlineUsers.getHashMapList().keySet() ) {
            // få tag på alla ClientHandlers i hashmappen
            ClientHandler client = globalOnlineUsers.getHashMapList().get(key);
            // skicka den senaste infon om anslutna användare till alla klienter
            client.getOos().writeObject( onlineUsers );
            client.getOos().flush();
        }
    }

    private void checkIfUserHasUnsentMessages(User onlineUser) {
        User[] keysArr = unsentMessagesObj.getUnsentHashMap().keySet().toArray(new User[0]);

        for ( int i = 0; i < keysArr.length; i++ ) {
            User currentUser = keysArr[i];

            if (onlineUser.getUsername().equals( currentUser.getUsername() )) { // check if online user exists in the hashmap (unsent)
                ArrayList<Message> userUnsentMessages = unsentMessagesObj.getUnsentMessages(currentUser);

                Message[] messagesArr = userUnsentMessages.toArray(new Message[0]);

                for ( int j = 0; j < messagesArr.length; j++ ) {
                    try {
                        sendMessageToCertainUser(currentUser, messagesArr[j]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                unsentMessagesObj.getUnsentHashMap().remove(currentUser); // clear the saved messages for the user that was offline
            }
        }
    }

    private void sendMessageToReceivers(Message message) throws IOException {
        User[] receivers = message.getArrayOfReceivers();

        try {
            for ( User user : receivers ) {
                ClientHandler client = globalOnlineUsers.getHashMapList().get(user);

                if(client == null) {
                    unsentMessagesObj.put(user, message);
                }
                else if (client.getSocket().isConnected()) {
                    client.getOos().writeObject(message);
                    client.getOos().flush();
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void sendMessageToCertainUser(User user, Message message) throws IOException {
        User[] filteredReceivers = message.filterReceivers(user, message.getArrayOfReceivers());
        message.setArrayOfReceivers(filteredReceivers);

        ClientHandler client = globalOnlineUsers.getHashMapList().get(user);

        if (client.getSocket().isConnected()) {
            client.getOos().writeObject(message);
            client.getOos().flush();
        } else {
            System.out.println("Error");
        }
    }

    private void logMessage(Message receivedMessage) {
        loggedMessages.add(receivedMessage); // log every received message by the server
        Message[] loggedMessagesArr = loggedMessages.toArray(new Message[0]); // convert the arraylist to array
        serverLogsGUI.updateLogs(loggedMessagesArr); // update the gui component
    }

    private Message getReceivedByServerTime(Message message) {
        LocalDateTime now = LocalDateTime.now(); // time now
        message.setReceivedByServerTime(now); // set a new time to the message
        return message;
    }
}