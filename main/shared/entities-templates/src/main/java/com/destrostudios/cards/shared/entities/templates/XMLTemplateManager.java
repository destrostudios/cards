package com.destrostudios.cards.shared.entities.templates;

import com.destrostudios.cards.shared.entities.EntityData;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Stack;

public class XMLTemplateManager{

    public XMLTemplateManager(TemplateReader templateReader) {
        this.templateReader = templateReader;
    }
    private TemplateReader templateReader;
    private HashMap<String, XMLComponentParser> xmlComponentParsers = new HashMap<>();
    private HashMap<String, Document> cachedDocuments = new HashMap<>();
    private Stack<String> currentDirectories = new Stack<>();
    private Stack<HashMap<String, Integer>> cachedEntities = new Stack<>();
    private Stack<HashMap<String, String>> cachedValues = new Stack<>();

    public <T> void registerComponent(XMLComponentParser<T> xmlComponentParser){
        xmlComponentParsers.put(xmlComponentParser.getElementName(), xmlComponentParser);
    }

    public void loadTemplate(EntityData entityData, int entity, EntityTemplate entityTemplate) {
        Document document = getDocument(entityTemplate.getName());
        if (document != null) {
            loadTemplate(entityData, entity, entityTemplate, document);
        }
    }

    private Document getDocument(String templateName){
        Document document = cachedDocuments.get(templateName);
        if (document == null) {
            try {
                InputStream inputStream = templateReader.read(templateName);
                document = new SAXBuilder().build(inputStream);
            } catch (Exception ex) {
                System.err.println("Error while loading template '" + templateName + "'.");
            }
            cachedDocuments.put(templateName, document);
        }
        return document;
    }

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
                XMLComponentParser<Object> componentParser = getComponentParser(entityData, componentElement);
                if (componentParser != null) {
                    Object value = componentParser.parseValue();
                    entityData.setComponent(entity, componentParser.getComponent(), value);
                }
            }
        }
    }

    public String parseTemplateText(EntityData entityData, String templateXMLText) {
        return parseTemplate(entityData, templateXMLText).getText();
    }

    public EntityTemplate parseTemplate(EntityData entityData, String templateXMLText) {
        String template = templateXMLText.replaceFirst("\\./", currentDirectories.lastElement());
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

    private <T> XMLComponentParser<T> getComponentParser(EntityData entityData, Element element){
        XMLComponentParser<T> xmlComponentParser = xmlComponentParsers.get(element.getName());
        if(xmlComponentParser != null){
            xmlComponentParser.prepare(this, entityData, element);
            return xmlComponentParser;
        }
        System.err.println("Unregistered component '" + element.getName() + "'");
        return null;
    }
}
