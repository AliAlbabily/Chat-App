package server.controller;

import java.io.IOException;

public class ServerMain {

    public static void main(String[] args) {

        try {
            new ServerController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
