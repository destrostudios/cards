package com.destrostudios.cards.shared.network.messages;

import com.destrostudios.cards.shared.model.changes.NewCardListCard;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class UpdateCardListMessage {
    private int userCardListId;
    private String name;
    private List<NewCardListCard> cards;
}
