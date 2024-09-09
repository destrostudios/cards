package com.destrostudios.cards.backend.application.modules.bot;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.PlayerActionsGenerator;
import com.destrostudios.cards.shared.rules.util.ArrayUtil;
import com.destrostudios.gametools.bot.BotActionReplay;
import com.destrostudios.gametools.bot.BotGameState;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import lombok.Getter;

import java.util.*;

public class CardsBotState implements BotGameState<Event, Integer> {

    // Used to create a new bot worker thread needing an own instance, will be set up immediately via copyFrom
    public CardsBotState() {
        gameContext = new GameContext();
    }

    // Used to create the actual first instance
    public CardsBotState(GameContext gameContext, Random botRandom) {
        this.gameContext = gameContext;
        this.botRandom = new SimpleRandom(botRandom);
        players = gameContext.getData().list(Components.NEXT_PLAYER).boxed();
    }
    @Getter
    private GameContext gameContext;
    private NetworkRandom botRandom;
    private List<Integer> players;

    public void copyFrom(CardsBotState source) {
        gameContext.copyFrom(source.gameContext);
        botRandom = source.botRandom;
        players = source.players;
    }

    @Override
    public BotActionReplay<Event> applyAction(Event action) {
        gameContext.fireAndResolveEvent(action, botRandom);
        return new BotActionReplay<>(action, ArrayUtil.EMPTY); // TODO: Randomness?
    }

    @Override
    public Integer activeTeam() {
        return gameContext.getData().unique(Components.Player.ACTIVE_PLAYER);
    }

    @Override
    public List<Event> generateActions(Integer team) {
        return PlayerActionsGenerator.generatePossibleActions(gameContext.getData(), team);
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
