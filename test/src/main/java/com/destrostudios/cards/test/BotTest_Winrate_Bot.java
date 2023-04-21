package com.destrostudios.cards.test;

import com.destrostudios.cards.backend.application.modules.bot.CardsBotEval;
import com.destrostudios.cards.backend.application.modules.bot.CardsBotState;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.PlayerInfo;
import com.destrostudios.gametools.bot.mcts.MctsBotSettings;

public class BotTest_Winrate_Bot extends BotTest_Winrate {

    public static void main(String[] args) {
        new BotTest_Winrate_Bot().run();
    }

    public BotTest_Winrate_Bot() {
        super(true);
    }

    @Override
    protected void modifyBotSettings(MctsBotSettings<CardsBotState, Event> settings, int player) {
        super.modifyBotSettings(settings, player);
        CardsBotEval.Weights weights = CardsBotEval.getDefaultWeights();
        if (player == 1) {
            // Modify weights to compare
        }
        settings.evaluation = state -> CardsBotEval.eval(state, weights);
    }

    @Override
    protected String getWinnerName(PlayerInfo winner) {
        return winner.getLogin();
    }
}
