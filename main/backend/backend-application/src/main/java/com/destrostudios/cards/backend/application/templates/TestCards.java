package com.destrostudios.cards.backend.application.templates;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

/**
 *
 * @author Philipp
 */
public class TestCards extends CardsUtil {

    public static int shyvana(EntityData data) {
        int card3 = creature(data, "Shyvana", 1, 1);
        data.setComponent(card3, Components.Color.BLUE);
        data.setComponent(card3, Components.Color.RED);
        data.setComponent(card3, Components.DAMAGED);
        data.setComponent(card3, Components.Tribe.HUMAN);
        data.setComponent(card3, Components.Tribe.DRAGON);
        data.setComponent(card3, Components.Ability.TAUNT);
        int playSpell3Cost = data.createEntity();
        data.setComponent(playSpell3Cost, Components.ManaAmount.RED, 1);
        data.setComponent(playSpell3Cost, Components.ManaAmount.BLUE, 1);
        int playSpell3 = summon(data, playSpell3Cost);

        int boardSpell3 = data.createEntity();
        int boardSpell3Cost = data.createEntity();
        data.setComponent(boardSpell3Cost, Components.Cost.TAP);
        data.setComponent(boardSpell3Cost, Components.ManaAmount.RED, 2);
        data.setComponent(boardSpell3, Components.Spell.COST_ENTITY, boardSpell3Cost);
        data.setComponent(card3, Components.SPELL_ENTITIES, new int[]{playSpell3, boardSpell3});
        data.setComponent(card3, Components.FLAVOUR_TEXT, "\"I am op.\"");
        return card3;
    }

    public static int aetherAdept(EntityData data) {
        int card4 = data.createEntity();
        data.setComponent(card4, Components.Color.NEUTRAL);
        data.setComponent(card4, Components.Color.WHITE);
        data.setComponent(card4, Components.Color.RED);
        data.setComponent(card4, Components.Color.GREEN);
        data.setComponent(card4, Components.Color.BLUE);
        data.setComponent(card4, Components.Color.BLACK);
        data.setComponent(card4, Components.CREATURE_CARD);
        data.setComponent(card4, Components.DISPLAY_NAME, "Aether Adept");
        data.setComponent(card4, Components.ATTACK, 1);
        data.setComponent(card4, Components.HEALTH, 1);
        data.setComponent(card4, Components.Tribe.GOD);
        data.setComponent(card4, Components.Ability.CHARGE);
        data.setComponent(card4, Components.Ability.DIVINE_SHIELD);
        data.setComponent(card4, Components.Ability.HEXPROOF);
        data.setComponent(card4, Components.Ability.IMMUNE);
        data.setComponent(card4, Components.Ability.TAUNT);
        int playSpell4 = data.createEntity();
        data.setComponent(playSpell4, Components.Spell.CastCondition.FROM_HAND);
        data.setComponent(playSpell4, Components.Spell.Effect.ADD_TO_BOARD);
        int playSpell4Cost = data.createEntity();
        data.setComponent(playSpell4Cost, Components.ManaAmount.NEUTRAL, 1);
        data.setComponent(playSpell4, Components.Spell.COST_ENTITY, playSpell4Cost);
        int boardSpell4 = data.createEntity();
        int boardSpell4Cost = data.createEntity();
        data.setComponent(boardSpell4Cost, Components.ManaAmount.BLUE, 1);
        data.setComponent(boardSpell4, Components.Spell.COST_ENTITY, boardSpell4Cost);
        data.setComponent(card4, Components.SPELL_ENTITIES, new int[]{playSpell4, boardSpell4});
        return card4;
    }
}
