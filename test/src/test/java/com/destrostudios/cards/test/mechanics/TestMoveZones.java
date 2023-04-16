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
        int creature = createCreature(player, Components.Zone.HAND);
        fire(new MoveToCreatureZoneEvent(creature));
        assertHasNoComponent(creature, Components.Zone.LIBRARY);
        assertHasNoComponent(creature, Components.Zone.HAND);
        assertHasComponent(creature, Components.BOARD);
        assertHasComponent(creature, Components.Zone.CREATURE_ZONE);
        assertHasNoComponent(creature, Components.Zone.GRAVEYARD);
    }

    @Test
    public void testBoardToGraveyard() {
        int creature = createCreature(player, Components.Zone.CREATURE_ZONE);
        fire(new MoveToGraveyardEvent(creature));
        assertHasNoComponent(creature, Components.Zone.LIBRARY);
        assertHasNoComponent(creature, Components.Zone.HAND);
        assertHasNoComponent(creature, Components.BOARD);
        assertHasNoComponent(creature, Components.Zone.CREATURE_ZONE);
        assertHasComponent(creature, Components.Zone.GRAVEYARD);
    }

    @Test
    public void testGraveyardToHand() {
        int creature = createCreature(player, Components.Zone.GRAVEYARD);
        fire(new MoveToHandEvent(creature));
        assertHasNoComponent(creature, Components.Zone.LIBRARY);
        assertHasComponent(creature, Components.Zone.HAND);
        assertHasNoComponent(creature, Components.BOARD);
        assertHasNoComponent(creature, Components.Zone.CREATURE_ZONE);
        assertHasNoComponent(creature, Components.Zone.GRAVEYARD);
    }
}
