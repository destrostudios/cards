package com.destrostudios.cards.frontend.application;

import com.destrostudios.cards.shared.application.ApplicationSetup;

import java.io.IOException;

public class FrontendClient {

    public static void main(String[] args) {
        new FrontendClient("localhost", 33900);
    }

    public FrontendClient(String host, int port) {
        ApplicationSetup.setup();

        try {
            System.out.println("Starting client...");
            SimpleGameClient client = new SimpleGameClient(host, port);
            System.out.println("Client started.");

            new FrontendJmeApplication(client).start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}