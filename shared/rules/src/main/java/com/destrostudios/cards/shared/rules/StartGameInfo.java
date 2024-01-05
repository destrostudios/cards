package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.Queue;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class StartGameInfo {
    private Mode mode;
    private Queue queue;
    private String boardName;
    private PlayerInfo[] players;
}
