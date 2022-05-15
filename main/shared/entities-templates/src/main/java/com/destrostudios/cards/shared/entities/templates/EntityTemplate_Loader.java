package com.destrostudios.cards.shared.entities.templates;

import com.destrostudios.cards.shared.entities.EntityData;

public interface EntityTemplate_Loader {

    void loadTemplate(EntityData entityData, int entity, EntityTemplate template);
}
