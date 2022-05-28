package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.events.Event;
import com.destrostudios.cards.shared.rules.battle.AttackEvent;
import com.destrostudios.cards.shared.rules.cards.Foil;
import com.destrostudios.cards.shared.rules.cards.PlaySpellEvent;
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

import java.util.List;

public class NetworkCardsService implements GameService<GameContext, Event> {

    public NetworkCardsService(boolean resolveActions) {
        this.resolveActions = resolveActions;
        gameStateSerializer = new GameStateSerializer();
    }
    private boolean resolveActions;
    private GameStateSerializer gameStateSerializer;

    @Override
    public void initialize(Kryo kryo) {
        kryo.register(ComponentDefinition.class, new FieldSerializer<>(kryo, ComponentDefinition.class));
        kryo.register(Foil.class, new EnumSerializer<>(Foil.class));
        kryo.register(GameContext.class, new Serializer<GameContext>() {

            @Override
            public void write(Kryo kryo, Output output, GameContext gameContext) {
                FullGameState fullGameState = gameStateSerializer.exportState(gameContext.getData());
                output.writeInt(fullGameState.getList().size());
                for (Tuple<ComponentDefinition<?>, List<Tuple<Integer, Object>>> tuple : fullGameState.getList()) {
                    ComponentDefinition component = tuple.getKey();
                    kryo.writeObject(output, component);
                    output.writeInt(tuple.getValue().size());
                    for (Tuple<Integer, Object> tuple1 : tuple.getValue()) {
                        int entity = tuple1.getKey();
                        Object value = tuple1.getValue();
                        output.writeInt(entity);
                        kryo.writeClassAndObject(output, value);
                    }
                }
                output.writeInt(fullGameState.getNextEntity());
            }

            @Override
            public GameContext read(Kryo kryo, Input input, Class<GameContext> type) {
                SimpleEntityData data = new SimpleEntityData();
                int components = input.readInt();
                for (int i = 0; i < components; i++) {
                    ComponentDefinition component = kryo.readObject(input, ComponentDefinition.class);
                    int entities = input.readInt();
                    for (int r = 0; r < entities; r++) {
                        int entity = input.readInt();
                        Object value = kryo.readClassAndObject(input);
                        data.setComponent(entity, component, value);
                    }
                }
                data.setNextEntity(input.readInt());
                return new GameContext(data);
            }
        });
        kryo.register(GameStartEvent.class, new FieldSerializer<>(kryo, GameStartEvent.class));
        kryo.register(AttackEvent.class, new FieldSerializer<>(kryo, AttackEvent.class));
        kryo.register(PlaySpellEvent.class, new FieldSerializer<>(kryo, PlaySpellEvent.class));
        kryo.register(EndTurnEvent.class, new FieldSerializer<>(kryo, EndTurnEvent.class));
    }

    @Override
    public GameContext applyAction(GameContext state, Event action, NetworkRandom random) {
        state.getEvents().fire(action, random);
        if (resolveActions) {
            while (state.getEvents().hasNextTriggeredHandler()) {
                state.getEvents().triggerNextHandler();
            }
        }
        return state;
    }
}
