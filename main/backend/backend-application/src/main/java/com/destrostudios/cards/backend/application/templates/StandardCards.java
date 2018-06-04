package com.destrostudios.cards.backend.application.templates;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.templates.EntityTemplate;
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
            data.setComponent(entity, Components.Ability.VIGILANCE);

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
            int entity = data.createEntity();
            EntityTemplate.loadTemplate(data, entity, "creatures/dragon");
            return entity;
        }

        public static int camel(EntityData data) {
            int entity = data.createEntity();
            EntityTemplate.loadTemplate(data, entity, "creatures/camel");
            return entity;
        }
    }

    public static class Green {

        public static int voyagingSatyr(EntityData data) {
            int entity = data.createEntity();
            EntityTemplate.loadTemplate(data, entity, "creatures/voyaging_satyr");
            return entity;
        }
    }
}
