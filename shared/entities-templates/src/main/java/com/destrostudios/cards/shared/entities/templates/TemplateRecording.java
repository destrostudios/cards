package com.destrostudios.cards.shared.entities.templates;

import java.util.ArrayList;

public class TemplateRecording<COMPONENT> implements EntityFramework<COMPONENT> {

    public TemplateRecording(TemplateManager templateManager) {
        this.templateManager = templateManager;
        recordedEntities.add(new ArrayList<>());
    }
    private TemplateManager templateManager;
    private ArrayList<ArrayList<ComponentProxy<?>>> recordedEntities = new ArrayList<>();

    @Override
    public int createEntity() {
        int entity = recordedEntities.size();
        recordedEntities.add(new ArrayList<>());
        return entity;
    }

    @Override
    public <T> void setComponent(int entity, COMPONENT component, T value) {
        recordedEntities.get(entity).add(new ComponentProxy<>(component, value));
    }

    public void run(EntityFramework framework, int entity) {
        int[] proxiedEntities = new int[recordedEntities.size()];
        proxiedEntities[0] = entity;
        for (int i = 1; i < proxiedEntities.length; i++) {
            proxiedEntities[i] = framework.createEntity();
        }
        for (int i = 0; i < proxiedEntities.length; i++) {
            ArrayList<ComponentProxy<?>> entityComponents = this.recordedEntities.get(i);
            for (ComponentProxy<?> componentProxy : entityComponents) {
                Object component = componentProxy.component();
                Object recordedValue = componentProxy.recordedValue();
                ComponentParser componentParser = templateManager.getComponentParser(component);
                Object actualValue = componentParser.resolve(proxiedEntities, recordedValue);
                if (actualValue instanceof String actualValueString) {
                    actualValue = EntityProxy.resolveEntitiesInText(proxiedEntities, actualValueString);
                }
                framework.setComponent(proxiedEntities[i], component, actualValue);
            }
        }
    }
}
