package com.destrostudios.cards.shared.entities.templates;

import com.destrostudios.cards.shared.entities.EntityData;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Stack;

public class XMLTemplateReader {

    public XMLTemplateReader(XMLTemplateManager templateManager) {
        this.templateManager = templateManager;
    }
    private XMLTemplateManager templateManager;
    private Stack<String> currentDirectories = new Stack<>();
    private Stack<HashMap<String, Integer>> cachedEntities = new Stack<>();
    private Stack<HashMap<String, String>> cachedValues = new Stack<>();

    public void loadTemplate(EntityData entityData, int entity, EntityTemplate template, Document document) {
        Element templateElement = document.getRootElement();
        String currentDirectory = "";
        String[] directories = template.getName().split("/");
        for (int i = 0; i < (directories.length - 1); i++) {
            currentDirectory += directories[i] + "/";
        }
        currentDirectories.push(currentDirectory);
        HashMap<String, Integer> entities = new HashMap<>(10);
        cachedEntities.push(entities);
        HashMap<String, String> values = new HashMap<>();
        Element valuesElement = templateElement.getChild("values");
        if (valuesElement != null) {
            for (Element valueElement : valuesElement.getChildren()) {
                values.put(valueElement.getName(), valueElement.getText());
                // Save the unmodified default value so it can be exported and accessed by parent
                values.put("_" + valueElement.getName(), valueElement.getText());
            }
        }
        for (Entry<String, String> parameterEntry : template.getInput().entrySet()) {
            values.put(parameterEntry.getKey(), parameterEntry.getValue());
        }
        cachedValues.push(values);
        boolean isFirstEntity = true;
        for (Element entityElement : templateElement.getChildren("entity")) {
            if (isFirstEntity) {
                String id = entityElement.getAttributeValue("id");
                if (id != null) {
                    cachedEntities.lastElement().put(id, entity);
                }
                loadEntity(entityData, entity, entityElement);
            } else {
                createAndLoadEntity(entityData, entityElement);
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

    public int createAndLoadEntity(EntityData entityData, Element entityElement) {
        if ((!isElementEnabled(entityData, entityElement)) || entityElement.getName().equals("empty")) {
            return -1;
        }
        Integer entity = null;
        String id = entityElement.getAttributeValue("id");
        if (id != null) {
            entity = cachedEntities.lastElement().get(id);
        }
        if (entity == null) {
            entity = createEntity(entityData, id);
        }
        loadEntity(entityData, entity, entityElement);
        return entity;
    }

    private int createEntity(EntityData entityData, String id) {
        int entity = entityData.createEntity();
        if (id != null) {
            cachedEntities.lastElement().put(id, entity);
        }
        return entity;
    }

    private void loadEntity(EntityData entityData, int entity, Element entityElement) {
        String templateXMLText = entityElement.getAttributeValue("template");
        if (templateXMLText != null) {
            EntityTemplate.loadTemplate(entityData, entity, parseTemplate(entityData, templateXMLText));
        }
        for (Element componentElement : entityElement.getChildren()) {
            if (isElementEnabled(entityData, componentElement)) {
                XMLComponentParser<Object> componentParser = templateManager.getComponentParser(componentElement);
                Object value = componentParser.parseValue(this, entityData, componentElement);
                entityData.setComponent(entity, componentParser.getComponent(), value);
            }
        }
    }

    public String parseTemplateText(EntityData entityData, String templateXMLText) {
        return parseTemplate(entityData, templateXMLText).getText();
    }

    public EntityTemplate parseTemplate(EntityData entityData, String templateXMLText) {
        String template = templateXMLText;
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

    private boolean isElementEnabled(EntityData entityData, Element element) {
        String ifCondition = element.getAttributeValue("if");
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
