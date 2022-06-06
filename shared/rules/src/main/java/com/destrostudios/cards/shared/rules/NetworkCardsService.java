package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.events.Event;
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

import java.util.LinkedList;
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
        kryo.register(PlayerInfo.class, new Serializer<PlayerInfo>() {

            @Override
            public void write(Kryo kryo, Output output, PlayerInfo object) {
                output.writeLong(object.getId());
                output.writeString(object.getLogin());
                output.writeInt(object.getLibraryTemplates().size());
                for (String template : object.getLibraryTemplates()) {
                    output.writeString(template);
                }
            }

            @Override
            public PlayerInfo read(Kryo kryo, Input input, Class<PlayerInfo> type) {
                long id = input.readLong();
                String login = input.readString();
                LinkedList<String> libraryTemplates = new LinkedList<>();
                int librarySize = input.readInt();
                for (int i = 0; i < librarySize; i++) {
                    libraryTemplates.add(input.readString());
                }
                return new PlayerInfo(id, login, libraryTemplates);
            }
        });
        kryo.register(StartGameInfo.class, new Serializer<StartGameInfo>() {

            @Override
            public void write(Kryo kryo, Output output, StartGameInfo object) {
                output.writeString(object.getBoardName());
                kryo.writeObject(output, object.getPlayer1());
                kryo.writeObject(output, object.getPlayer2());
            }

            @Override
            public StartGameInfo read(Kryo kryo, Input input, Class<StartGameInfo> type) {
                StartGameInfo startGameInfo = new StartGameInfo();
                startGameInfo.setBoardName(input.readString());
                startGameInfo.setPlayer1(kryo.readObject(input, PlayerInfo.class));
                startGameInfo.setPlayer2(kryo.readObject(input, PlayerInfo.class));
                return startGameInfo;
            }
        });
        kryo.register(ComponentDefinition.class, new FieldSerializer<>(kryo, ComponentDefinition.class));
        kryo.register(Foil.class, new EnumSerializer<>(Foil.class));
        kryo.register(GameContext.class, new Serializer<GameContext>() {

            @Override
            public void write(Kryo kryo, Output output, GameContext gameContext) {
                kryo.writeObject(output, gameContext.getStartGameInfo());
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
                StartGameInfo startGameInfo = kryo.readObject(input, StartGameInfo.class);
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
                return new GameContext(startGameInfo, data);
            }
        });
        kryo.register(GameStartEvent.class, new FieldSerializer<>(kryo, GameStartEvent.class));
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
