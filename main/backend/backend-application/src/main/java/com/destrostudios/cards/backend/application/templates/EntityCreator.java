package com.destrostudios.cards.backend.application.templates;

import com.destrostudios.cards.shared.entities.EntityData;

/**
 *
 * @author Philipp
 */
public interface EntityCreator {
    int create(EntityData data);
}
