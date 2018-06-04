package com.destrostudios.cards.shared.entities.templates;


import com.destrostudios.cards.shared.entities.EntityData;


public interface EntityTemplateLoader {
    void loadTemplate(EntityData entityData, int entity, String templateName, String[] parameters);
}
