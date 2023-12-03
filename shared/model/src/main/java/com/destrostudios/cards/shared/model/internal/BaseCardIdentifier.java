package com.destrostudios.cards.shared.model.internal;

import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.model.CardIdentifier;
import com.destrostudios.cards.shared.model.Foil;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class BaseCardIdentifier implements CardIdentifier {
    private Card card;
    private Foil foil;
}
