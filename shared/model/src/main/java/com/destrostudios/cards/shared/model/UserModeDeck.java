package com.destrostudios.cards.shared.model;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class UserModeDeck implements Deck {
    private int id;
    private int userModeId;
    private CardList deckCardList;
}
