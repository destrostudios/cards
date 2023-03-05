package com.destrostudios.cards.shared.network.messages;

import com.destrostudios.cards.shared.model.UserCardList;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class OwnUserCardListsMessage {
    private List<UserCardList> userCardLists;
}
