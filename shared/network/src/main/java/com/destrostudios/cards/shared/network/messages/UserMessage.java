package com.destrostudios.cards.shared.network.messages;

import com.destrostudios.cards.shared.model.User;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class UserMessage {
    private User user;
}
