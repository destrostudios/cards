package com.destrostudios.cards.shared.model;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class UserModeDeck implements Deck {
    private int id;
    private int userId;
    private Mode mode;
    private CardList deckCardList;
}
