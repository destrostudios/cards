package com.destrostudios.cards.shared.model;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class BaseCardIdentifier implements CardIdentifier {
    private Card card;
    private Foil foil;
}
