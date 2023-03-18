package com.destrostudios.cards.shared.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class UserMode {
    private int id;
    private int userId;
    private Mode mode;
    private CardList collectionCardList;
    private int packs;
    private int packsOpened;
    private List<UserModeDeck> decks;
    private List<UserModeQueue> queues;
}
