package com.destrostudios.cards.shared.network;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.jme3.network.AbstractMessage;

import java.util.List;

/**
 *
 * @author Philipp
 */
public class FullGameState extends AbstractMessage {

    private List<Tuple<ComponentDefinition<?>, List<Tuple<Integer, Object>>>> list;
    private int nextEntity;

    public FullGameState() {
    }

    public FullGameState(List<Tuple<ComponentDefinition<?>, List<Tuple<Integer, Object>>>> list, int nextEntity) {
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