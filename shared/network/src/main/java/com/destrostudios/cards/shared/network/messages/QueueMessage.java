package com.destrostudios.cards.shared.network.messages;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class QueueMessage {
    private boolean againstHumanOrBot;
    private int userCardListId;
}
