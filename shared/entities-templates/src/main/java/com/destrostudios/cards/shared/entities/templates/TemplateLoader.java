package com.destrostudios.cards.shared.entities.templates;

public interface TemplateLoader {

    void loadTemplate(EntityFramework<?> framework, int entity, Template template);
}
