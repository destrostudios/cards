package com.destrostudios.cards.backend.application.templates;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

/**
 *
 * @author Philipp
 */
public class TestCards extends CardsUtil {

    public static int shyvana(EntityData data) {
        int card = creature(data, "Shyvana", 1, 1);
        data.setComponent(card, Components.DAMAGED);
        data.setComponent(card, Components.Tribe.HUMAN);
        data.setComponent(card, Components.Tribe.DRAGON);
        data.setComponent(card, Components.Ability.TAUNT);

        int playSpellCost = data.createEntity();
        data.setComponent(playSpellCost, Components.MANA, 2);
        int playSpell = summon(data, playSpellCost);

        int boardSpell = data.createEntity();
        int boardSpellCost = data.createEntity();
        data.setComponent(boardSpellCost, Components.MANA, 2);
        data.setComponent(boardSpell, Components.Spell.COST_ENTITY, boardSpellCost);

        data.setComponent(card, Components.SPELL_ENTITIES, new int[]{playSpell, boardSpell});
        data.setComponent(card, Components.FLAVOUR_TEXT, "\"I am op.\"");
        return card;
    }

    public static int aetherAdept(EntityData data) {
        int card = data.createEntity();
        data.setComponent(card, Components.CREATURE_CARD);
        data.setComponent(card, Components.NAME, "Aether Adept");
        data.setComponent(card, Components.ATTACK, 1);
        data.setComponent(card, Components.HEALTH, 1);
        data.setComponent(card, Components.Tribe.GOD);
        data.setComponent(card, Components.Ability.SLOW);
        data.setComponent(card, Components.Ability.DIVINE_SHIELD);
        data.setComponent(card, Components.Ability.HEXPROOF);
        data.setComponent(card, Components.Ability.IMMUNE);
        data.setComponent(card, Components.Ability.TAUNT);

        int playSpellCost = data.createEntity();
        data.setComponent(playSpellCost, Components.MANA, 1);
        int playSpell = summon(data, playSpellCost);

        int boardSpell = data.createEntity();
        int boardSpellCost = data.createEntity();
        data.setComponent(boardSpellCost, Components.MANA, 1);
        data.setComponent(boardSpell, Components.Spell.COST_ENTITY, boardSpellCost);

        data.setComponent(card, Components.SPELL_ENTITIES, new int[] { playSpell, boardSpell });
        return card;
    }
}
