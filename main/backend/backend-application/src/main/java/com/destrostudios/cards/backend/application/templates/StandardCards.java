package com.destrostudios.cards.backend.application.templates;

import static com.destrostudios.cards.backend.application.templates.CardsUtil.creature;
import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;

/**
 *
 * @author Philipp
 */
public class StandardCards extends CardsUtil {

    public static class Red {

        public static int goblin(EntityData data) {
            int entity = creature(data, "Goblin", 2, 1);
            data.setComponent(entity, Components.Color.RED);

            int summonCost = data.createEntity();
            data.setComponent(summonCost, Components.ManaAmount.RED, 1);
            int summon = summon(data, summonCost);

            data.setComponent(entity, Components.SPELL_ENTITIES, new int[]{summon});
            data.setComponent(entity, Components.FLAVOUR_TEXT, "\"Ouch.\"");
            return entity;
        }

        public static int orc(EntityData data) {
            int entity = creature(data, "Orc", 3, 2);
            data.setComponent(entity, Components.Color.RED);

            int summonCost = data.createEntity();
            data.setComponent(summonCost, Components.ManaAmount.RED, 1);
            data.setComponent(summonCost, Components.ManaAmount.NEUTRAL, 1);
            int summon = summon(data, summonCost);

            data.setComponent(entity, Components.SPELL_ENTITIES, new int[]{summon});
            data.setComponent(entity, Components.FLAVOUR_TEXT, "\"Hungry.\"");
            return entity;
        }

        public static int ogre(EntityData data) {
            int entity = creature(data, "Ogre", 4, 3);
            data.setComponent(entity, Components.Color.RED);

            int summonCost = data.createEntity();
            data.setComponent(summonCost, Components.ManaAmount.RED, 2);
            data.setComponent(summonCost, Components.ManaAmount.NEUTRAL, 1);
            int summon = summon(data, summonCost);

            data.setComponent(entity, Components.SPELL_ENTITIES, new int[]{summon});
            data.setComponent(entity, Components.FLAVOUR_TEXT, "\"Die!\"");
            return entity;
        }

        public static int giant(EntityData data) {
            int entity = creature(data, "Giant", 5, 4);
            data.setComponent(entity, Components.Color.RED);

            int summonCost = data.createEntity();
            data.setComponent(summonCost, Components.ManaAmount.RED, 2);
            data.setComponent(summonCost, Components.ManaAmount.NEUTRAL, 2);
            int summon = summon(data, summonCost);

            data.setComponent(entity, Components.SPELL_ENTITIES, new int[]{summon});
            data.setComponent(entity, Components.FLAVOUR_TEXT, "\"This hat is nice.\"");
            return entity;
        }

        public static int dragon(EntityData data) {
            int entity = creature(data, "Dragon", 6, 5);
            data.setComponent(entity, Components.Color.RED);
            data.setComponent(entity, Components.Tribe.DRAGON);

            int summonCost = data.createEntity();
            data.setComponent(summonCost, Components.ManaAmount.RED, 3);
            data.setComponent(summonCost, Components.ManaAmount.NEUTRAL, 2);
            int summon = summon(data, summonCost);

            data.setComponent(entity, Components.SPELL_ENTITIES, new int[]{summon});
            data.setComponent(entity, Components.FLAVOUR_TEXT, "\"Fragile creatures.\"");
            return entity;
        }
    }

    public static class Green {

        public static int satyr(EntityData data) {
            int entity = creature(data, "Voyaging Satyr", 1, 2);
            data.setComponent(entity, Components.Color.GREEN);

            int summonCost = data.createEntity();
            data.setComponent(summonCost, Components.ManaAmount.GREEN, 1);
            data.setComponent(summonCost, Components.ManaAmount.NEUTRAL, 1);
            int summon = summon(data, summonCost);

            int tapForMana = data.createEntity();
            boardActivated(data, tapForMana);
            int tapForManaCost = data.createEntity();
            data.setComponent(tapForManaCost, Components.Cost.TAP);
            data.setComponent(tapForManaCost, Components.ManaAmount.GREEN, -1);//Kappa
            data.setComponent(tapForMana, Components.Spell.COST_ENTITY, tapForManaCost);

            data.setComponent(entity, Components.SPELL_ENTITIES, new int[]{summon, tapForMana});
            data.setComponent(entity, Components.FLAVOUR_TEXT, "\"None can own the land's bounty. The gods made this world for all to share its riches. And I'm not just saying that because you caught me stealing your fruit.\"");
            return entity;
        }
    }
}
