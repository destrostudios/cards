package com.destrostudios.cards.shared.entities.templates;

import com.destrostudios.cards.shared.entities.EntityData;

import java.io.InputStream;
import java.util.HashMap;

public class TemplateManager {

    public TemplateManager(TemplateReader reader, TemplateFormat format) {
        this.reader = reader;
        this.format = format;
    }
    private TemplateReader reader;
    private TemplateFormat format;
    private HashMap<String, ComponentParser> componentParsers = new HashMap<>();
    private HashMap<String, Object> cachedRoots = new HashMap<>();

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
        ComponentParser xmlComponentParser = componentParsers.get(format.getName(node));
        if (xmlComponentParser != null) {
            return xmlComponentParser;
        }
        throw new RuntimeException("Unregistered component '" + format.getName(node) + "'");
    }
}
