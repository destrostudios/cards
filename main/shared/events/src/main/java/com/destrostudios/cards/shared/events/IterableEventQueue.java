package com.destrostudios.cards.shared.events;

public interface IterableEventQueue extends EventQueue {

    boolean processNextEvent();

}