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
import java.util.Map;

public class CardsNetworkService implements GameService<GameContext, Event> {

    public CardsNetworkService(boolean resolveActions) {
        this.resolveActions = resolveActions;
    }
    private boolean resolveActions;

    @Override
    public void initialize(Kryo kryo) {
        kryo.register(PlayerInfo.class, new Serializer<PlayerInfo>() {

            @Override
            public void write(Kryo kryo, Output output, PlayerInfo object) {
                output.writeInt(object.getId());
                output.writeString(object.getLogin());
                output.writeInt(object.getLibraryTemplates().size());
                for (String template : object.getLibraryTemplates()) {
                    output.writeString(template);
                }
            }

            @Override
            public PlayerInfo read(Kryo kryo, Input input, Class<PlayerInfo> type) {
                int id = input.readInt();
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
        kryo.register(String[].class, new Serializer<String[]>() {

            @Override
            public void write(Kryo kryo, Output output, String[] object) {
                output.writeInt(object.length);
                for (String string : object) {
                    output.writeString(string);
                }
            }

            @Override
            public String[] read(Kryo kryo, Input input, Class<String[]> type) {
                int length = input.readInt();
                String[] object = new String[length];
                for (int i = 0; i < length; i++) {
                    object[i] = input.readString();
                }
                return object;
            }
        });
        kryo.register(Foil.class, new EnumSerializer<>(Foil.class));
        kryo.register(TargetPrefilter.class, new EnumSerializer<>(TargetPrefilter.class));
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
