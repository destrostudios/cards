package com.destrostudios.cards.shared.network.messages;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class QueueMessage {
    private int modeId;
    private int deckId;
    private int queueId;
}
