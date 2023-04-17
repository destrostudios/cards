package com.destrostudios.cards.shared.events;

public record PendingEventHandler<E extends Event, C>(E event, EventHandler<E, C> handler) {}
