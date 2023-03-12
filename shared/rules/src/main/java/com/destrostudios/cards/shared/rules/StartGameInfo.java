package com.destrostudios.cards.shared.rules;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter
public class StartGameInfo {
    private String boardName;
    private PlayerInfo player1;
    private PlayerInfo player2;
}
