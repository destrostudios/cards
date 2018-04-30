package com.destrostudios.cards.sandbox;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.TestGameSetup;
import com.destrostudios.cards.shared.rules.debug.EntityDebugMapper;
import com.destrostudios.cards.shared.rules.moves.MoveGenerator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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

        try {
            Random rng = new Random(3);
            for (int i = 0; i < 5; i++) {
                logState(context.getData(), context.getMoveGenerator());
                List<Event> events = context.getMoveGenerator().generateAvailableMoves(context.getData().entities(Components.TURN_PHASE).get(0));
                Event event = events.get(rng.nextInt(events.size()));
                context.getEvents().action(event);
            }
        } finally {
            logState(context.getData(), context.getMoveGenerator());
        }
    }

    private static void logState(EntityData data, MoveGenerator gen) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(GSON.toJson(new EntityDebugMapper().toDebugObjects(data)));
            LOG.debug("available moves are {}", gen.generateAvailableMoves(data.entities(Components.TURN_PHASE).get(0)));
        }
    }

}
