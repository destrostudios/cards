package com.destrostudios.cards.shared.network.messages;

import com.destrostudios.cards.shared.model.internal.NewCardListCard;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class UpdateDeckMessage {
    private int modeId;
    private int deckId;
    private String name;
    private List<NewCardListCard> cards;
}
