package com.destrostudios.cards.shared.entities.templates;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Stack;

public class TemplateParser<NODE> {

    public TemplateParser(TemplateManager templateManager, TemplateFormat<NODE> format, TemplateRecording recording) {
        this.templateManager = templateManager;
        this.format = format;
        this.recording = recording;
    }
    private TemplateManager templateManager;
    private TemplateFormat<NODE> format;
    private TemplateRecording recording;
    private Stack<String> currentDirectories = new Stack<>();
    private Stack<HashMap<String, EntityProxy>> cachedEntities = new Stack<>();
    private Stack<HashMap<String, Object>> cachedValues = new Stack<>();

    public void loadTemplate(EntityProxy entityProxy, Template template, NODE root) {
        String currentDirectory = "";
        String[] directories = template.getName().split("/");
        for (int i = 0; i < (directories.length - 1); i++) {
            currentDirectory += directories[i] + "/";
        }
        currentDirectories.push(currentDirectory);
        HashMap<String, EntityProxy> entities = new HashMap<>(10);
        cachedEntities.push(entities);
        HashMap<String, Object> values = new HashMap<>();
        NODE valuesNode = format.getChild(root, TemplateKeyword.VALUES);
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
        for (NODE entityNode : format.getChildren(root, TemplateKeyword.ENTITY)) {
            if (isFirstEntity) {
                String id = format.getAttribute(entityNode, TemplateKeyword.ID);
                if (id != null) {
                    cachedEntities.lastElement().put(id, entityProxy);
                }
                loadEntity(entityProxy, entityNode);
            } else {
                createAndLoadEntity(entityNode);
            }
            isFirstEntity = false;
        }
        // Export
        if (cachedValues.size() > 1) {
            HashMap<String, Object> parentTemplateValues = cachedValues.get(cachedValues.size() - 2);
            parentTemplateValues.putAll(template.getOutput());
        }
        currentDirectories.pop();
        cachedEntities.pop();
        cachedValues.pop();
    }

    public EntityProxy createAndLoadEntity(NODE entityNode) {
        if ((!isNodeEnabled(entityNode)) || format.getName(entityNode).equals(TemplateKeyword.EMPTY)) {
            return null;
        }
        EntityProxy entityProxy = null;
        String id = format.getAttribute(entityNode, TemplateKeyword.ID);
        if (id != null) {
            entityProxy = cachedEntities.lastElement().get(id);
        }
        if (entityProxy == null) {
            entityProxy = createEntity(id);
        }
        loadEntity(entityProxy, entityNode);
        return entityProxy;
    }

    private EntityProxy createEntity(String id) {
        EntityProxy entityProxy = new EntityProxy(recording.createEntity());
        if (id != null) {
            cachedEntities.lastElement().put(id, entityProxy);
        }
        return entityProxy;
    }

    private void loadEntity(EntityProxy entityProxy, NODE entityNode) {
        String templateText = format.getAttribute(entityNode, TemplateKeyword.TEMPLATE);
        if (templateText != null) {
            EntityTemplate.loadTemplate(recording, entityProxy.entity(), parseTemplate(templateText));
        }
        for (NODE componentNode : format.getChildren(entityNode)) {
            if (isNodeEnabled(componentNode)) {
                Object component = templateManager.getComponent(format.getName(componentNode));
                ComponentParser componentParser = templateManager.getComponentParser(component);
                Object value = componentParser.parse(this, format, componentNode);
                recording.setComponent(entityProxy.entity(), component, value);
            }
        }
    }

    public Template parseTemplate(String templateText) {
        String template = templateText;
        if (template.startsWith("./") || template.startsWith("../")) {
            template = currentDirectories.lastElement() + template;
        }
        return Template.parse(template, text -> {
            if (text.startsWith("#")) {
                return text.substring(1);
            } else if (text.startsWith("[") && text.endsWith("]")) {
                return text.substring(1, text.length() - 1);
            }
            return text;
        }, this::parseText, this::parseEntityOrText);
    }

    private boolean isNodeEnabled(NODE node) {
        String ifdefCondition = format.getAttribute(node, TemplateKeyword.IFDEF);
        if (ifdefCondition != null) {
            HashMap<String, Object> values = cachedValues.lastElement();
            if (!values.containsKey(ifdefCondition)) {
                return false;
            }
        }
        String ifCondition = format.getAttribute(node, TemplateKeyword.IF);
        return ((ifCondition == null) || parseBoolean(ifCondition));
    }

    public boolean parseBoolean(String text) {
        String valueText = text;
        boolean inverted = false;
        if (valueText.startsWith("!")) {
            valueText = valueText.substring(1);
            inverted = true;
        }
        String value = parseText(valueText);
        boolean isTruthy = ((!value.isEmpty()) && (!value.equals("false")) && (!value.equals("0")));
        return (isTruthy != inverted);
    }

    public Object parseEntityOrText(String text) {
        Object value = parseEntityNullable(text);
        if (value == null) {
            value = parseText(text);
        }
        return value;
    }

    public Object parseEntity(String text) {
        Object entity = parseEntityNullable(text);
        if (entity == null) {
            throw new RuntimeException("Invalid entity text '" + text + "'");
        }
        return entity;
    }

    public Object parseEntityNullable(String text) {
        if (text.startsWith("#")) {
            String entityId = text.substring(1);
            EntityProxy entityProxy = cachedEntities.lastElement().get(entityId);
            if (entityProxy == null) {
                entityProxy = createEntity(entityId);
            }
            return entityProxy;
        }
        HashMap<String, Object> values = cachedValues.lastElement();
        for (Entry<String, Object> valueEntry : values.entrySet()) {
            if (text.equals("[" + valueEntry.getKey() + "]")) {
                return valueEntry.getValue();
            }
        }
        return null;
    }

    public String parseTextNullable(String text) {
        return ((text != null) ? parseText(text) : null);
    }

    public String parseText(String text) {
        HashMap<String, Object> values = cachedValues.lastElement();
        for (Entry<String, Object> valueEntry : values.entrySet()) {
            text = text.replaceAll("\\[" + valueEntry.getKey() + "\\]", valueEntry.getValue().toString());
        }
        return text;
    }
}
