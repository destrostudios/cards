package com.destrostudios.cards.backend.application.templates;

import com.destrostudios.cards.shared.entities.templates.EntityTemplate;

public class TestLibraries {

    public static CardPool custom() {
        CardPool cards = new CardPool();
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/goblin"), 1);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/orc"), 1);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/ogre"), 1);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/giant"), 1);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/dragon"), 3);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/camel"), 2);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/novice_engineer"), 2);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/voodoo_doctor"), 2);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/earthen_ring_farseer"), 1);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/argent_squire"), 1);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/scarlet_crusader"), 1);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/force_tank_max"), 1);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/elven_archer"), 1);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/fire_plume_phoenix"), 2);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/voidwalker"), 2);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/sunwalker"), 1);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/senjin_shieldmasta"), 1);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/sludge_belcher"), 1);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "spells/lightning_bolt"), 2);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "spells/arcane_intellect"), 2);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "spells/flamestrike"), 2);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "spells/hellfire"), 1);
        return cards;
    }

    public static CardPool random() {
        CardPool cards = new CardPool();
        cards.put(RandomCards::randomCard, 1);
        return cards;
    }
}
