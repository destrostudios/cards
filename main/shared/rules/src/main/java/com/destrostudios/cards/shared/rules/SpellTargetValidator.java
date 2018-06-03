package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class SpellTargetValidator {

    private static final List<ComponentDefinition> simpleRequirableComponents = new LinkedList<>();
    static {
        simpleRequirableComponents.add(Components.ATTACK);
        simpleRequirableComponents.add(Components.BOARD);
        simpleRequirableComponents.add(Components.CREATURE_CARD);
        simpleRequirableComponents.add(Components.CREATURE_ZONE);
        simpleRequirableComponents.add(Components.DAMAGED);
        simpleRequirableComponents.add(Components.DISPLAY_NAME);
        simpleRequirableComponents.add(Components.ENCHANTMENT_CARD);
        simpleRequirableComponents.add(Components.ENCHANTMENT_ZONE);
        simpleRequirableComponents.add(Components.FLAVOUR_TEXT);
        simpleRequirableComponents.add(Components.GRAVEYARD);
        simpleRequirableComponents.add(Components.HAND_CARDS);
        simpleRequirableComponents.add(Components.HEALTH);
        simpleRequirableComponents.add(Components.LAND_CARD);
        simpleRequirableComponents.add(Components.LAND_ZONE);
        simpleRequirableComponents.add(Components.LIBRARY);
        simpleRequirableComponents.add(Components.OWNED_BY);
        simpleRequirableComponents.add(Components.SPELL_CARD);
        simpleRequirableComponents.add(Components.SPELL_ZONE);
        simpleRequirableComponents.add(Components.TAPPED);
        simpleRequirableComponents.add(Components.Ability.CHARGE);
        simpleRequirableComponents.add(Components.Ability.DIVINE_SHIELD);
        simpleRequirableComponents.add(Components.Ability.FLYING);
        simpleRequirableComponents.add(Components.Ability.HEXPROOF);
        simpleRequirableComponents.add(Components.Ability.IMMUNE);
        simpleRequirableComponents.add(Components.Ability.TAUNT);
        simpleRequirableComponents.add(Components.Color.NEUTRAL);
        simpleRequirableComponents.add(Components.Color.WHITE);
        simpleRequirableComponents.add(Components.Color.RED);
        simpleRequirableComponents.add(Components.Color.GREEN);
        simpleRequirableComponents.add(Components.Color.BLUE);
        simpleRequirableComponents.add(Components.Color.BLACK);
        simpleRequirableComponents.add(Components.Tribe.BEAST);
        simpleRequirableComponents.add(Components.Tribe.DRAGON);
        simpleRequirableComponents.add(Components.Tribe.FISH);
        simpleRequirableComponents.add(Components.Tribe.GOD);
        simpleRequirableComponents.add(Components.Tribe.HUMAN);
    }

    public static boolean isValidTarget(EntityData entityData, int targetRuleEntity, int targetEntity) {
        for (ComponentDefinition componentDefinition : simpleRequirableComponents) {
            // Check has instead of (get != null) because of Void type
            if (entityData.hasComponent(targetRuleEntity, componentDefinition)) {
                Object requiredComponent = entityData.getComponent(targetRuleEntity, componentDefinition);
                if (entityData.hasComponent(targetEntity, componentDefinition)) {
                    Object targetComponent = entityData.getComponent(targetEntity, componentDefinition);
                    if (!Objects.equals(requiredComponent, targetComponent)) {
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
        }
        return true;
    }
}
