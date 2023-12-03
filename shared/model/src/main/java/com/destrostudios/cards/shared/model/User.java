package com.destrostudios.cards.shared.model;

import com.destrostudios.cards.shared.model.internal.BaseCardIdentifier;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class User {
    private int id;
    private String login;
    private boolean admin;
    private CardList collectionCardList;
    private int packs;
    private int packsOpened;
    private long arenaSeed;
    private List<BaseCardIdentifier> arenaDraftCards;
    private LocalDateTime firstLoginDate;
    private LocalDateTime lastLoginDate;
    private List<UserModeDeck> decks;
    private List<UserModeQueue> queues;
}
