package com.destrostudios.cards.shared.rules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StartGameInfo {

    private String boardName;
    private PlayerInfo player1;
    private PlayerInfo player2;
}
