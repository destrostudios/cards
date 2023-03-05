package com.destrostudios.cards.shared.model;

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
}
