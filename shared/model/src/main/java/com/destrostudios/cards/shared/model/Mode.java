package com.destrostudios.cards.shared.model;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class Mode {
    private int id;
    private String name;
    private String title;
    private boolean hasUserDecks;
    private List<ModeDeck> decks;
}
