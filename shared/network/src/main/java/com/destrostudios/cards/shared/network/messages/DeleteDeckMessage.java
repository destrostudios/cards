package com.destrostudios.cards.shared.network.messages;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class DeleteDeckMessage {
    private int modeId;
    private int deckId;
}
