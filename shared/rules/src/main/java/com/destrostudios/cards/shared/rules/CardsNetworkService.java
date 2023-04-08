package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.cards.Foil;
import com.destrostudios.cards.shared.rules.cards.CastSpellEvent;
import com.destrostudios.cards.shared.rules.game.GameStartEvent;
import com.destrostudios.cards.shared.rules.game.turn.EndTurnEvent;
import com.destrostudios.gametools.network.shared.modules.game.GameService;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import com.destrostudios.gametools.network.shared.serializers.EnumSerializer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer;

import java.time.LocalDateTime;
import java.util.Map;

public class CardsNetworkService implements GameService<GameContext, Event> {

    public CardsNetworkService(boolean resolveActions) {
        this.resolveActions = resolveActions;
    }
    private boolean resolveActions;

    @Override
    public void initialize(Kryo kryo) {
        kryo.register(LocalDateTime.class, new Serializer<LocalDateTime>() {

            @Override
            public void write(Kryo kryo, Output output, LocalDateTime localDateTime) {
                output.writeString(localDateTime.toString());
            }

            @Override
            public LocalDateTime read(Kryo kryo, Input input, Class<LocalDateTime> type) {
                return LocalDateTime.parse(input.readString());
            }
        });
        kryo.register(PlayerInfo.class);
        kryo.register(StartGameInfo.class);
        kryo.register(String[].class);
        kryo.register(Foil.class, new EnumSerializer<>(Foil.class));
        kryo.register(TargetPrefilter.class, new EnumSerializer<>(TargetPrefilter.class));
        kryo.register(Components.AddBuff.class);
        kryo.register(Components.Create.class);
        kryo.register(SimpleEntityData.class, new Serializer<SimpleEntityData>() {

            @Override
            public void write(Kryo kryo, Output output, SimpleEntityData simpleEntityData) {
                for (int i = 0; i < Components.ALL.size(); i++) {
                    Map<Integer, Object> componentMap = simpleEntityData.getComponents()[i];
                    output.writeInt(componentMap.size());
                    for (Map.Entry<Integer, Object> entry : componentMap.entrySet()) {
                        output.writeInt(entry.getKey());
                        kryo.writeClassAndObject(output, entry.getValue());
                    }
                }
                output.writeInt(simpleEntityData.getNextEntity());
            }

            @Override
            public SimpleEntityData read(Kryo kryo, Input input, Class<SimpleEntityData> type) {
                SimpleEntityData data = new SimpleEntityData(Components.ALL);
                for (int i = 0; i < Components.ALL.size(); i++) {
                    ComponentDefinition component = Components.ALL.get(i);
                    int entities = input.readInt();
                    for (int r = 0; r < entities; r++) {
                        int entity = input.readInt();
                        Object value = kryo.readClassAndObject(input);
                        data.setComponent(entity, component, value);
                    }
                }
                data.setNextEntity(input.readInt());
                return data;
            }
        });
        kryo.register(GameContext.class, new Serializer<GameContext>() {

            @Override
            public void write(Kryo kryo, Output output, GameContext gameContext) {
                kryo.writeObject(output, gameContext.getStartGameInfo());
                kryo.writeObject(output, gameContext.getData());
            }

            @Override
            public GameContext read(Kryo kryo, Input input, Class<GameContext> type) {
                StartGameInfo startGameInfo = kryo.readObject(input, StartGameInfo.class);
                SimpleEntityData data = kryo.readObject(input, SimpleEntityData.class);
                return new GameContext(startGameInfo, data);
            }
        });
        kryo.register(GameStartEvent.class, new FieldSerializer<>(kryo, GameStartEvent.class));
        kryo.register(CastSpellEvent.class, new FieldSerializer<>(kryo, CastSpellEvent.class));
        kryo.register(EndTurnEvent.class, new FieldSerializer<>(kryo, EndTurnEvent.class));
    }

    @Override
    public GameContext applyAction(GameContext state, Event action, NetworkRandom random) {
        state.getEvents().fire(action, random);
        if (resolveActions) {
            while (state.getEvents().hasPendingEventHandler()) {
                state.getEvents().triggerNextEventHandler();
            }
        }
        return state;
    }
}
