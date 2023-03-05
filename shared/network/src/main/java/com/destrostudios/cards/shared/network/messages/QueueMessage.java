package com.destrostudios.cards.shared.network.messages;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class QueueMessage {
    private boolean againstHumanOrBot;
    private List<String> libraryTemplates;
}
