package com.destrostudios.cards.shared.entities.templates;

import com.destrostudios.cards.shared.entities.EntityData;

import java.util.ArrayList;

/**
 *
 * @author Carl
 */
public class EntityTemplate{

    private static ArrayList<EntityTemplateLoader> loaders = new ArrayList<>();

    public static void addLoader(EntityTemplateLoader loader) {
        loaders.add(loader);
    }

    public static void loadTemplate(EntityData entityData, int entity, String templateName) {
        loadTemplate(entityData, entity, templateName, new String[0]);
    }

    public static void loadTemplate(EntityData entityData, int entity, String templateName, String[] parameters) {
        for (EntityTemplateLoader loader : loaders) {
            loader.loadTemplate(entityData, entity, templateName, parameters);
        }
    }
}
