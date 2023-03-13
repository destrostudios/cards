package com.destrostudios.cards.shared.network.messages;

import com.destrostudios.cards.shared.model.User;
import com.destrostudios.cards.shared.model.internal.PackResult;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class PackResultMessage {
    private PackResult packResult;
    private User user;
}
