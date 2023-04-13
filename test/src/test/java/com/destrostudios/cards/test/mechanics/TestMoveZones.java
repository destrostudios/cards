package com.destrostudios.cards.test.mechanics;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToCreatureZoneEvent;
import com.destrostudios.cards.test.TestGame;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToGraveyardEvent;
import com.destrostudios.cards.shared.rules.cards.zones.MoveToHandEvent;
import org.junit.jupiter.api.Test;

public class TestMoveZones extends TestGame {

    @Test
    public void testHandToCreatureZone() {
        int creature = createCreature(player, Components.HAND);
        fire(new MoveToCreatureZoneEvent(creature));
        assertHasNoComponent(creature, Components.LIBRARY);
        assertHasNoComponent(creature, Components.HAND);
        assertHasComponent(creature, Components.BOARD);
        assertHasComponent(creature, Components.CREATURE_ZONE);
        assertHasNoComponent(creature, Components.GRAVEYARD);
    }

    @Test
    public void testBoardToGraveyard() {
        int creature = createCreature(player, Components.CREATURE_ZONE);
        fire(new MoveToGraveyardEvent(creature));
        assertHasNoComponent(creature, Components.LIBRARY);
        assertHasNoComponent(creature, Components.HAND);
        assertHasNoComponent(creature, Components.BOARD);
        assertHasNoComponent(creature, Components.CREATURE_ZONE);
        assertHasComponent(creature, Components.GRAVEYARD);
    }

    @Test
    public void testGraveyardToHand() {
        int creature = createCreature(player, Components.GRAVEYARD);
        fire(new MoveToHandEvent(creature));
        assertHasNoComponent(creature, Components.LIBRARY);
        assertHasComponent(creature, Components.HAND);
        assertHasNoComponent(creature, Components.BOARD);
        assertHasNoComponent(creature, Components.CREATURE_ZONE);
        assertHasNoComponent(creature, Components.GRAVEYARD);
    }
}
