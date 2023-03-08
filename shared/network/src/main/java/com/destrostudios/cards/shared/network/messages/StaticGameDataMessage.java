package com.destrostudios.cards.shared.network.messages;

import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.User;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class StaticGameDataMessage {
    private List<Mode> modes;
    private List<Card> cards;
    private User ownUser;
}
