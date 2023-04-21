package com.destrostudios.cards.test;

import com.destrostudios.cards.shared.model.CardList;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.PlayerInfo;
import com.destrostudios.cards.shared.rules.StartGameInfo;

import java.util.Random;

public class BotTest_Winrate_Deck extends BotTest_Winrate {

    public static void main(String[] args) {
        new BotTest_Winrate_Deck().run();
    }

    public BotTest_Winrate_Deck() {
        super(false);
    }

    @Override
    protected String getWinnerName(PlayerInfo winner) {
        return winner.getDeck().getName();
    }

    @Override
    protected StartGameInfo getStartGameInfo(Random random) {
        return getDefaultStartGameInfo(getRandomDeck(random), getRandomDeck(random));
    }

    private CardList getRandomDeck(Random random) {
        Mode mode = modeService.getMode(GameConstants.MODE_NAME_BRAWL);
        return mode.getDecks().get(random.nextInt(mode.getDecks().size())).getDeckCardList();
    }
}
