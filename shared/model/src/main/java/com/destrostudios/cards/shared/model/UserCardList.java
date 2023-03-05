package com.destrostudios.cards.shared.model;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class UserCardList {
    private int id;
    private Mode mode;
    private boolean library;
    private CardList cardList;
}
