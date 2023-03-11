package com.destrostudios.cards.shared.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class CardList {
    private int id;
    private String name;
    private List<CardListCard> cards;

    @JsonIgnore
    public int getSize() {
        int size = 0;
        for (CardListCard card : cards) {
            size += card.getAmount();
        }
        return size;
    }
}
