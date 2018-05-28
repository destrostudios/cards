package com.destrostudios.cards.backend.application;

import java.io.IOException;

public class BackendApplication {

    public static void main(String[] args) {
        new BackendApplication(33900);
        while (true) {
            try {
                Thread.sleep(60000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public BackendApplication(int port) {
        try {
            System.out.println("Starting server...");
            SimpleGameServer server = new SimpleGameServer(port);
            server.start();
            System.out.println("Server started.");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
