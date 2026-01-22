package com.destrostudios.cards.shared.model;

public interface Deck {
    int getId();
    Power getPower();
    CardList getDeckCardList();
}