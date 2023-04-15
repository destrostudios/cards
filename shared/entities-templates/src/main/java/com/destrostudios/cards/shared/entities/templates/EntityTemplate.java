package com.destrostudios.cards.shared.entities.templates;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;

import java.util.ArrayList;

public class EntityTemplate {

    private static ArrayList<TemplateLoader> loaders = new ArrayList<>();

    public static void addLoader(TemplateLoader loader) {
        loaders.add(loader);
    }

    public static int createFromTemplate(EntityFramework<?> framework, String... templateNames) {
        int entity = framework.createEntity();
        loadTemplates(framework, entity, templateNames);
        return entity;
    }

    public static void loadTemplates(EntityFramework<?> framework, int entity, String... templateNames) {
        for (String templateName : templateNames) {
            loadTemplate(framework, entity, templateName);
        }
    }

    public static void loadTemplate(EntityFramework<?> framework, int entity, String template) {
        loadTemplate(framework, entity, Template.parse(template));
    }

    public static void loadTemplate(EntityFramework<?> framework, int entity, Template template) {
        for (TemplateLoader loader : loaders) {
            loader.loadTemplate(framework, entity, template);
        }
    }

    // TODO: Refactor so the application can use the generic methods above
    public static void loadTemplate(EntityData data, int entity, String template) {
        loadTemplate(new EntityFramework<ComponentDefinition>() {

            @Override
            public int createEntity() {
                return data.createEntity();
            }

            @Override
            public <T> void setComponent(int entity, ComponentDefinition component, T value) {
                data.setComponent(entity, component, value);
            }
        }, entity, Template.parse(template));
    }
}
