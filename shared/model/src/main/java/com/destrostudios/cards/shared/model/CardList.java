package com.destrostudios.cards.shared.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class CardList {
    private int id;
    private String name;
    private LocalDateTime creationDate;
    private LocalDateTime lastModificationDate;
    private List<CardListCard> cards;

    @JsonIgnore
    public int getSize() {
        return cards.stream().mapToInt(CardListCard::getAmount).sum();
    }

    @JsonIgnore
    public int getCardAmount(int cardId, int foilId) {
        return cards.stream().filter(cardListCard -> cardListCard.getCard().getId() == cardId && cardListCard.getFoil().getId() == foilId).findFirst().map(CardListCard::getAmount).orElse(0);
    }
}
