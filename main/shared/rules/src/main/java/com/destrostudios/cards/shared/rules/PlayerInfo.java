package com.destrostudios.cards.shared.rules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlayerInfo {
    private long id;
    private String login;
    private String deckName;
}
