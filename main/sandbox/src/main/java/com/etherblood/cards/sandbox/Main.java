package com.etherblood.cards.sandbox;

import com.etherblood.cards.rules.battle.DamageEvent;
import com.etherblood.cards.rules.battle.AttackEvent;
import com.etherblood.cards.rules.battle.AttackEventHandler;
import com.etherblood.cards.rules.battle.DamageEventHandler;
import com.etherblood.cards.rules.battle.SetHealthEventHandler;
import com.etherblood.cards.rules.battle.SetHealthEvent;
import com.etherblood.cards.entities.EntityDataBuilder;
import com.etherblood.cards.entities.EntityPool;
import com.etherblood.cards.entities.EntityData;
import com.etherblood.cards.events.EventDispatcher;
import com.etherblood.cards.events.EventQueue;
import com.etherblood.cards.events.EventQueueImpl;
import com.etherblood.cards.rules.battle.ArmorEventHandler;
import com.etherblood.cards.rules.battle.BattleEventHandler;
import com.etherblood.cards.rules.battle.DeclareAttackEvent;
import com.etherblood.cards.rules.battle.DeclareAttackEventHandler;
import com.etherblood.cards.rules.battle.DeclareBlockEvent;
import com.etherblood.cards.rules.battle.DeclareBlockEventHandler;
import com.etherblood.cards.rules.turns.main.EndMainPhaseEvent;
import com.etherblood.cards.rules.turns.main.EndMainPhaseEventHandler;
import com.etherblood.cards.rules.turns.main.StartMainPhaseEvent;
import com.etherblood.cards.rules.turns.main.StartMainPhaseEventHandler;
import com.etherblood.cards.rules.turns.respond.StartRespondPhaseEvent;
import com.etherblood.cards.rules.turns.respond.StartRespondPhaseEventHandler;
import com.etherblood.cards.rules.turns.upkeep.StartUpkeepPhaseEvent;
import com.etherblood.cards.rules.turns.upkeep.StartUpkeepPhaseEventHandler;
import com.etherblood.cards.rules.turns.TurnPhase;
import com.etherblood.cards.rules.turns.battle.EndBattlePhaseEvent;
import com.etherblood.cards.rules.turns.battle.EndBattlePhaseEventHandler;
import com.etherblood.cards.rules.turns.battle.StartBattlePhaseEvent;
import com.etherblood.cards.rules.turns.battle.StartBattlePhaseEventHandler;
import com.etherblood.cards.rules.turns.respond.EndRespondPhaseEvent;
import com.etherblood.cards.rules.turns.respond.EndRespondPhaseEventHandler;
import com.etherblood.cards.rules.turns.upkeep.EndUpkeepPhaseEvent;
import com.etherblood.cards.rules.turns.upkeep.EndUpkeepPhaseEventHandler;
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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        String matchName = "matchName";
        Function<Class<?>, Logger> loggerFactory = clazz -> LoggerFactory.getLogger(matchName + " " + clazz.getSimpleName());

        List<String> names = new ArrayList<>();
        names.add("hero");
        names.add("minion1");
        names.add("minion2");
        names.add("noise");
        names.add("player1");
        names.add("player2");

        EntityDataBuilder builder = new EntityDataBuilder();
        EntityPool entities = new EntityPool(new Random(453));

        int health = builder.withComponent("health").getKey();
        int ownedBy = builder.withComponent("ownedBy").getKey();
        int nextPlayer = builder.withComponent("nextPlayer").getKey();
        int attack = builder.withComponent("attack").getKey();
        int armor = builder.withComponent("armor").getKey();
        int declaredAttack = builder.withComponent("declaredAttack").getKey();
        int declaredBlock = builder.withComponent("declaredBlock").getKey();
        int displayName = builder.withComponent("displayName", names::get).getKey();
        int turnPhase = builder.withComponent("phase", x -> TurnPhase.values()[x]).getKey();

        EntityData data = builder.build();
        EventDispatcher dispatcher = new EventDispatcher();
        EventQueue events = new EventQueueImpl(dispatcher::fire);
        dispatcher.addListener(AttackEvent.class, new AttackEventHandler(data, events, loggerFactory.apply(AttackEventHandler.class), attack));

        dispatcher.addListener(DamageEvent.class, new ArmorEventHandler(data, events, loggerFactory.apply(ArmorEventHandler.class), armor));
        dispatcher.addListener(DamageEvent.class, new DamageEventHandler(data, events, loggerFactory.apply(DamageEventHandler.class), health));

        dispatcher.addListener(SetHealthEvent.class, new SetHealthEventHandler(data, events, loggerFactory.apply(SetHealthEventHandler.class), health));
        dispatcher.addListener(DeclareAttackEvent.class, new DeclareAttackEventHandler(data, events, loggerFactory.apply(DeclareAttackEventHandler.class), declaredAttack));
        dispatcher.addListener(DeclareBlockEvent.class, new DeclareBlockEventHandler(data, events, loggerFactory.apply(DeclareBlockEventHandler.class), declaredBlock));

        dispatcher.addListener(StartRespondPhaseEvent.class, new StartRespondPhaseEventHandler(data, events, loggerFactory.apply(StartRespondPhaseEventHandler.class), turnPhase));
        dispatcher.addListener(EndRespondPhaseEvent.class, new EndRespondPhaseEventHandler(data, events, loggerFactory.apply(EndRespondPhaseEventHandler.class), turnPhase));
        dispatcher.addListener(StartBattlePhaseEvent.class, new StartBattlePhaseEventHandler(data, events, loggerFactory.apply(StartBattlePhaseEventHandler.class), turnPhase));
        dispatcher.addListener(StartBattlePhaseEvent.class, new BattleEventHandler(data, events, loggerFactory.apply(BattleEventHandler.class), declaredBlock, declaredAttack));
        dispatcher.addListener(EndBattlePhaseEvent.class, new EndBattlePhaseEventHandler(data, events, loggerFactory.apply(EndBattlePhaseEventHandler.class), turnPhase));
        dispatcher.addListener(StartUpkeepPhaseEvent.class, new StartUpkeepPhaseEventHandler(data, events, loggerFactory.apply(StartUpkeepPhaseEventHandler.class), turnPhase));
        dispatcher.addListener(EndUpkeepPhaseEvent.class, new EndUpkeepPhaseEventHandler(data, events, loggerFactory.apply(EndUpkeepPhaseEventHandler.class), turnPhase));
        dispatcher.addListener(StartMainPhaseEvent.class, new StartMainPhaseEventHandler(data, events, loggerFactory.apply(StartMainPhaseEventHandler.class), turnPhase));
        dispatcher.addListener(EndMainPhaseEvent.class, new EndMainPhaseEventHandler(data, events, loggerFactory.apply(EndMainPhaseEventHandler.class), turnPhase, nextPlayer));

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

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Logger log = LoggerFactory.getLogger(Main.class);

        log.info(gson.toJson(builder.toDebugMap(data, entities)));
        events.action(new DeclareAttackEvent(hero, minion2));
        log.info(gson.toJson(builder.toDebugMap(data, entities)));
        events.action(new EndMainPhaseEvent(player1));
        log.info(gson.toJson(builder.toDebugMap(data, entities)));
        events.action(new DeclareBlockEvent(minion1, hero));
        log.info(gson.toJson(builder.toDebugMap(data, entities)));
        events.action(new EndRespondPhaseEvent(player2));
        log.info(gson.toJson(builder.toDebugMap(data, entities)));
    }

}
