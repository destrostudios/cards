package com.destrostudios.cards.shared.rules.creatures.specific;

import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TestGame;
import org.junit.jupiter.api.Test;

public class TestMermaid extends TestGame {

    @Test
    public void testReduceMermaidsManaCostWhenOnBoard() {
        int handMermaid = create("creatures/mermaid", player, Components.HAND);
        create("creatures/mermaid", player, Components.CREATURE_ZONE);
        assertManaCost(handMermaid, 1);
    }
}
