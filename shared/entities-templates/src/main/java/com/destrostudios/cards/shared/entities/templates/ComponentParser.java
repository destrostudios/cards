package com.destrostudios.cards.shared.entities.templates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ComponentParser<NODE, RECORDED_VALUE, ACTUAL_VALUE> {

    public abstract RECORDED_VALUE parse(TemplateParser<NODE> parser, TemplateFormat<NODE> format, NODE node);

    public abstract ACTUAL_VALUE resolve(int[] proxiedEntities, RECORDED_VALUE recordedValue);

    protected Object[] parseOrCreateChildEntities(TemplateParser<NODE> parser, TemplateFormat<NODE> format, NODE node, String attributeName) {
        ArrayList<Object> childEntities = new ArrayList<>();
        List<NODE> children = format.getChildren(node);
        if (children.size() > 0) {
            for (NODE child : children) {
                childEntities.add(parser.createAndLoadEntity(child));
            }
        } else if (format.getText(node).length() > 0) {
            String[] textParts = format.getText(node).split(",");
            for (String textPart : textParts) {
                childEntities.add(parser.parseEntity(textPart));
            }
        }
        int parameterIndex = 0;
        String attributeValue;
        while ((attributeValue = format.getAttribute(node, attributeName + parameterIndex)) != null) {
            childEntities.add(parser.parseEntity(attributeValue));
            parameterIndex++;
        }
        return childEntities.toArray();
    }

    protected Object parseOrCreateChildEntity(TemplateParser<NODE> parser, TemplateFormat<NODE> format, NODE node, int index, String attributeName) {
        List<NODE> children = format.getChildren(node);
        if (children.size() > 0) {
            if (index < children.size()) {
                return parser.createAndLoadEntity(children.get(index));
            }
        } else if ((index == 0) && (format.getText(node).length() > 0)) {
            return parser.parseEntity(format.getText(node));
        }
        return parser.parseEntity(format.getAttribute(node, attributeName));
    }

    protected int[] resolveEntities(int[] proxiedEntities, Object[] recordedEntities) {
        int[] entities = new int[recordedEntities.length];
        for (int i = 0; i < entities.length; i++) {
            entities[i] = resolveEntity(proxiedEntities, recordedEntities[i]);
        }
        return entities;
    }

    protected int resolveEntity(int[] proxiedEntities, Object recordedEntity) {
        return (int) resolveValue(proxiedEntities, recordedEntity);
    }

    protected Template[] resolveTemplates(int[] proxiedEntities, Template[] recordedTemplates) {
        Template[] templates = new Template[recordedTemplates.length];
        for (int i = 0; i < templates.length; i++) {
            templates[i] = resolveTemplate(proxiedEntities, recordedTemplates[i]);
        }
        return templates;
    }

    protected Template resolveTemplate(int[] proxiedEntities, Template recordedTemplate) {
        Map<String, Object> input = resolveTemplateInOrOutputs(proxiedEntities, recordedTemplate.getInput());
        Map<String, Object> output = resolveTemplateInOrOutputs(proxiedEntities, recordedTemplate.getOutput());
        return new Template(recordedTemplate.getName(), input, output);
    }

    private Map<String, Object> resolveTemplateInOrOutputs(int[] proxiedEntities, Map<String, Object> recordedInOrOutputs) {
        HashMap<String, Object> inOrOutputs = new HashMap<>();
        recordedInOrOutputs.forEach((key, recordedValue) -> {
            inOrOutputs.put(key, resolveValue(proxiedEntities, recordedValue));
        });
        return inOrOutputs;
    }

    protected Object resolveValue(int[] proxiedEntities, Object recordedValue) {
        if (recordedValue instanceof EntityProxy entityProxy) {
            return proxiedEntities[entityProxy.entity()];
        }
        return recordedValue;
    }
}
