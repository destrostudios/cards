package com.destrostudios.cards.shared.model.internal;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NewCardListCard {
    private int cardId;
    private int foilId;
    private int amount;
}
