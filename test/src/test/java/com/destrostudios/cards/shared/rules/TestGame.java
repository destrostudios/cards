package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.application.ApplicationSetup;
import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.entities.templates.EntityTemplate;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.Queue;
import com.destrostudios.cards.shared.rules.battle.DamageEvent;
import com.destrostudios.cards.shared.rules.battle.DestructionEvent;
import com.destrostudios.cards.shared.rules.cards.PlaySpellEvent;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import com.destrostudios.cards.shared.rules.util.CostUtil;
import com.destrostudios.cards.shared.rules.util.SpellUtil;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import com.destrostudios.cards.shared.rules.util.ZoneUtil;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.junit.jupiter.api.BeforeEach;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class TestGame {

    static {
        ApplicationSetup.setup();
    }
    private StartGameInfo startGameInfo;
    protected SimpleEntityData data;
    private GameContext gameContext;
    private NetworkRandom random;
    protected int player;
    protected int opponent;

    @BeforeEach
    public void before() {
        startGameInfo = new StartGameInfo(
            mock(Mode.class),
            mock(Queue.class),
            "myBoardName",
            new PlayerInfo[] {
                new PlayerInfo(1, "Player", null),
                new PlayerInfo(2, "Opponent", null),
            }
        );
        data = new SimpleEntityData(Components.ALL);
        gameContext = new GameContext(startGameInfo, data);
        random = mock(NetworkRandom.class);
        setupGame();
        fire(new GameStartEvent());
    }

    protected void setupGame() {
        player = data.createEntity();
        opponent = data.createEntity();
        GameSetup.initPlayer(data, player, opponent, startGameInfo.getPlayers()[0].getLogin(), createPlayerLibrary());
        GameSetup.initPlayer(data, opponent, player, startGameInfo.getPlayers()[1].getLogin(), createOpponentLibrary());
    }

    protected List<Integer> createPlayerLibrary() {
        return new LinkedList<>();
    }

    protected List<Integer> createOpponentLibrary() {
        return new LinkedList<>();
    }

    protected int[] createCards(int count, int owner, ComponentDefinition<Integer> zone) {
        return createCreatures(count, owner, zone);
    }

    protected int[] createCreatures(int count, int owner, ComponentDefinition<Integer> zone) {
        return create(count, () -> createCreature(owner, zone));
    }

    protected int createCreature(int owner, ComponentDefinition<Integer> zone) {
        return createVanilla(0, 1, 1, owner, zone);
    }

    protected int[] createVanillasForBothPlayers(int countPerPlayer, int manaCost, int attack, int health, ComponentDefinition<Integer> zone) {
        return createForBothPlayers(countPerPlayer, owner -> createVanilla(manaCost, attack, health, owner, zone));
    }

    protected int[] createVanillas(int count, int manaCost, int attack, int health, int owner, ComponentDefinition<Integer> zone) {
        return create(count, () -> createVanilla(manaCost, attack, health, owner, zone));
    }

    protected int createVanilla(int manaCost, int attack, int health, int owner, ComponentDefinition<Integer> zone) {
        return create("creatures/templates/vanilla(name=Dummy Creature,manaCost=" + manaCost + ",attack=" + attack + ",health=" + health + ")", owner, zone);
    }

    protected int[] createSpells(int count, int manaCost, int owner, ComponentDefinition<Integer> zone) {
        return create(count, () -> createSpell(manaCost, owner, zone));
    }

    protected int createSpell(int manaCost, int owner, ComponentDefinition<Integer> zone) {
        return create("tests/dummy_spell(manaCost=" + manaCost + ")", owner, zone);
    }

    protected int create(String template, int owner, ComponentDefinition<Integer> zone) {
        int card = create(template, owner);
        ZoneUtil.addCardToZone(data, card, zone);
        if (zone == Components.CREATURE_ZONE) {
            data.setComponent(card, Components.BOARD);
        }
        return card;
    }

    protected int create(String template, int owner) {
        int card = create(template);
        data.setComponent(card, Components.OWNED_BY, owner);
        return card;
    }

    protected int create(String template) {
        return EntityTemplate.createFromTemplate(data, template);
    }

    protected int[] createForBothPlayers(int countPerPlayer, Function<Integer, Integer> createForPlayer) {
        int[] entities = new int[2 * countPerPlayer];
        int[] allyEntities = create(countPerPlayer, () -> createForPlayer.apply(player));
        System.arraycopy(allyEntities, 0, entities, 0, countPerPlayer);
        int[] opponentEntities = create(countPerPlayer, () -> createForPlayer.apply(opponent));
        System.arraycopy(opponentEntities, 0, entities, countPerPlayer, countPerPlayer);
        return entities;
    }

    private int[] create(int count, Supplier<Integer> create) {
        int[] entities = new int[count];
        for (int i = 0; i < entities.length; i++) {
            entities[i] = create.get();
        }
        return entities;
    }

    protected void castFromHand(int card) {
        castFromHand(card, new int[0]);
    }

    protected void castFromHand(int card, int target) {
        castFromHand(card, new int[] { target });
    }

    protected void castFromHand(int card, int[] targets) {
        cast(getDefaultCastFromHandSpell(card), targets);
    }

    protected void attack(int card, int target) {
        cast(getDefaultAttackSpell(card), target);
    }

    protected void cast(int spell, int target) {
        cast(spell, new int[] { target });
    }

    protected void cast(int spell, int[] targets) {
        fire(new PlaySpellEvent(spell, targets));
    }

    protected void damage(int entity, int damage) {
        fire(new DamageEvent(entity, damage));
    }

    protected void destroy(int entity) {
        fire(new DestructionEvent(entity));
    }

    protected void fire(Event event) {
        gameContext.getEvents().fire(event, random);
        while (gameContext.getEvents().hasPendingEventHandler()) {
            gameContext.getEvents().triggerNextEventHandler();
        }
    }

    protected void assertManaCost(int entity, int value) {
        int defaultCastFromHandSpell = getDefaultCastFromHandSpell(entity);
        assertEquals(value, CostUtil.getEffectiveManaCost(data, defaultCastFromHandSpell));
    }

    protected int getDefaultCastFromHandSpell(int entity) {
        return getSpell(entity, spell -> SpellUtil.isDefaultCastFromHandSpell(data, spell));
    }

    protected int getDefaultAttackSpell(int entity) {
        return getSpell(entity, spell -> SpellUtil.isDefaultAttackSpell(data, spell));
    }

    private int getSpell(int entity, Predicate<Integer> isMatching) {
        int[] spells = data.getComponent(entity, Components.SPELLS);
        for (int spell : spells) {
            if (isMatching.test(spell)) {
                return spell;
            }
        }
        fail("Can't find matching spell.");
        return -1;
    }

    protected void assertCardsCount(int player, ComponentDefinition<Integer> zone, int count) {
        assertEquals(count, getCardsCount(player, zone));
    }

    protected int getCardsCount(int player, ComponentDefinition<Integer> zone) {
        return data.query(zone).count(entity -> data.getComponent(entity, Components.OWNED_BY) == player);
    }

    protected void assertOneCard(int player, ComponentDefinition<Integer> zone, String name) {
        assertCardsCount(player, zone, name, 1);
    }

    protected void assertCardsCount(int player, ComponentDefinition<Integer> zone, String name, int count) {
        assertEquals(count, getCards(player, zone, name).size());
    }

    protected int getCard(int player, ComponentDefinition<Integer> zone, String name) {
        List<Integer> cards = getCards(player, zone, name);
        if (cards.size() != 1) {
            fail("More than one matching card found.");
        }
        return cards.get(0);
    }

    protected List<Integer> getCards(int player, ComponentDefinition<Integer> zone, String name) {
        return data.query(zone).list(entity -> (data.getComponent(entity, Components.OWNED_BY) == player) && (data.getComponent(entity, Components.NAME).equals(name)));
    }

    protected void assertAttack(int[] entities, int value) {
        forEach(entities, entity -> assertAttack(entity, value));
    }

    protected void assertAttack(int entity, int value) {
        assertEquals(value, getAttack(entity));
    }

    protected int getAttack(int entity) {
        return StatsUtil.getEffectiveAttack(data, entity);
    }

    protected void assertHealthAndDamaged(int[] entities, int value) {
        forEach(entities, entity -> assertHealthAndDamaged(entity, value));
    }

    protected void assertHealthAndDamaged(int entity, int value) {
        assertHealth(entity, value);
        assertDamaged(entity);
    }

    protected void assertHealth(int[] entities, int value) {
        forEach(entities, entity -> assertHealth(entity, value));
    }

    protected void assertHealth(int entity, int value) {
        assertEquals(value, getHealth(entity));
    }

    protected int getHealth(int entity) {
        return StatsUtil.getEffectiveHealth(data, entity);
    }

    protected void assertDamaged(int[] entities) {
        forEach(entities, this::assertDamaged);
    }

    protected void assertDamaged(int entity) {
        assertTrue(data.hasComponent(entity, Components.Stats.DAMAGED) || data.hasComponent(entity, Components.Stats.BONUS_DAMAGED));
    }

    protected void assertHasComponent(int[] entities, ComponentDefinition<?> component) {
        forEach(entities, entity -> assertHasComponent(entity, component));
    }

    protected void assertHasComponent(int entity, ComponentDefinition<?> component) {
        assertComponentExistence(entity, component, true);
    }

    protected void assertHasNoComponent(int[] entities, ComponentDefinition<?> component) {
        forEach(entities, entity -> assertHasNoComponent(entity, component));
    }

    protected void assertHasNoComponent(int entity, ComponentDefinition<?> component) {
        assertComponentExistence(entity, component, false);
    }

    protected void assertComponentExistence(int[] entities, ComponentDefinition<?> component, boolean exists) {
        forEach(entities, entity -> assertComponentExistence(entity, component, exists));
    }

    protected <T> void assertComponentExistence(int entity, ComponentDefinition<T> component, boolean exists) {
        assertEquals(exists, data.hasComponent(entity, component));
    }

    protected <T> void assertComponent(int[] entities, ComponentDefinition<T> component, T value) {
        forEach(entities, entity -> assertComponent(entity, component, value));
    }

    protected <T> void assertComponent(int entity, ComponentDefinition<T> component, T value) {
        assertEquals(value, data.getComponent(entity, component));
    }

    private void forEach(int[] entities, Consumer<Integer> assertEntity) {
        for (int entity : entities) {
            assertEntity.accept(entity);
        }
    }
}
