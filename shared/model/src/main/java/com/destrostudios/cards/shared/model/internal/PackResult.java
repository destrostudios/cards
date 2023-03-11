package com.destrostudios.cards.shared.model.internal;

import com.destrostudios.cards.shared.model.CardListCard;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class PackResult {
    private List<CardListCard> cards;
}
