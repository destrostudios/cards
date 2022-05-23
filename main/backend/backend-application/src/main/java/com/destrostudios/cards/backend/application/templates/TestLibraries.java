package com.destrostudios.cards.backend.application.templates;

import com.destrostudios.cards.shared.entities.templates.EntityTemplate;

/**
 *
 * @author Philipp
 */
public class TestLibraries {

    public static CardPool red() {
        CardPool cards = new CardPool();
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/goblin"), 2);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/orc"), 1);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/ogre"), 1);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/giant"), 1);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/dragon"), 3);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/camel"), 2);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/novice_engineer"), 2);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/voodoo_doctor"), 2);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/earthen_ring_farseer"), 2);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "spells/lightning_bolt"), 3);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "spells/arcane_intellect"), 2);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "spells/flamestrike"), 2);
        return cards;
    }
}
