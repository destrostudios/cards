package com.etherblood.cards.sandbox;

import com.etherblood.cards.sandbox.rules.DamageEvent;
import com.etherblood.cards.sandbox.rules.AttackEvent;
import com.etherblood.cards.sandbox.rules.AttackEventHandler;
import com.etherblood.cards.sandbox.rules.DamageEventHandler;
import com.etherblood.cards.sandbox.rules.SetHealthEventHandler;
import com.etherblood.cards.sandbox.rules.SetHealthEvent;
import com.etherblood.cards.entities.EntityDataBuilder;
import com.etherblood.cards.entities.EntityPool;
import com.etherblood.cards.entities.EntityData;
import com.etherblood.cards.events.EventDispatcherImpl;
import com.etherblood.cards.events.EventQueue;
import com.etherblood.cards.events.EventQueueImpl;
import com.etherblood.cards.sandbox.rules.ArmorEventHandler;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        names.add("minion");
        names.add("weapon");
        names.add("stuff");
        names.add("gamestate");

        EntityDataBuilder builder = new EntityDataBuilder();
        EntityPool entities = new EntityPool(new Random(453));

        int health = builder.withComponent("health").getKey();
        int attack = builder.withComponent("attack").getKey();
        int armor = builder.withComponent("armor").getKey();
        int summonSickness = builder.withComponent("summonSickness").getKey();
        int boardIndex = builder.withComponent("boardIndex").getKey();
        int displayName = builder.withComponent("displayName", names::get).getKey();

        EntityData data = builder.build();
        EventDispatcherImpl dispatcher = new EventDispatcherImpl();
        EventQueue events = new EventQueueImpl(dispatcher::fire);
        dispatcher.addListener(AttackEvent.class, new AttackEventHandler(data, events, loggerFactory.apply(AttackEventHandler.class), attack));
        dispatcher.addListener(DamageEvent.class, new ArmorEventHandler(data, events, loggerFactory.apply(ArmorEventHandler.class), armor));
        dispatcher.addListener(DamageEvent.class, new DamageEventHandler(data, events, loggerFactory.apply(DamageEventHandler.class), health));
        dispatcher.addListener(SetHealthEvent.class, new SetHealthEventHandler(data, events, loggerFactory.apply(SetHealthEventHandler.class), health));

        int minion = entities.create();
        data.set(minion, health, 15);
        data.set(minion, displayName, names.indexOf("minion"));
        data.set(minion, boardIndex, 1);
        data.set(minion, summonSickness, 1);
        data.set(minion, armor, 1);

        int hero = entities.create();
        data.set(hero, health, 215);
        data.set(hero, attack, 3);
        data.set(hero, displayName, names.indexOf("hero"));
        data.set(hero, boardIndex, 2);

        int gamestate = entities.create();
        data.set(gamestate, displayName, names.indexOf("gamestate"));

        Map<Integer, Map<String, Object>> map = builder.toDebugMap(data, entities);
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(map));
        
        events.action(new AttackEvent(hero, minion));

        map = builder.toDebugMap(data, entities);
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(map));
    }

}
