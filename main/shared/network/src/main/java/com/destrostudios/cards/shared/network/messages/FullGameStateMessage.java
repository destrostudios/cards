package com.destrostudios.cards.shared.network.messages;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.network.Tuple;
import com.jme3.network.AbstractMessage;
import java.util.List;

/**
 *
 * @author Philipp
 */
public class FullGameStateMessage extends AbstractMessage {

    private List<Tuple<ComponentDefinition<?>, List<Tuple<Integer, Object>>>> list;
    private int nextEntity;

    public FullGameStateMessage() {
    }

    public FullGameStateMessage(List<Tuple<ComponentDefinition<?>, List<Tuple<Integer, Object>>>> list, int nextEntity) {
        this.list = list;
        this.nextEntity = nextEntity;
    }

    public List<Tuple<ComponentDefinition<?>, List<Tuple<Integer, Object>>>> getList() {
        return list;
    }

    public int getNextEntity() {
        return nextEntity;
    }
}
