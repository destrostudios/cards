package com.destrostudios.cards.shared.rules.util;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.entities.SimpleEntityData;
import com.destrostudios.cards.shared.entities.templates.EntityTemplate;
import com.destrostudios.cards.shared.rules.Cards;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.GameConstants;
import com.destrostudios.cards.shared.rules.effects.DiscoverPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class DiscoverUtil {

    private static HashMap<DiscoverPool, ArrayList<String>> POOL_TEMPLATES = new HashMap<>();

    public static void setup() {
        EntityData data = new SimpleEntityData(Components.ALL);
        for (String template : Cards.COLLECTIBLE) {
            int card = data.createEntity();
            EntityTemplate.loadTemplate(data, card, template);

            Integer manaCost = null;
            Integer defaultCastFromHandSpell = SpellUtil.getDefaultCastFromHandSpell(data, card);
            if (defaultCastFromHandSpell != null) {
                manaCost = CostUtil.getEffectiveManaCost(data, defaultCastFromHandSpell);
            }

            if (data.hasComponent(card, Components.CREATURE_CARD)) {
                addToPool(template, DiscoverPool.CREATURE);
                if (data.hasComponent(card, Components.LEGENDARY)) {
                    addToPool(template, DiscoverPool.LEGENDARY_CREATURE);
                }
                if (data.hasComponent(card, Components.Ability.TAUNT)) {
                    addToPool(template, DiscoverPool.TAUNT);
                }
                if (data.hasComponent(card, Components.Tribe.BEAST)) {
                    addToPool(template, DiscoverPool.BEAST);
                }
                if (data.hasComponent(card, Components.Tribe.DRAGON)) {
                    addToPool(template, DiscoverPool.DRAGON);
                }
                if (data.hasComponent(card, Components.Tribe.GOBLIN)) {
                    addToPool(template, DiscoverPool.GOBLIN);
                    if ((manaCost != null) && (manaCost <= 2)) {
                        addToPool(template, DiscoverPool.GOBLIN_THAT_COSTS_2_OR_LESS);
                    }
                }
                if (data.hasComponent(card, Components.Tribe.MACHINE)) {
                    addToPool(template, DiscoverPool.MACHINE);
                }
                if (manaCost != null) {
                    if (manaCost == 2) {
                        addToPool(template, DiscoverPool.CREATURE_THAT_COSTS_2);
                    } else if (manaCost >= 8) {
                        addToPool(template, DiscoverPool.CREATURE_THAT_COSTS_8_OR_MORE);
                    }
                }
            }
            if (data.hasComponent(card, Components.SPELL_CARD)) {
                addToPool(template, DiscoverPool.SPELL);
                if ((manaCost != null) && (manaCost == 1)) {
                    addToPool(template, DiscoverPool.SPELL_THAT_COSTS_1);
                }
            }
        }
        POOL_TEMPLATES.forEach((discoverPool, templates) -> {
            if (templates.size() < GameConstants.DISCOVER_OPTIONS) {
                throw new RuntimeException("Discover pool for " + discoverPool + " too small.");
            }
        });
    }

    private static void addToPool(String template, DiscoverPool discoverPool) {
        POOL_TEMPLATES.computeIfAbsent(discoverPool, _ -> new ArrayList<>()).add(template);
    }

    public static String[] getRandomTemplates(DiscoverPool pool, Random random) {
        ArrayList<String> allTemplates = getTemplates(pool);
        ArrayList<Integer> randomTemplatesIndices = new ArrayList<>(GameConstants.DISCOVER_OPTIONS);
        while (randomTemplatesIndices.size() < GameConstants.DISCOVER_OPTIONS) {
            int templateIndex = random.nextInt(allTemplates.size());
            if (!randomTemplatesIndices.contains(templateIndex)) {
                randomTemplatesIndices.add(templateIndex);
            }
        }

        String[] randomTemplates = new String[GameConstants.DISCOVER_OPTIONS];
        for (int i = 0; i < randomTemplates.length; i++) {
            randomTemplates[i] = allTemplates.get(randomTemplatesIndices.get(i));
        }
        return randomTemplates;
    }

    private static ArrayList<String> getTemplates(DiscoverPool pool) {
        return POOL_TEMPLATES.get(pool);
    }
}
