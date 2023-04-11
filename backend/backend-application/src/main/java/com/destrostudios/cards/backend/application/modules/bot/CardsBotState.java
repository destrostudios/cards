package com.destrostudios.cards.backend.application.modules.bot;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.PlayerActionsGenerator;
import com.destrostudios.gametools.bot.BotActionReplay;
import com.destrostudios.gametools.bot.BotGameState;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import lombok.Getter;

import java.util.*;

public class CardsBotState implements BotGameState<Event, Integer> {

    public CardsBotState(CardsBotState cardsBotState) {
        this.gameContext = new GameContext(cardsBotState.gameContext);
        players = cardsBotState.players;
        playerActionsGenerator = cardsBotState.playerActionsGenerator;
        random = cardsBotState.random;
    }

    public CardsBotState(GameContext gameContext, NetworkRandom random) {
        this.gameContext = gameContext;
        players = gameContext.getData().query(Components.NEXT_PLAYER).list().boxed();
        playerActionsGenerator = new PlayerActionsGenerator();
        this.random = random;
    }
    @Getter
    private GameContext gameContext;
    private List<Integer> players;
    private PlayerActionsGenerator playerActionsGenerator;
    private NetworkRandom random;

    @Override
    public BotActionReplay<Event> applyAction(Event action) {
        gameContext.getEvents().fire(action, random);
        while (gameContext.getEvents().hasPendingEventHandler()) {
            gameContext.getEvents().triggerNextEventHandler();
        }
        return new BotActionReplay<>(action, new int[0]); // TODO: Randomness?
    }

    @Override
    public Integer activeTeam() {
        return gameContext.getData().query(Components.Game.ACTIVE_PLAYER).unique();
    }

    @Override
    public List<Event> generateActions(Integer team) {
        return playerActionsGenerator.generatePossibleActions(gameContext.getData(), team);
    }

    @Override
    public boolean isGameOver() {
        return gameContext.isGameOver();
    }

    @Override
    public List<Integer> getTeams() {
        return players;
    }
}
