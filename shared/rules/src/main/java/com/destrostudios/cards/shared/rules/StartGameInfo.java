package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.model.Mode;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@Setter
public class StartGameInfo {
    private Mode mode;
    private String boardName;
    private PlayerInfo player1;
    private PlayerInfo player2;
}
