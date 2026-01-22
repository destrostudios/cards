package com.destrostudios.cards.backend.application.modules.bot;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.PlayerActionsGenerator;
import com.destrostudios.cards.shared.rules.actions.Action;
import com.destrostudios.cards.shared.rules.util.ArrayUtil;
import com.destrostudios.gametools.bot.BotActionReplay;
import com.destrostudios.gametools.bot.BotGameState;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import lombok.Getter;

import java.util.*;

public class CardsBotState implements BotGameState<Action, Integer> {

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
    public BotActionReplay<Action> applyAction(Action action) {
        gameContext.fireAndResolveAction(action, botRandom);
        return new BotActionReplay<>(action, ArrayUtil.EMPTY); // TODO: Randomness?
    }

    @Override
    public Integer activeTeam() {
        return gameContext.getData().unique(Components.Player.ACTIVE_PLAYER);
    }

    @Override
    public List<Action> generateActions(Integer team) {
        List<Action> actions = PlayerActionsGenerator.generatePossibleActions(gameContext.getData(), team);
        // The action order matters for the bot, as in case of tied visit counts it simply takes the first tied action instead of a random one.
        // This currently seems to benefit the second player (whose entity gets created last, as the generated actions follow the entity query order which is sorted).
        // The effect is accordingly very noticeable with low bot strength (node visits), and less with a higher one.
        // By shuffling the actions, we guarantee a random action in case of tied visit counts, removing this bias.
        Collections.shuffle(actions);
        return actions;
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
