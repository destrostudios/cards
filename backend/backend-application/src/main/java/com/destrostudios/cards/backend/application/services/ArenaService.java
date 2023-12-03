package com.destrostudios.cards.backend.application.services;

import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.rules.GameConstants;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
public class ArenaService {

    private CardService cardService;
    private final Random seedRandom = new Random();

    public long generateSeed() {
        return seedRandom.nextLong();
    }

    public List<Card> getDraftCards(long seed) {
        ArrayList<Card> draftCards = new ArrayList<>();
        List<Card> allCards = cardService.getCards();
        Random cardIndexRandom = new Random(seed);
        for (int i = 0; i < GameConstants.CARDS_PER_ARENA_DRAFT; i++) {
            int cardIndex = cardIndexRandom.nextInt(allCards.size());
            draftCards.add(allCards.get(cardIndex));
        }
        return draftCards;
    }
}
