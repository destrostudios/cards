package com.destrostudios.cards.shared.network.messages;

import com.destrostudios.cards.shared.model.*;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class InitialGameDataMessage {
    private List<Card> cards;
    private List<Power> powers;
    private List<Mode> modes;
    private List<Queue> queues;
    private User user;
}
