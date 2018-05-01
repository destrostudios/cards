package com.destrostudios.cards.backend.application;

import com.destrostudios.cards.shared.network.TrackedRandom;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.TestGameSetup;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import com.jme3.network.*;

import java.io.IOException;
import java.util.Random;

public class BackendApplication {

    public static void main(String[] args) {
        new BackendApplication(7563);
    }

    public BackendApplication(int port) {
        try {
            System.out.println("Starting server...");
            SimpleGameServer server = new SimpleGameServer(port);
            server.start();
            System.out.println("Server started.");

            new Thread(() -> {
                while (true) {
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
