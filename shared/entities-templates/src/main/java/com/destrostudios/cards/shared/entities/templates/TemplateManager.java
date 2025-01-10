package com.destrostudios.cards.shared.entities.templates;

import com.destrostudios.cards.shared.entities.IntMap;

import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class TemplateManager implements TemplateLoader {

    public TemplateManager(TemplateReader reader, TemplateFormat format, ComponentInfo componentInfo) {
        this.reader = reader;
        this.format = format;
        this.componentInfo = componentInfo;
    }
    private TemplateReader reader;
    private TemplateFormat format;
    private ComponentInfo componentInfo;
    private HashMap<String, Object> componentsByName = new HashMap<>();
    private IntMap<ComponentParser> componentParsers = new IntMap<>();
    // Has to be concurrent for now as the EntityTemplate wrapper is static and multiple bot threads can load templates in parallel
    private ConcurrentHashMap<String, Object> cachedRoots = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, TemplateRecording> recordings = new ConcurrentHashMap<>();

    public void registerComponent(Object component, ComponentParser componentParser) {
        componentsByName.put(componentInfo.getName(component), component);
        componentParsers.set(componentInfo.getId(component), componentParser);
    }

    @Override
    public void loadTemplate(EntityFramework<?> framework, int entity, Template template) {
        Object root = cachedRoots.computeIfAbsent(template.getName(), templateName -> {
            InputStream inputStream = reader.read(templateName);
            return format.readRoot(inputStream);
        });
        if (root != null) {
            if (framework instanceof TemplateRecording templateRecording) {
                TemplateParser parser = new TemplateParser(this, format, templateRecording);
                parser.loadTemplate(new EntityProxy(entity), template, root);
            } else {
                TemplateRecording recording = recordings.computeIfAbsent(template.getAsResolvedText(), t -> {
                    TemplateRecording newRecording = new TemplateRecording(this);
                    TemplateParser parser = new TemplateParser(this, format, newRecording);
                    parser.loadTemplate(new EntityProxy(0), template, root);
                    return newRecording;
                });
                recording.run(framework, entity);
            }
        }
    }

    public Object getComponent(String componentName) {
        Object component = componentsByName.get(componentName);
        if (component == null) {
            throw new RuntimeException("Unregistered component '" + componentName + "'.");
        }
        return component;
    }

    public ComponentParser getComponentParser(Object component) {
        return componentParsers.get(componentInfo.getId(component));
    }
}
