package com.destrostudios.cards.shared.entities.templates;

import com.destrostudios.cards.shared.entities.EntityData;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Stack;

public class TemplateParser<NODE> {

    public TemplateParser(TemplateManager templateManager, TemplateFormat<NODE> format) {
        this.templateManager = templateManager;
        this.format = format;
    }
    private TemplateManager templateManager;
    private TemplateFormat<NODE> format;
    private Stack<String> currentDirectories = new Stack<>();
    private Stack<HashMap<String, Integer>> cachedEntities = new Stack<>();
    private Stack<HashMap<String, String>> cachedValues = new Stack<>();

    public void loadTemplate(EntityData entityData, int entity, EntityTemplate template, NODE root) {
        String currentDirectory = "";
        String[] directories = template.getName().split("/");
        for (int i = 0; i < (directories.length - 1); i++) {
            currentDirectory += directories[i] + "/";
        }
        currentDirectories.push(currentDirectory);
        HashMap<String, Integer> entities = new HashMap<>(10);
        cachedEntities.push(entities);
        HashMap<String, String> values = new HashMap<>();
        NODE valuesNode = format.getChild(root, "values");
        if (valuesNode != null) {
            for (NODE valueNode : format.getChildren(valuesNode)) {
                values.put(format.getName(valueNode), format.getText(valueNode));
                // Save the unmodified default value so it can be exported and accessed by parent
                values.put("_" + format.getName(valueNode), format.getText(valueNode));
            }
        }
        values.putAll(template.getInput());
        cachedValues.push(values);
        boolean isFirstEntity = true;
        for (NODE entityNode : format.getChildren(root, "entity")) {
            if (isFirstEntity) {
                String id = format.getAttribute(entityNode, "id");
                if (id != null) {
                    cachedEntities.lastElement().put(id, entity);
                }
                loadEntity(entityData, entity, entityNode);
            } else {
                createAndLoadEntity(entityData, entityNode);
            }
            isFirstEntity = false;
        }
        // Export
        if (cachedValues.size() > 1) {
            HashMap<String, String> parentTemplateValues = cachedValues.get(cachedValues.size() - 2);
            for (Entry<String, String> output : template.getOutput().entrySet()) {
                String name = output.getKey();
                String value = parseValue(entityData, output.getValue());
                parentTemplateValues.put(name, value);
            }
        }
        currentDirectories.pop();
        cachedEntities.pop();
        cachedValues.pop();
    }

    public int createAndLoadEntity(EntityData entityData, NODE entityNode) {
        if ((!isNodeEnabled(entityData, entityNode)) || format.getName(entityNode).equals("empty")) {
            return -1;
        }
        Integer entity = null;
        String id = format.getAttribute(entityNode, "id");
        if (id != null) {
            entity = cachedEntities.lastElement().get(id);
        }
        if (entity == null) {
            entity = createEntity(entityData, id);
        }
        loadEntity(entityData, entity, entityNode);
        return entity;
    }

    private int createEntity(EntityData entityData, String id) {
        int entity = entityData.createEntity();
        if (id != null) {
            cachedEntities.lastElement().put(id, entity);
        }
        return entity;
    }

    private void loadEntity(EntityData entityData, int entity, NODE entityNode) {
        String templateText = format.getAttribute(entityNode, "template");
        if (templateText != null) {
            EntityTemplate.loadTemplate(entityData, entity, parseTemplate(entityData, templateText));
        }
        for (NODE componentNode : format.getChildren(entityNode)) {
            if (isNodeEnabled(entityData, componentNode)) {
                ComponentParser componentParser = templateManager.getComponentParser(componentNode);
                Object value = componentParser.parseValue(this, format, entityData, componentNode);
                entityData.setComponent(entity, componentParser.getComponent(), value);
            }
        }
    }

    public String parseTemplateText(EntityData entityData, String templateText) {
        return parseTemplate(entityData, templateText).getText();
    }

    public EntityTemplate parseTemplate(EntityData entityData, String templateText) {
        String template = templateText;
        if (template.startsWith("./") || template.startsWith("../")) {
            template = currentDirectories.lastElement() + template;
        }
        return EntityTemplate.parseTemplate(template, text -> {
            if (text.startsWith("#")) {
                return text.substring(1);
            } else if (text.startsWith("[") && text.endsWith("]")) {
                return text.substring(1, text.length() - 1);
            }
            return text;
        }, key -> parseValue(entityData, key), value -> parseValue(entityData, value));
    }

    private boolean isNodeEnabled(EntityData entityData, NODE node) {
        String ifCondition = format.getAttribute(node, "if");
        return ((ifCondition == null) || parseValueBoolean(entityData, ifCondition));
    }

    private boolean parseValueBoolean(EntityData entityData, String text) {
        String valueText = text;
        boolean inverted = false;
        if (valueText.startsWith("!")) {
            valueText = valueText.substring(1);
            inverted = true;
        }
        String value = parseValue(entityData, valueText);
        boolean isTruthy = ((!value.isEmpty()) && (!value.equals("false")) && (!value.equals("0")));
        return (isTruthy != inverted);
    }

    public String parseValue(EntityData entityData, String text) {
        if (text.startsWith("#")) {
            String entityId = text.substring(1);
            Integer entity = cachedEntities.lastElement().get(entityId);
            if (entity == null) {
                entity = createEntity(entityData, entityId);
            }
            return entity.toString();
        }
        HashMap<String, String> values = cachedValues.lastElement();
        for (Entry<String, String> valueEntry : values.entrySet()) {
            text = text.replaceAll("\\[" + valueEntry.getKey() + "\\]", valueEntry.getValue());
        }
        return text;
    }
}
