package com.destrostudios.cards.shared.network.messages;

import com.destrostudios.cards.shared.model.User;
import com.destrostudios.cards.shared.model.UserCardList;
import com.destrostudios.cards.shared.model.internal.PackResult;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class PackResultMessage {
    private PackResult packResult;
    private User user;
    private List<UserCardList> userCardLists;
}
