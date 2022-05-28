package com.destrostudios.cards.shared.rules;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoStartGameInfo {

    public static void initialize(Kryo kryo) {
        Serializer<PlayerInfo> playerInfoSerializer = new Serializer<PlayerInfo>() {

            @Override
            public void write(Kryo kryo, Output output, PlayerInfo object) {
                output.writeLong(object.getId());
                output.writeString(object.getLogin());
                output.writeString(object.getDeckName());
            }

            @Override
            public PlayerInfo read(Kryo kryo, Input input, Class<PlayerInfo> type) {
                return new PlayerInfo(input.readLong(), input.readString(), input.readString());
            }
        };
        kryo.register(StartGameInfo.class, new Serializer<StartGameInfo>() {

            @Override
            public void write(Kryo kryo, Output output, StartGameInfo object) {
                output.writeString(object.getBoardName());
                kryo.writeObject(output, object.getPlayer1(), playerInfoSerializer);
                kryo.writeObject(output, object.getPlayer2(), playerInfoSerializer);
            }

            @Override
            public StartGameInfo read(Kryo kryo, Input input, Class<StartGameInfo> type) {
                StartGameInfo startGameInfo = new StartGameInfo();
                startGameInfo.setBoardName(input.readString());
                startGameInfo.setPlayer1(kryo.readObject(input, PlayerInfo.class, playerInfoSerializer));
                startGameInfo.setPlayer2(kryo.readObject(input, PlayerInfo.class, playerInfoSerializer));
                return startGameInfo;
            }
        });
    }
}
