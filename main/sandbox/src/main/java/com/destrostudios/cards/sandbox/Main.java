package com.destrostudios.cards.sandbox;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.EntityPool;
import com.destrostudios.cards.shared.events.ActionEvent;
import com.destrostudios.cards.shared.events.EventDispatcher;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.events.EventQueueImpl;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.battle.*;
import com.destrostudios.cards.shared.rules.cards.*;
import com.destrostudios.cards.shared.rules.debug.EntityDebugMapper;
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
        //events.action(new StartGameEvent());
        EntityData data = createEntityData();
        MoveGenerator moveGenerator = new MoveGenerator(data);
        Random random = new Random(453);
        EntityPool entities = new EntityPool(random);
        EventDispatcher dispatcher = new EventDispatcher();
        EventQueue events = new EventQueueImpl(dispatcher::fire);
        setListener(dispatcher, data, events, random);

        try {
            for (int i = 0; i < 5; i++) {
                logState(data, entities, moveGenerator);
                List<ActionEvent> actionEvents = moveGenerator.generateAvailableMoves(data.entities(Components.TURN_PHASE).get(0));
                ActionEvent actionEvent = actionEvents.get(random.nextInt(actionEvents.size()));
                events.action(actionEvent);
            }
        } finally {
            logState(data, entities, moveGenerator);
        }
    }

    private static EntityData createEntityData() {
        Random random = new Random(453);
        EntityData data = new EntityData();
        EntityPool entities = new EntityPool(random);

        int player1 = entities.create();
        int player2 = entities.create();
        int hero1 = entities.create();
        int hero2 = entities.create();
        int handCards1 = entities.create();
        int handCards2 = entities.create();

        initPlayerAndHeroEntities(data, player1, player2, hero1, hero2);
        initLibraryAndHandCardsEntities(data, entities, player1, player2, handCards1, handCards2);
        initBoardCardsEntities(data, entities, player1, player2);

        return data;
    }

    private static void initBoardCardsEntities(EntityData data, EntityPool entities, int player1, int player2) {
        int card1 = entities.create();
        int card2 = entities.create();


        data.set(card1, Components.CARD_TEMPLATE, 100);
        data.set(card1, Components.DISPLAY_NAME, "card100");
        data.set(card1, Components.OWNED_BY, player1);
        data.set(card1, Components.BOARD, null);
        data.set(card1, Components.CREATURE_CARD, null);
        data.set(card1, Components.CREATURE_ZONE, null);
        data.set(card1, Components.ATTACK, 2);
        data.set(card1, Components.HEALTH, 2);
        data.set(card2, Components.OWNED_BY, player1);
        data.set(card2, Components.CARD_TEMPLATE, 101);
        data.set(card2, Components.DISPLAY_NAME, "card101");
        data.set(card2, Components.BOARD, null);
        data.set(card2, Components.ENTCHANTMENT_CARD, null);
        data.set(card2, Components.ENCHANTMENT_ZONE, 0);

        int card3 = entities.create();
        int card4 = entities.create();

        data.set(card3, Components.CARD_TEMPLATE, 102);
        data.set(card3, Components.DISPLAY_NAME, "card102");
        data.set(card3, Components.OWNED_BY, player2);
        data.set(card3, Components.BOARD, null);
        data.set(card3, Components.CREATURE_CARD, null);
        data.set(card3, Components.CREATURE_ZONE, 0);
        data.set(card3, Components.ATTACK, 1);
        data.set(card3, Components.HEALTH, 1);

        data.set(card4, Components.OWNED_BY, player2);
        data.set(card4, Components.CARD_TEMPLATE, 103);
        data.set(card4, Components.DISPLAY_NAME, "card103");
        data.set(card4, Components.BOARD, null);
        data.set(card4, Components.CREATURE_CARD, null);
        data.set(card4, Components.CREATURE_ZONE, 1);
        data.set(card4, Components.ATTACK, 1);
        data.set(card4, Components.HEALTH, 1);
    }


    private static void initLibraryAndHandCardsEntities(EntityData data, EntityPool entities, int player1, int player2, int handCards1, int handCards2) {
        int librarySize = 50;
        int handSize = 5;

        for (int i = 0; i < 2 * (librarySize - handSize); i++) {
            int card = entities.create();
            data.set(card, Components.CARD_TEMPLATE, i);
            data.set(card, Components.DISPLAY_NAME, "card" + i);
            data.set(card, Components.OWNED_BY, i < (librarySize - handSize) ? player1 : player2);
            data.set(card, Components.LIBRARY, i % (librarySize - handSize));
            data.set(card, i % 2 == 0 ? Components.CREATURE_CARD : Components.SPELL_CARD, null);
        }

        data.set(handCards1, Components.OWNED_BY, player1);
        data.set(handCards2, Components.OWNED_BY, player2);

        for (int i = librarySize; i < 2 * (librarySize + handSize); i++) {
            int card = entities.create();
            data.set(card, Components.CARD_TEMPLATE, i);
            data.set(card, Components.DISPLAY_NAME, "card" + i);
            data.set(card, Components.OWNED_BY, i < (librarySize + handSize) ? player1 : player2);
            data.set(card, Components.HAND_CARDS, i % (librarySize + handSize));
            data.set(card, i % 2 == 0 ? Components.CREATURE_CARD : Components.SPELL_CARD, null);
        }

    }

    private static void initPlayerAndHeroEntities(EntityData data, int player1, int player2, int hero1, int hero2) {
        data.set(player1, Components.DISPLAY_NAME, "player1");
        data.set(player1, Components.TURN_PHASE, TurnPhase.MAIN);
        data.set(player2, Components.DISPLAY_NAME, "player2");
        data.set(player1, Components.NEXT_PLAYER, player2);
        data.set(player2, Components.NEXT_PLAYER, player1);

        data.set(hero1, Components.HEALTH, 20);
        data.set(hero1, Components.DISPLAY_NAME, "hero1");
        data.set(hero1, Components.OWNED_BY, player1);

        data.set(hero1, Components.HEALTH, 20);
        data.set(hero1, Components.DISPLAY_NAME, "hero2");
        data.set(hero1, Components.OWNED_BY, player2);
    }


    private static void setListener(EventDispatcher dispatcher, EntityData data, EventQueue events, Random random) {
        dispatcher.addListeners(BattleEvent.class, new BattleEventHandler(data, events));
        dispatcher.addListeners(DamageEvent.class,
                new ArmorEventHandler(data, events),
                new DamageEventHandler(data, events));
        dispatcher.addListeners(SetHealthEvent.class, new SetHealthEventHandler(data, events));
        dispatcher.addListeners(DeclareAttackEvent.class, new DeclareAttackEventHandler(data, events));
        dispatcher.addListeners(DeclareBlockEvent.class, new DeclareBlockEventHandler(data, events));
        dispatcher.addListeners(StartRespondPhaseEvent.class, new StartRespondPhaseEventHandler(data, events));
        dispatcher.addListeners(EndRespondPhaseEvent.class, new EndRespondPhaseEventHandler(data, events));
        dispatcher.addListeners(StartBattlePhaseEvent.class,
                new StartBattlePhaseEventHandler(data, events),
                new StartBattleEventHandler(data, events));
        dispatcher.addListeners(EndBattlePhaseEvent.class, new EndBattlePhaseEventHandler(data, events));
        dispatcher.addListeners(StartUpkeepPhaseEvent.class,
                new StartUpkeepPhaseEventHandler(data, events),
                new UpkeepDrawEventHandler(data, events));
        dispatcher.addListeners(EndUpkeepPhaseEvent.class, new EndUpkeepPhaseEventHandler(data, events));
        dispatcher.addListeners(StartMainPhaseEvent.class, new StartMainPhaseEventHandler(data, events));
        dispatcher.addListeners(EndMainPhaseEvent.class, new EndMainPhaseEventHandler(data, events));
        dispatcher.addListeners(StartGameEvent.class, new ShuffleLibraryOnGameStartHandler(data, events));
        dispatcher.addListeners(ShuffleLibraryEvent.class, new ShuffleLibraryEventHandler(data, events, random));
        dispatcher.addListeners(DrawCardEvent.class, new DrawCardEventHandler(data, events));
        dispatcher.addListeners(AddCardToHandEvent.class, new AddCardToHandEventHandler(data, events));
        dispatcher.addListeners(RemoveCardFromHandEvent.class, new RemoveCardFromHandEventHandler(data, events));
        dispatcher.addListeners(AddCardToLibraryEvent.class, new AddCardToLibraryEventHandler(data, events));
        dispatcher.addListeners(RemoveCardFromLibraryEvent.class, new RemoveCardFromLibraryEventHandler(data, events));
    }

    private static void logState(EntityData data, EntityPool entities, MoveGenerator gen) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(GSON.toJson(new EntityDebugMapper().toDebugObjects(data, entities.getEntities())));
            LOG.debug("available moves are {}", gen.generateAvailableMoves(data.entities(Components.TURN_PHASE).get(0)));
        }
    }

}
