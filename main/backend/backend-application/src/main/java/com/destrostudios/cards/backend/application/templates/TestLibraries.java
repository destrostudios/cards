package com.destrostudios.cards.backend.application.templates;

import com.destrostudios.cards.shared.entities.templates.EntityTemplate;

/**
 *
 * @author Philipp
 */
public class TestLibraries {

    public static CardPool red() {
        CardPool cards = new CardPool();
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/goblin"), 5);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/orc"), 4);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/ogre"), 3);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/giant"), 2);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/dragon"), 1);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "creatures/camel"), 3);
        cards.put((data) -> EntityTemplate.createFromTemplate(data, "spells/lightning_bolt"), 5);
        return cards;
    }
}
