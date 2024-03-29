package com.destrostudios.cards.shared.model;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class CardListCard implements CardIdentifier {
    private int id;
    private Card card;
    private Foil foil;
    private int amount;
}
