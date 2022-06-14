package com.destrostudios.cards.backend.application.modules.bot;

import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.GameContext;
import com.destrostudios.cards.shared.rules.NetworkCardsService;
import com.destrostudios.gametools.bot.BotGameService;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class CardsBotService implements BotGameService<CardsBotState, Event, Integer, byte[]> {

    public CardsBotService() {
        kryo = new Kryo();
        kryo.setReferences(false);
        kryo.setCopyReferences(false);
        // UGLY HACKING
        new NetworkCardsService(false).initialize(kryo);
    }
    private Kryo kryo;

    @Override
    public byte[] serialize(CardsBotState cardsBotState) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Output output = new Output(outputStream);
        kryo.writeObject(output, cardsBotState.getGameContext());
        output.flush();
        return outputStream.toByteArray();
    }

    @Override
    public CardsBotState deserialize(byte[] data) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Input input = new Input(inputStream);
        GameContext gameContext = kryo.readObject(input, GameContext.class);
        return new CardsBotState(gameContext);
    }
}
