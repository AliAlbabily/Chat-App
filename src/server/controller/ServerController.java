package server.controller;

import sharedResources.Message;
import server.model.Server;
import server.view.ServerLogsGUI;

import java.io.IOException;
import java.util.ArrayList;

public class ServerController {

    private Server server; // model
    private ServerLogsGUI serverLogsGUI; // view

    public ServerController() throws IOException {
        server = new Server(2343, this);
        serverLogsGUI = new ServerLogsGUI(this); // opens the GUI window for the Server
    }

    public void updateLogsGUI(Message[] loggedMessagesArr) {
        serverLogsGUI.updateLogs(loggedMessagesArr); // update the gui component
    }

    public ArrayList<Message> getLoggedMessages() {
        return server.getLoggedMessages();
    }
}
