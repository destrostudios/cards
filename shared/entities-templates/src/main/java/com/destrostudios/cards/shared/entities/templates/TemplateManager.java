package com.destrostudios.cards.shared.entities.templates;

import com.destrostudios.cards.shared.entities.EntityData;

import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class TemplateManager {

    public TemplateManager(TemplateReader reader, TemplateFormat format) {
        this.reader = reader;
        this.format = format;
    }
    private TemplateReader reader;
    private TemplateFormat format;
    private HashMap<String, ComponentParser> componentParsers = new HashMap<>();
    // Has to be concurrent for now as the EntityTemplate wrapper is static and multiple bot threads can load templates in parallel
    private ConcurrentHashMap<String, Object> cachedRoots = new ConcurrentHashMap<>();

    public <T> void registerComponent(ComponentParser componentParser){
        componentParsers.put(componentParser.getElementName(), componentParser);
    }

    public void loadTemplate(EntityData entityData, int entity, EntityTemplate entityTemplate) {
        Object root = cachedRoots.computeIfAbsent(entityTemplate.getName(), templateName -> {
            InputStream inputStream = reader.read(templateName);
            return format.readRoot(inputStream);
        });
        if (root != null) {
            TemplateParser parser = new TemplateParser<>(this, format);
            parser.loadTemplate(entityData, entity, entityTemplate, root);
        }
    }

    public ComponentParser getComponentParser(Object node){
        ComponentParser componentParser = componentParsers.get(format.getName(node));
        if (componentParser != null) {
            return componentParser;
        }
        throw new RuntimeException("Unregistered component '" + format.getName(node) + "'");
    }
}
