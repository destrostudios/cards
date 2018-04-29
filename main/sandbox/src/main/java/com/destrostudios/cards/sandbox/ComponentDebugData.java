package com.destrostudios.cards.sandbox;

import java.util.function.IntFunction;

/**
 *
 * @author Philipp
 */
public class ComponentDebugData {

    final String name;
    final IntFunction<?> converter;

    public ComponentDebugData(String name, IntFunction<?> converter) {
        this.name = name;
        this.converter = converter;
    }
    
}
