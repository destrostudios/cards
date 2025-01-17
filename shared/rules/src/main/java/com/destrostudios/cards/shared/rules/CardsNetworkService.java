package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.IntList;
import com.destrostudios.cards.shared.entities.IntMap;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.rules.actions.*;
import com.destrostudios.cards.shared.rules.cards.Foil;
import com.destrostudios.cards.shared.rules.effects.DiscoverPool;
import com.destrostudios.cards.shared.rules.effects.EffectOptions;
import com.destrostudios.gametools.network.shared.modules.game.GameService;
import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import com.destrostudios.gametools.network.shared.serializers.EnumSerializer;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.FieldSerializer;

import java.time.LocalDateTime;

public class CardsNetworkService implements GameService<GameContext, Action> {

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
        kryo.register(Foil.class, new EnumSerializer<>(Foil.class));
        kryo.register(IntList.class, new Serializer<IntList>() {

            @Override
            public void write(Kryo kryo, Output output, IntList intList) {
                output.writeInt(intList.size());
                for (int i = 0; i < intList.size(); i++) {
                    output.writeInt(intList.get(i));
                }
            }

            @Override
            public IntList read(Kryo kryo, Input input, Class<IntList> type) {
                int size = input.readInt();
                int[] data = input.readInts(size);
                return new IntList(data);
            }
        });
        kryo.register(String[].class);
        kryo.register(AdvancedPrefilter.class, new EnumSerializer<>(AdvancedPrefilter.class));
        kryo.register(AdvancedPrefilter[].class);
        kryo.register(Components.Prefilters.class, new FieldSerializer<>(kryo, Components.Prefilters.class));
        kryo.register(SimpleTarget.class, new EnumSerializer<>(SimpleTarget.class));
        kryo.register(SimpleTarget[].class);
        kryo.register(Components.AddBuff.class);
        kryo.register(Components.Create.class);
        kryo.register(Components.Discover.class);
        kryo.register(DiscoverPool.class, new EnumSerializer<>(DiscoverPool.class));
        kryo.register(Components.TriggerDelayed.class, new FieldSerializer<>(kryo, Components.TriggerDelayed.class));
        kryo.register(ComponentDefinition.class, new Serializer<ComponentDefinition>() {

            @Override
            public void write(Kryo kryo, Output output, ComponentDefinition componentDefinition) {
                output.writeInt(componentDefinition.getId());
            }

            @Override
            public ComponentDefinition read(Kryo kryo, Input input, Class type) {
                return Components.ALL.get(input.readInt());
            }
        });
        kryo.register(ComponentDefinition[].class);
        kryo.register(SimpleEntityData.class, new Serializer<SimpleEntityData>() {

            @Override
            public void write(Kryo kryo, Output output, SimpleEntityData simpleEntityData) {
                for (int i = 0; i < Components.ALL.size(); i++) {
                    IntMap<?> componentMap = simpleEntityData.getComponents()[i];
                    output.writeInt(componentMap.size());
                    componentMap.foreachKey(entity -> {
                        output.writeInt(entity);
                        kryo.writeClassAndObject(output, componentMap.get(entity));
                    });
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
                output.writeLong(gameContext.getActionsHash());
                output.writeLong(gameContext.getLastActionHash());
            }

            @Override
            public GameContext read(Kryo kryo, Input input, Class<GameContext> type) {
                StartGameInfo startGameInfo = kryo.readObject(input, StartGameInfo.class);
                // Create an own instance, so the frontend can add custom handlers
                GameEventHandling eventHandling = new GameEventHandling();
                SimpleEntityData data = kryo.readObject(input, SimpleEntityData.class);
                long actionsHash = input.readLong();
                long lastActionHash = input.readLong();
                return new GameContext(startGameInfo, eventHandling, data, actionsHash, lastActionHash);
            }
        });
        kryo.register(GameStartAction.class);
        kryo.register(MulliganAction.class);
        kryo.register(CastSpellAction.class);
        kryo.register(EffectOptions.class);
        kryo.register(EndTurnAction.class);
    }

    @Override
    public GameContext applyAction(GameContext state, Action action, NetworkRandom random) {
        if (resolveActions) {
            state.fireAndResolveAction(action, random);
        } else {
            state.fireAction(action, random);
        }
        return state;
    }
}
