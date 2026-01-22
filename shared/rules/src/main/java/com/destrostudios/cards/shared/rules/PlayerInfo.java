package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.model.CardList;
import com.destrostudios.cards.shared.model.Power;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class PlayerInfo {
    private int id;
    private String login;
    private Power[] powers;
    private CardList deck;
}
