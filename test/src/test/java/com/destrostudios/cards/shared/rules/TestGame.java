package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.application.ApplicationSetup;
import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.entities.templates.EntityTemplate;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.Queue;
import com.destrostudios.cards.shared.rules.battle.DestructionEvent;
import com.destrostudios.cards.shared.rules.cards.PlaySpellEvent;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import com.destrostudios.cards.shared.rules.util.CostUtil;
import com.destrostudios.cards.shared.rules.util.SpellUtil;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import com.destrostudios.cards.shared.rules.util.ZoneUtil;
import com.destrostudios.cards.shared.rules.wrappers.Vanilla;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import org.junit.jupiter.api.BeforeEach;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    protected Vanilla createVanilla(int manaCost, int attack, int health, int owner, ComponentDefinition<Integer> zone) {
        int card = create("creatures/templates/vanilla(name=Generic Target,manaCost=" + manaCost + ",attack=" + attack + ",health=" + health + ")", owner, zone);
        return new Vanilla(card, manaCost, attack, health);
    }

    protected int create(String templateName, int owner, ComponentDefinition<Integer> zone) {
        int card = create(templateName, owner);
        ZoneUtil.addCardToZone(data, card, zone);
        if (zone == Components.CREATURE_ZONE) {
            data.setComponent(card, Components.BOARD);
        }
        return card;
    }

    protected int create(String templateName, int owner) {
        int card = create(templateName);
        data.setComponent(card, Components.OWNED_BY, owner);
        return card;
    }

    protected int create(String templateName) {
        return EntityTemplate.createFromTemplate(data, templateName);
    }

    protected void castFromHand(int card) {
        castFromHand(card, new int[0]);
    }

    protected void castFromHand(int card, int[] targets) {
        cast(getDefaultCastFromHandSpell(card), targets);
    }

    protected void attack(int card, int target) {
        cast(getDefaultAttackSpell(card), new int[] { target });
    }

    protected void cast(int spell, int[] targets) {
        fire(new PlaySpellEvent(spell, targets));
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
        throw new RuntimeException("Can't find matching spell.");
    }

    protected void assertAttack(int entity, int value) {
        assertEquals(value, StatsUtil.getEffectiveAttack(data, entity));
    }

    protected void assertHealth(int entity, int value) {
        assertEquals(value, StatsUtil.getEffectiveHealth(data, entity));
    }

    protected void assertHasComponent(int entity, ComponentDefinition<?> component) {
        assertComponentExistence(entity, component, true);
    }

    protected void assertHasNoComponent(int entity, ComponentDefinition<?> component) {
        assertComponentExistence(entity, component, false);
    }

    protected <T> void assertComponentExistence(int entity, ComponentDefinition<T> component, boolean exists) {
        assertEquals(exists, data.hasComponent(entity, component));
    }

    protected <T> void assertComponent(int entity, ComponentDefinition<T> component, T value) {
        assertEquals(value, data.getComponent(entity, component));
    }
}
