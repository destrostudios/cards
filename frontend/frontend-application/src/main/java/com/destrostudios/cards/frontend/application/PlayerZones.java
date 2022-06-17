package com.destrostudios.cards.frontend.application;

import com.destrostudios.cardgui.CardZone;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlayerZones {
    private CardZone deckZone;
    private CardZone handZone;
    private CardZone creatureZone;
    private CardZone graveyardZone;
}
