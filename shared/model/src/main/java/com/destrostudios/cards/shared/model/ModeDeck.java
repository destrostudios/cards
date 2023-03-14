package com.destrostudios.cards.shared.model;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class ModeDeck {
    private int id;
    private int modeId;
    private CardList deckCardList;
}
