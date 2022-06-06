package com.destrostudios.cards.shared.network.modules;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class QueueMessage {
    private boolean againstHumanOrBot;
    private List<String> libraryTemplates;
}
