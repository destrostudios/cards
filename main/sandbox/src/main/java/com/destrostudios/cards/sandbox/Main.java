package com.destrostudios.cards.sandbox;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.EntityDataBuilder;
import com.destrostudios.cards.shared.entities.EntityPool;
import com.destrostudios.cards.shared.events.EventDispatcher;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.events.EventQueueImpl;
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
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class Main {

    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
        String matchName = "matchName";
        Function<Class<?>, Logger> loggerFactory = clazz -> LoggerFactory.getLogger(matchName + " " + clazz.getSimpleName());

        List<String> names = new ArrayList<>();
        names.add("hero");
        names.add("minion1");
        names.add("minion2");
        names.add("noise");
        names.add("player1");
        names.add("player2");
        names.add("card");

        EntityDataBuilder builder = new EntityDataBuilder();
        Random random = new Random(453);
        EntityPool entities = new EntityPool(random);

        int health = builder.withComponent("health").getKey();
        int ownedBy = builder.withComponent("ownedBy").getKey();
        int nextPlayer = builder.withComponent("nextPlayer").getKey();
        int attack = builder.withComponent("attack").getKey();
        int armor = builder.withComponent("armor").getKey();
        int declaredAttack = builder.withComponent("declaredAttack").getKey();
        int declaredBlock = builder.withComponent("declaredBlock").getKey();
        int displayName = builder.withComponent("displayName", names::get).getKey();
        int turnPhase = builder.withComponent("phase", x -> TurnPhase.values()[x]).getKey();
        int library = builder.withComponent("library").getKey();
        int hand = builder.withComponent("hand").getKey();
        int cardTemplate = builder.withComponent("cardTemplate").getKey();

        EntityData data = builder.build();
        EventDispatcher dispatcher = new EventDispatcher();
        EventQueue events = new EventQueueImpl(dispatcher::fire, loggerFactory.apply(EventQueueImpl.class));
        dispatcher.addListener(AttackEvent.class, new AttackEventHandler(data, events, loggerFactory.apply(AttackEventHandler.class), attack));

        dispatcher.addListener(DamageEvent.class, new ArmorEventHandler(data, events, loggerFactory.apply(ArmorEventHandler.class), armor));
        dispatcher.addListener(DamageEvent.class, new DamageEventHandler(data, events, loggerFactory.apply(DamageEventHandler.class), health));

        dispatcher.addListener(SetHealthEvent.class, new SetHealthEventHandler(data, events, loggerFactory.apply(SetHealthEventHandler.class), health));
        dispatcher.addListener(DeclareAttackEvent.class, new DeclareAttackEventHandler(data, events, loggerFactory.apply(DeclareAttackEventHandler.class), declaredAttack));
        dispatcher.addListener(DeclareBlockEvent.class, new DeclareBlockEventHandler(data, events, loggerFactory.apply(DeclareBlockEventHandler.class), declaredBlock));

        dispatcher.addListener(StartRespondPhaseEvent.class, new StartRespondPhaseEventHandler(data, events, loggerFactory.apply(StartRespondPhaseEventHandler.class), turnPhase));
        dispatcher.addListener(EndRespondPhaseEvent.class, new EndRespondPhaseEventHandler(data, events, loggerFactory.apply(EndRespondPhaseEventHandler.class), turnPhase));

        dispatcher.addListener(StartBattlePhaseEvent.class, new StartBattlePhaseEventHandler(data, events, loggerFactory.apply(StartBattlePhaseEventHandler.class), turnPhase));
        dispatcher.addListener(StartBattlePhaseEvent.class, new BattleEventHandler(data, events, loggerFactory.apply(BattleEventHandler.class), declaredBlock, declaredAttack, ownedBy));

        dispatcher.addListener(EndBattlePhaseEvent.class, new EndBattlePhaseEventHandler(data, events, loggerFactory.apply(EndBattlePhaseEventHandler.class), turnPhase));

        dispatcher.addListener(StartUpkeepPhaseEvent.class, new StartUpkeepPhaseEventHandler(data, events, loggerFactory.apply(StartUpkeepPhaseEventHandler.class), turnPhase));
        dispatcher.addListener(StartUpkeepPhaseEvent.class, new UpkeepDrawEventHandler(data, events, loggerFactory.apply(UpkeepDrawEventHandler.class)));

        dispatcher.addListener(EndUpkeepPhaseEvent.class, new EndUpkeepPhaseEventHandler(data, events, loggerFactory.apply(EndUpkeepPhaseEventHandler.class), turnPhase));
        dispatcher.addListener(StartMainPhaseEvent.class, new StartMainPhaseEventHandler(data, events, loggerFactory.apply(StartMainPhaseEventHandler.class), turnPhase));
        dispatcher.addListener(EndMainPhaseEvent.class, new EndMainPhaseEventHandler(data, events, loggerFactory.apply(EndMainPhaseEventHandler.class), turnPhase, nextPlayer));

        dispatcher.addListener(StartGameEvent.class, new ShuffleLibraryOnGameStartHandler(data, events, loggerFactory.apply(ShuffleLibraryOnGameStartHandler.class), nextPlayer));
        dispatcher.addListener(ShuffleLibraryEvent.class, new ShuffleLibraryEventHandler(data, events, loggerFactory.apply(ShuffleLibraryEventHandler.class), random, library, ownedBy));

        dispatcher.addListener(DrawCardEvent.class, new DrawCardEventHandler(data, events, loggerFactory.apply(DrawCardEventHandler.class), library, ownedBy));
        dispatcher.addListener(AddCardToHandEvent.class, new AddCardToHandEventHandler(data, events, loggerFactory.apply(AddCardToHandEventHandler.class), hand, ownedBy));
        dispatcher.addListener(RemoveCardFromHandEvent.class, new RemoveCardFromHandEventHandler(data, events, loggerFactory.apply(RemoveCardFromHandEventHandler.class), hand, ownedBy));
        dispatcher.addListener(AddCardToLibraryEvent.class, new AddCardToLibraryEventHandler(data, events, loggerFactory.apply(AddCardToLibraryEventHandler.class), library, ownedBy));
        dispatcher.addListener(RemoveCardFromLibraryEvent.class, new RemoveCardFromLibraryEventHandler(data, events, loggerFactory.apply(RemoveCardFromLibraryEventHandler.class), library, ownedBy));

        MoveGenerator moveGenerator = new MoveGenerator(data, turnPhase, attack, health, ownedBy, declaredAttack);

        int player1 = entities.create();
        data.set(player1, displayName, names.indexOf("player1"));
        data.set(player1, turnPhase, TurnPhase.MAIN.ordinal());

        int player2 = entities.create();
        data.set(player2, displayName, names.indexOf("player2"));

        data.set(player1, nextPlayer, player2);
        data.set(player2, nextPlayer, player1);

        int minion1 = entities.create();
        data.set(minion1, health, 15);
        data.set(minion1, displayName, names.indexOf("minion1"));
        data.set(minion1, attack, 2);
        data.set(minion1, ownedBy, player2);

        int minion2 = entities.create();
        data.set(minion2, health, 5);
        data.set(minion2, displayName, names.indexOf("minion2"));
        data.set(minion2, attack, 5);
        data.set(minion2, ownedBy, player2);

        int hero = entities.create();
        data.set(hero, health, 215);
        data.set(hero, attack, 3);
        data.set(hero, displayName, names.indexOf("hero"));
        data.set(hero, armor, 1);
        data.set(hero, ownedBy, player1);

        int librarySize = 3;
        for (int i = 0; i < 2 * librarySize; i++) {
            int card = entities.create();
            data.set(card, cardTemplate, i);
            data.set(card, displayName, names.indexOf("card"));
            data.set(card, ownedBy, i < librarySize ? player1 : player2);
            data.set(card, library, i % librarySize);
        }
        events.action(new StartGameEvent());

        Logger log = LoggerFactory.getLogger(Main.class);

        try {
            logState(log, builder, data, entities, moveGenerator, turnPhase);
            events.action(new DeclareAttackEvent(hero, minion2));
            logState(log, builder, data, entities, moveGenerator, turnPhase);
            events.action(new EndMainPhaseEvent(player1));
            logState(log, builder, data, entities, moveGenerator, turnPhase);
            events.action(new DeclareBlockEvent(minion1, hero));
            logState(log, builder, data, entities, moveGenerator, turnPhase);
            events.action(new EndRespondPhaseEvent(player2));
        } finally {
            logState(log, builder, data, entities, moveGenerator, turnPhase);
        }
    }

    private static void logState(Logger log, EntityDataBuilder builder, EntityData data, EntityPool entities, MoveGenerator gen, int activePlayerKey) {
        if (log.isDebugEnabled()) {
            log.debug(GSON.toJson(builder.toDebugMap(data, entities)));
            log.debug("available moves are {}", gen.generateAvailableMoves(data.entitiesWithComponent(activePlayerKey).get(0)));
        }
    }

}
