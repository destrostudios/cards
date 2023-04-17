package com.destrostudios.cards.backend.application.modules.bot;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.gametools.bot.BotGameService;

public class CardsBotService implements BotGameService<CardsBotState, Event, Integer, CardsBotState> {

    @Override
    public CardsBotState serialize(CardsBotState state) {
        return state;
    }

    @Override
    public CardsBotState deserialize(CardsBotState source, CardsBotState target) {
        if (target == null) {
            target = new CardsBotState();
        }
        target.copyFrom(source);
        return target;
    }
}
