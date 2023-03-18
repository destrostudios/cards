package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.events.EventHandlers;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.rules.abilities.*;
import com.destrostudios.cards.shared.rules.auras.*;
import com.destrostudios.cards.shared.rules.battle.*;
import com.destrostudios.cards.shared.rules.buffs.*;
import com.destrostudios.cards.shared.rules.cards.*;
import com.destrostudios.cards.shared.rules.cards.zones.*;
import com.destrostudios.cards.shared.rules.effects.*;
import com.destrostudios.cards.shared.rules.game.*;
import com.destrostudios.cards.shared.rules.game.turn.*;
import lombok.Getter;

public class GameContext {

    public GameContext(GameContext gameContext) {
        this(gameContext.startGameInfo, new SimpleEntityData(gameContext.data));
    }

    public GameContext(StartGameInfo startGameInfo, SimpleEntityData data) {
        this.startGameInfo = startGameInfo;
        this.data = data;
        events = new EventQueue();
        initListeners();
    }
    @Getter
    private StartGameInfo startGameInfo;
    @Getter
    private SimpleEntityData data;
    @Getter
    private EventQueue events;
    @Getter
    private Integer winner;

    private void initListeners() {
        addEventHandlers(events.instant(), AddCardToBoardEvent.class,
                new AddCardToBoardHandler(),
                new ActivateDivineShieldOnAddToBoardHandler()
        );
        addEventHandler(events.instant(), AddCardToBoardZoneEvent.class, new AddCardToBoardZoneHandler());
        addEventHandler(events.instant(), AddCardToCreatureZoneEvent.class, new AddCardToCreatureZoneHandler());
        addEventHandler(events.instant(), AddCardToGraveyardEvent.class, new AddCardToGraveyardHandler());
        addEventHandler(events.instant(), AddCardToHandEvent.class, new AddCardToHandHandler());
        addEventHandler(events.instant(), AddCardToLibraryEvent.class, new AddCardToLibraryHandler());
        addEventHandler(events.instant(), AddCardToSpellZoneEvent.class, new AddCardToSpellZoneHandler());
        addEventHandlers(events.instant(), AddCardToZoneEvent.class,
                new RemoveFromOtherZonesOnAddHandler(),
                new AddCardToZoneHandler()
        );
        addEventHandler(events.instant(), AddManaEvent.class, new AddManaHandler());
        addEventHandler(events.instant(), SetAvailableManaEvent.class, new SetAvailableManaHandler());
        addEventHandler(events.instant(), SetManaEvent.class, new SetManaHandler());
        addEventHandler(events.instant(), BattleEvent.class, new BattleHandler());
        addEventHandlers(events.instant(), DamageEvent.class,
                new DamageHandler(),
                new DestroyCardsWithZeroHealthHandler()
        );
        addEventHandler(events.instant(), HealEvent.class, new HealHandler());
        addEventHandler(events.instant(), DestructionEvent.class, new DestructionHandler());
        addEventHandler(events.resolved(), DestructionEvent.class, new TriggerOnDeathHandler());
        addEventHandler(events.instant(), DrawCardEvent.class, new DrawCardHandler());
        addEventHandlers(events.instant(), EndTurnEvent.class,
                new EndTurnHandler(),
                new ResetCurrentCastsPerTurnOnEndTurnHandler()
        );
        addEventHandlers(events.instant(), GameStartEvent.class,
                new ShuffleAllLibrariesOnGameStartHandler(),
                new DrawCardsOnGameStartHandler(),
                new SetStartingPlayerHandler());
        addEventHandler(events.instant(), PayManaEvent.class, new PayManaHandler());
        addEventHandlers(events.instant(), PlaySpellEvent.class,
                new PlaySpellHandler(),
                new IncreaseCurrentCastsPerTurnHandler()
        );
        addEventHandlers(events.instant(), RemoveCardFromBoardZoneEvent.class,
                new RemoveCardFromBoardZoneHandler(),
                new RemoveDamageOnRemoveFromBoardHandler(),
                new DeactivateDivineShieldOnRemoveFromBoardHandler(),
                new RemoveBuffsOnRemoveFromBoardHandler()
        );
        addEventHandler(events.instant(), RemoveCardFromCreatureZoneEvent.class, new RemoveCardFromCreatureZoneHandler());
        addEventHandler(events.instant(), RemoveCardFromGraveyardEvent.class, new RemoveCardFromGraveyardHandler());
        addEventHandler(events.instant(), RemoveCardFromHandEvent.class, new RemoveCardFromHandHandler());
        addEventHandler(events.instant(), RemoveCardFromLibraryEvent.class, new RemoveCardFromLibraryHandler());
        addEventHandler(events.instant(), RemoveCardFromSpellZoneEvent.class, new RemoveCardFromSpellZoneHandler());
        addEventHandler(events.instant(), RemoveCardFromZoneEvent.class, new RemoveCardFromZoneHandler());
        addEventHandler(events.instant(), ShuffleLibraryEvent.class, new ShuffleLibraryHandler());
        addEventHandlers(events.instant(), StartTurnEvent.class,
                new StartTurnHandler(),
                new IncreaseAvailableManaOnTurnStartHandler(),
                new SetManaToAvailableManaOnTurnStartHandler(),
                new DrawCardOnTurnStartHandler()
        );
        addEventHandler(events.instant(), TriggerEffectTriggerIfPossibleEvent.class, new TriggerEffectTriggerIfPossibleHandler());
        addEventHandler(events.instant(), TriggerEffectEvent.class, new TriggerEffectHandler());
        addEventHandler(events.instant(), AddAuraEvent.class, new AddAuraHandler());
        addEventHandler(events.instant(), RemoveAuraEvent.class, new RemoveAuraHandler());
        addEventHandler(events.instant(), AddBuffEvent.class, new AddBuffHandler());
        addEventHandler(events.instant(), RemoveBuffEvent.class, new RemoveBuffHandler());
        addEventHandler(events.instant(), SummonEvent.class, new SummonHandler());
        addEventHandler(events.instant(), ConditionsAffectedEvent.class, new CheckBonusDamageHandler());
        addEventHandler(events.instant(), GameOverEvent.class, new GameOverHandler(this));
    }

    private <T extends Event> void addEventHandlers(EventHandlers eventHandlers, Class<T> eventClass, GameEventHandler<T>... handlers) {
        for (GameEventHandler<T> handler : handlers) {
            addEventHandler(eventHandlers, eventClass, handler);
        }
    }

    private <T extends Event> void addEventHandler(EventHandlers eventHandlers, Class<T> eventClass, GameEventHandler<T> handler) {
        handler.data = data;
        handler.events = events;
        eventHandlers.add(eventClass, handler::handle);
    }

    public void onGameOver(int winner) {
        this.winner = winner;
    }

    public boolean isGameOver() {
        return (winner != null);
    }

    public int getUserId(int player) {
        String login = data.getComponent(player, Components.NAME);
        for (PlayerInfo playerInfo : startGameInfo.getPlayers()) {
            if (playerInfo.getLogin().equals(login)) {
                return playerInfo.getId();
            }
        }
        throw new RuntimeException("User with login '" + login + "' not found.");
    }
}
