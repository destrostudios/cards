package com.destrostudios.cards.frontend.application;

import com.destrostudios.cards.frontend.cardgui.files.FileAssets;
import com.destrostudios.cards.shared.rules.GameContext;
import com.jme3.network.Client;
import com.jme3.network.Network;

import java.io.IOException;
import java.util.Random;

public class FrontendClient {

    public static void main(String[] args) {
        new FrontendClient("localhost", 7563);
    }

    public FrontendClient(String host, int port) {
        FileAssets.readRootFile();

        try {
            System.out.println("Starting client...");
            Client client = Network.connectToServer(host, port);
            client.start();
            System.out.println("Client started.");

            GameContext gameContext = new GameContext(new Random(453)::nextInt);

            new FrontendJmeApplication(gameContext).start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
