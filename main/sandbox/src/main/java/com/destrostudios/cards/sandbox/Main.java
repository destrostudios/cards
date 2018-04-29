package com.destrostudios.cards.sandbox;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.EntityPool;
import com.destrostudios.cards.shared.events.EventDispatcher;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.events.EventQueueImpl;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.battle.ArmorEventHandler;
import com.destrostudios.cards.shared.rules.battle.AttackEvent;
import com.destrostudios.cards.shared.rules.battle.AttackEventHandler;
import com.destrostudios.cards.shared.rules.battle.BattleEventHandler;
import com.destrostudios.cards.shared.rules.battle.DamageEvent;
import com.destrostudios.cards.shared.rules.battle.DamageEventHandler;
import com.destrostudios.cards.shared.rules.battle.DeclareAttackEvent;
import com.destrostudios.cards.shared.rules.battle.DeclareAttackEventHandler;
import com.destrostudios.cards.shared.rules.battle.DeclareBlockEvent;
import com.destrostudios.cards.shared.rules.battle.DeclareBlockEventHandler;
import com.destrostudios.cards.shared.rules.battle.SetHealthEvent;
import com.destrostudios.cards.shared.rules.battle.SetHealthEventHandler;
import com.destrostudios.cards.shared.rules.cards.AddCardToHandEvent;
import com.destrostudios.cards.shared.rules.cards.AddCardToHandEventHandler;
import com.destrostudios.cards.shared.rules.cards.AddCardToLibraryEvent;
import com.destrostudios.cards.shared.rules.cards.AddCardToLibraryEventHandler;
import com.destrostudios.cards.shared.rules.cards.DrawCardEvent;
import com.destrostudios.cards.shared.rules.cards.DrawCardEventHandler;
import com.destrostudios.cards.shared.rules.cards.RemoveCardFromHandEvent;
import com.destrostudios.cards.shared.rules.cards.RemoveCardFromHandEventHandler;
import com.destrostudios.cards.shared.rules.cards.RemoveCardFromLibraryEvent;
import com.destrostudios.cards.shared.rules.cards.RemoveCardFromLibraryEventHandler;
import com.destrostudios.cards.shared.rules.cards.ShuffleLibraryEvent;
import com.destrostudios.cards.shared.rules.cards.ShuffleLibraryEventHandler;
import com.destrostudios.cards.shared.rules.cards.ShuffleLibraryOnGameStartHandler;
import com.destrostudios.cards.shared.rules.cards.UpkeepDrawEventHandler;
import com.destrostudios.cards.shared.rules.game.StartGameEvent;
import com.destrostudios.cards.shared.rules.moves.MoveGenerator;
import com.destrostudios.cards.shared.rules.turns.TurnPhase;
import com.destrostudios.cards.shared.rules.turns.battle.EndBattlePhaseEvent;
import com.destrostudios.cards.shared.rules.turns.battle.EndBattlePhaseEventHandler;
import com.destrostudios.cards.shared.rules.turns.battle.StartBattlePhaseEvent;
import com.destrostudios.cards.shared.rules.turns.battle.StartBattlePhaseEventHandler;
import com.destrostudios.cards.shared.rules.turns.main.EndMainPhaseEvent;
import com.destrostudios.cards.shared.rules.turns.main.EndMainPhaseEventHandler;
import com.destrostudios.cards.shared.rules.turns.main.StartMainPhaseEvent;
import com.destrostudios.cards.shared.rules.turns.main.StartMainPhaseEventHandler;
import com.destrostudios.cards.shared.rules.turns.respond.EndRespondPhaseEvent;
import com.destrostudios.cards.shared.rules.turns.respond.EndRespondPhaseEventHandler;
import com.destrostudios.cards.shared.rules.turns.respond.StartRespondPhaseEvent;
import com.destrostudios.cards.shared.rules.turns.respond.StartRespondPhaseEventHandler;
import com.destrostudios.cards.shared.rules.turns.upkeep.EndUpkeepPhaseEvent;
import com.destrostudios.cards.shared.rules.turns.upkeep.EndUpkeepPhaseEventHandler;
import com.destrostudios.cards.shared.rules.turns.upkeep.StartUpkeepPhaseEvent;
import com.destrostudios.cards.shared.rules.turns.upkeep.StartUpkeepPhaseEventHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
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
        List<String> names = new ArrayList<>();
        names.add("hero");
        names.add("minion1");
        names.add("minion2");
        names.add("noise");
        names.add("player1");
        names.add("player2");
        names.add("card");

        Random random = new Random(453);
        EntityPool entities = new EntityPool(random);

        EntityDebugMapper debug = new EntityDebugMapper();
        debug.register(Components.HEALTH, "health");
        debug.register(Components.OWNED_BY, "ownedBy");
        debug.register(Components.NEXT_PLAYER, "nextPlayer");
        debug.register(Components.ATTACK, "attack");
        debug.register(Components.ARMOR, "armor");
        debug.register(Components.DECLARED_ATTACK, "declaredAttack");
        debug.register(Components.DECLARED_BLOCK, "declaredBlock");
        debug.register(Components.DISPLAY_NAME, "displayName", names::get);
        debug.register(Components.TURN_PHASE, "phase", x -> TurnPhase.values()[x]);
        debug.register(Components.LIBRARY, "library");
        debug.register(Components.HAND, "hand");
        debug.register(Components.CARD_TEMPLATE, "cardTemplate");

        EntityData data = new EntityData(Components.COMPONENTS_COUNT);
        EventDispatcher dispatcher = new EventDispatcher();
        EventQueue events = new EventQueueImpl(dispatcher::fire);
        dispatcher.addListener(AttackEvent.class, new AttackEventHandler(data, events));

        dispatcher.addListener(DamageEvent.class, new ArmorEventHandler(data, events));
        dispatcher.addListener(DamageEvent.class, new DamageEventHandler(data, events));

        dispatcher.addListener(SetHealthEvent.class, new SetHealthEventHandler(data, events));
        dispatcher.addListener(DeclareAttackEvent.class, new DeclareAttackEventHandler(data, events));
        dispatcher.addListener(DeclareBlockEvent.class, new DeclareBlockEventHandler(data, events));

        dispatcher.addListener(StartRespondPhaseEvent.class, new StartRespondPhaseEventHandler(data, events));
        dispatcher.addListener(EndRespondPhaseEvent.class, new EndRespondPhaseEventHandler(data, events));

        dispatcher.addListener(StartBattlePhaseEvent.class, new StartBattlePhaseEventHandler(data, events));
        dispatcher.addListener(StartBattlePhaseEvent.class, new BattleEventHandler(data, events));

        dispatcher.addListener(EndBattlePhaseEvent.class, new EndBattlePhaseEventHandler(data, events));

        dispatcher.addListener(StartUpkeepPhaseEvent.class, new StartUpkeepPhaseEventHandler(data, events));
        dispatcher.addListener(StartUpkeepPhaseEvent.class, new UpkeepDrawEventHandler(data, events));

        dispatcher.addListener(EndUpkeepPhaseEvent.class, new EndUpkeepPhaseEventHandler(data, events));
        dispatcher.addListener(StartMainPhaseEvent.class, new StartMainPhaseEventHandler(data, events));
        dispatcher.addListener(EndMainPhaseEvent.class, new EndMainPhaseEventHandler(data, events));

        dispatcher.addListener(StartGameEvent.class, new ShuffleLibraryOnGameStartHandler(data, events));
        dispatcher.addListener(ShuffleLibraryEvent.class, new ShuffleLibraryEventHandler(data, events, random));

        dispatcher.addListener(DrawCardEvent.class, new DrawCardEventHandler(data, events));
        dispatcher.addListener(AddCardToHandEvent.class, new AddCardToHandEventHandler(data, events));
        dispatcher.addListener(RemoveCardFromHandEvent.class, new RemoveCardFromHandEventHandler(data, events));
        dispatcher.addListener(AddCardToLibraryEvent.class, new AddCardToLibraryEventHandler(data, events));
        dispatcher.addListener(RemoveCardFromLibraryEvent.class, new RemoveCardFromLibraryEventHandler(data, events));

        MoveGenerator moveGenerator = new MoveGenerator(data);

        int player1 = entities.create();
        data.set(player1, Components.DISPLAY_NAME, names.indexOf("player1"));
        data.set(player1, Components.TURN_PHASE, TurnPhase.MAIN.ordinal());

        int player2 = entities.create();
        data.set(player2, Components.DISPLAY_NAME, names.indexOf("player2"));

        data.set(player1, Components.NEXT_PLAYER, player2);
        data.set(player2, Components.NEXT_PLAYER, player1);

        int minion1 = entities.create();
        data.set(minion1, Components.HEALTH, 15);
        data.set(minion1, Components.DISPLAY_NAME, names.indexOf("minion1"));
        data.set(minion1, Components.ATTACK, 2);
        data.set(minion1, Components.OWNED_BY, player2);

        int minion2 = entities.create();
        data.set(minion2, Components.HEALTH, 5);
        data.set(minion2, Components.DISPLAY_NAME, names.indexOf("minion2"));
        data.set(minion2, Components.ATTACK, 5);
        data.set(minion2, Components.OWNED_BY, player2);

        int hero = entities.create();
        data.set(hero, Components.HEALTH, 215);
        data.set(hero, Components.ATTACK, 3);
        data.set(hero, Components.DISPLAY_NAME, names.indexOf("hero"));
        data.set(hero, Components.ARMOR, 1);
        data.set(hero, Components.OWNED_BY, player1);

        int librarySize = 2;
        for (int i = 0; i < 2 * librarySize; i++) {
            int card = entities.create();
            data.set(card, Components.CARD_TEMPLATE, i);
            data.set(card, Components.DISPLAY_NAME, names.indexOf("card"));
            data.set(card, Components.OWNED_BY, i < librarySize ? player1 : player2);
            data.set(card, Components.LIBRARY, i % librarySize);
        }
        events.action(new StartGameEvent());

        try {
            logState(debug, data, entities, moveGenerator);
            events.action(new DeclareAttackEvent(hero, minion2));
            logState(debug, data, entities, moveGenerator);
            events.action(new EndMainPhaseEvent(player1));
            logState(debug, data, entities, moveGenerator);
            events.action(new DeclareBlockEvent(minion1, hero));
            logState(debug, data, entities, moveGenerator);
            events.action(new EndRespondPhaseEvent(player2));
        } finally {
            logState(debug, data, entities, moveGenerator);
        }
    }

    private static void logState(EntityDebugMapper debug, EntityData data, EntityPool entities, MoveGenerator gen) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(GSON.toJson(debug.toDebugObjects(data, entities.getEntities())));
            LOG.debug("available moves are {}", gen.generateAvailableMoves(data.entities(Components.TURN_PHASE).get(0)));
        }
    }

}
