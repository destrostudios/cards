package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.model.CardList;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter
public class PlayerInfo {
    private int id;
    private String login;
    private CardList deck;
}
