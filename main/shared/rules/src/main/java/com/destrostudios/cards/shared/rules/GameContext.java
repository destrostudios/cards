package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.events.EventDispatcher;
import com.destrostudios.cards.shared.events.EventQueue;
import com.destrostudios.cards.shared.events.EventQueueImpl;
import com.destrostudios.cards.shared.rules.battle.ArmorEventHandler;
import com.destrostudios.cards.shared.rules.battle.BattleEvent;
import com.destrostudios.cards.shared.rules.battle.BattleEventHandler;
import com.destrostudios.cards.shared.rules.battle.DamageEvent;
import com.destrostudios.cards.shared.rules.battle.DamageEventHandler;
import com.destrostudios.cards.shared.rules.battle.DeclareAttackEvent;
import com.destrostudios.cards.shared.rules.battle.DeclareAttackEventHandler;
import com.destrostudios.cards.shared.rules.battle.DeclareBlockEvent;
import com.destrostudios.cards.shared.rules.battle.DeclareBlockEventHandler;
import com.destrostudios.cards.shared.rules.battle.SetHealthEvent;
import com.destrostudios.cards.shared.rules.battle.SetHealthEventHandler;
import com.destrostudios.cards.shared.rules.battle.StartBattleEventHandler;
import com.destrostudios.cards.shared.rules.cards.AddCardToHandEvent;
import com.destrostudios.cards.shared.rules.cards.AddCardToHandEventHandler;
import com.destrostudios.cards.shared.rules.cards.AddCardToLibraryEvent;
import com.destrostudios.cards.shared.rules.cards.AddCardToLibraryEventHandler;
import com.destrostudios.cards.shared.rules.cards.DrawCardEvent;
import com.destrostudios.cards.shared.rules.cards.DrawCardEventHandler;
import com.destrostudios.cards.shared.rules.cards.RemoveCardFromHandEvent;
import com.destrostudios.cards.shared.rules.cards.RemoveCardFromHandEventHandler;
import com.destrostudios.cards.shared.rules.cards.RemoveCardFromLibraryEvent;
import com.destrostudios.cards.shared.rules.cards.RemoveCardFromLibraryEventHandler;
import com.destrostudios.cards.shared.rules.cards.ShuffleLibraryEvent;
import com.destrostudios.cards.shared.rules.cards.ShuffleLibraryEventHandler;
import com.destrostudios.cards.shared.rules.cards.ShuffleLibraryOnGameStartHandler;
import com.destrostudios.cards.shared.rules.cards.UpkeepDrawEventHandler;
import com.destrostudios.cards.shared.rules.game.StartGameEvent;
import com.destrostudios.cards.shared.rules.moves.MoveGenerator;
import com.destrostudios.cards.shared.rules.turns.battle.EndBattlePhaseEvent;
import com.destrostudios.cards.shared.rules.turns.battle.EndBattlePhaseEventHandler;
import com.destrostudios.cards.shared.rules.turns.battle.StartBattlePhaseEvent;
import com.destrostudios.cards.shared.rules.turns.battle.StartBattlePhaseEventHandler;
import com.destrostudios.cards.shared.rules.turns.main.EndMainPhaseEvent;
import com.destrostudios.cards.shared.rules.turns.main.EndMainPhaseEventHandler;
import com.destrostudios.cards.shared.rules.turns.main.StartMainPhaseEvent;
import com.destrostudios.cards.shared.rules.turns.main.StartMainPhaseEventHandler;
import com.destrostudios.cards.shared.rules.turns.respond.EndRespondPhaseEvent;
import com.destrostudios.cards.shared.rules.turns.respond.EndRespondPhaseEventHandler;
import com.destrostudios.cards.shared.rules.turns.respond.StartRespondPhaseEvent;
import com.destrostudios.cards.shared.rules.turns.respond.StartRespondPhaseEventHandler;
import com.destrostudios.cards.shared.rules.turns.upkeep.EndUpkeepPhaseEvent;
import com.destrostudios.cards.shared.rules.turns.upkeep.EndUpkeepPhaseEventHandler;
import com.destrostudios.cards.shared.rules.turns.upkeep.StartUpkeepPhaseEvent;
import com.destrostudios.cards.shared.rules.turns.upkeep.StartUpkeepPhaseEventHandler;
import java.util.function.IntUnaryOperator;

/**
 *
 * @author Philipp
 */
public class GameContext {

    private final EntityData data;
    private final EventDispatcher dispatcher;
    private final EventQueue events;
    private final MoveGenerator moveGenerator;
    private final IntUnaryOperator random;

    public GameContext(IntUnaryOperator random) {
        this.random = random;
        data = new EntityData();
        dispatcher = new EventDispatcher();
        events = new EventQueueImpl(dispatcher::fire);
        moveGenerator = new MoveGenerator(data);
        initListeners();
    }

    private void initListeners() {
        dispatcher.addListeners(BattleEvent.class, new BattleEventHandler(data, events));
        dispatcher.addListeners(DamageEvent.class,
                new ArmorEventHandler(data, events),
                new DamageEventHandler(data, events));
        dispatcher.addListeners(SetHealthEvent.class, new SetHealthEventHandler(data, events));
        dispatcher.addListeners(DeclareAttackEvent.class, new DeclareAttackEventHandler(data, events));
        dispatcher.addListeners(DeclareBlockEvent.class, new DeclareBlockEventHandler(data, events));
        dispatcher.addListeners(StartRespondPhaseEvent.class, new StartRespondPhaseEventHandler(data, events));
        dispatcher.addListeners(EndRespondPhaseEvent.class, new EndRespondPhaseEventHandler(data, events));
        dispatcher.addListeners(StartBattlePhaseEvent.class,
                new StartBattlePhaseEventHandler(data, events),
                new StartBattleEventHandler(data, events));
        dispatcher.addListeners(EndBattlePhaseEvent.class, new EndBattlePhaseEventHandler(data, events));
        dispatcher.addListeners(StartUpkeepPhaseEvent.class,
                new StartUpkeepPhaseEventHandler(data, events),
                new UpkeepDrawEventHandler(data, events));
        dispatcher.addListeners(EndUpkeepPhaseEvent.class, new EndUpkeepPhaseEventHandler(data, events));
        dispatcher.addListeners(StartMainPhaseEvent.class, new StartMainPhaseEventHandler(data, events));
        dispatcher.addListeners(EndMainPhaseEvent.class, new EndMainPhaseEventHandler(data, events));
        dispatcher.addListeners(StartGameEvent.class, new ShuffleLibraryOnGameStartHandler(data, events));
        dispatcher.addListeners(ShuffleLibraryEvent.class, new ShuffleLibraryEventHandler(data, events, random));
        dispatcher.addListeners(DrawCardEvent.class, new DrawCardEventHandler(data, events));
        dispatcher.addListeners(AddCardToHandEvent.class, new AddCardToHandEventHandler(data, events));
        dispatcher.addListeners(RemoveCardFromHandEvent.class, new RemoveCardFromHandEventHandler(data, events));
        dispatcher.addListeners(AddCardToLibraryEvent.class, new AddCardToLibraryEventHandler(data, events));
        dispatcher.addListeners(RemoveCardFromLibraryEvent.class, new RemoveCardFromLibraryEventHandler(data, events));
    }

    public EntityData getData() {
        return data;
    }

    public EventDispatcher getDispatcher() {
        return dispatcher;
    }

    public EventQueue getEvents() {
        return events;
    }

    public MoveGenerator getMoveGenerator() {
        return moveGenerator;
    }

    public IntUnaryOperator getRandom() {
        return random;
    }

}
