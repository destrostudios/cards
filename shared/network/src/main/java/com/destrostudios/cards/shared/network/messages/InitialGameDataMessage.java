package com.destrostudios.cards.shared.network.messages;

import com.destrostudios.cards.shared.model.Card;
import com.destrostudios.cards.shared.model.Mode;
import com.destrostudios.cards.shared.model.User;
import com.destrostudios.cards.shared.model.UserCardList;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class InitialGameDataMessage {
    private List<Mode> modes;
    private List<Card> cards;
    private User user;
    private List<UserCardList> userCardLists;
}
