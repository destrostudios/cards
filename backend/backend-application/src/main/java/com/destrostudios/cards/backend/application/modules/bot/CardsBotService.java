package com.destrostudios.cards.backend.application.modules.bot;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.gametools.bot.BotGameService;

public class CardsBotService implements BotGameService<CardsBotState, Event, Integer, CardsBotState> {

    @Override
    public CardsBotState serialize(CardsBotState cardsBotState) {
        return cardsBotState;
    }

    @Override
    public CardsBotState deserialize(CardsBotState data) {
        return new CardsBotState(data);
    }
}
