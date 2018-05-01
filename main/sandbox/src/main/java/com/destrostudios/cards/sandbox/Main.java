package com.destrostudios.cards.sandbox;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.TestGameSetup;
import com.destrostudios.cards.shared.rules.debug.EntityDebugMapper;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @author Philipp
 */
public class Main {
    
    static {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
    }
    
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GameContext context = new GameContext(new Random(453)::nextInt);
        new TestGameSetup().testSetup(context.getData());
        context.getEvents().action(new GameStartEvent());
        logState(context.getData());
    }
    
    private static void logState(EntityData data) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(GSON.toJson(new EntityDebugMapper().toDebugObjects(data)));
        }
    }
    
}
