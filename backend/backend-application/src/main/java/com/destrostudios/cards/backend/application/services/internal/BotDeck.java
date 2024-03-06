package com.destrostudios.cards.backend.application.services.internal;

import com.destrostudios.cards.shared.model.CardList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BotDeck {
    private int id;
    private CardList deckCardList;
    private double elo;
    private int evaluationGames;
}
