package com.destrostudios.cards.backend.application;

import com.destrostudios.cards.shared.application.ApplicationSetup;

import java.io.IOException;

public class BackendApplication {

    public static void main(String[] args) {
        ApplicationSetup.setup();

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
