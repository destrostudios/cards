package com.destrostudios.cards.shared.entities.templates;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;

import java.util.LinkedList;
import java.util.List;

public abstract class ComponentParser<NODE, T> {

    public ComponentParser(ComponentDefinition<T> component) {
        this.component = component;
    }
    private ComponentDefinition<T> component;

    public String getElementName() {
        return component.getName();
    }

    public abstract T parseValue(TemplateParser<NODE> parser, TemplateFormat<NODE> format, EntityData entityData, NODE node);

    protected int[] createChildEntities(TemplateParser<NODE> parser, TemplateFormat<NODE> format, EntityData entityData, NODE node, String attributeName) {
        LinkedList<Integer> childEntities = new LinkedList<>();
        List<NODE> children = format.getChildren(node);
        if (children.size() > 0) {
            for (NODE child : children) {
                childEntities.add(parser.createAndLoadEntity(entityData, child));
            }
        } else if (format.getText(node).length() > 0) {
            String[] textParts = format.getText(node).split(",");
            for (String textPart : textParts) {
                childEntities.add(parseEntity(parser, entityData, textPart));
            }
        }
        int parameterIndex = 0;
        String attributeValue;
        while ((attributeValue = format.getAttribute(node, attributeName + parameterIndex)) != null) {
            childEntities.add(parseEntity(parser, entityData,  attributeValue));
            parameterIndex++;
        }
        return Util.convertToArray_Integer(childEntities);
    }

    protected int createChildEntity(TemplateParser<NODE> parser, TemplateFormat<NODE> format, EntityData entityData, NODE node, int index, String attributeName) {
        List<NODE> children = format.getChildren(node);
        if (children.size() > 0) {
            if (index < children.size()) {
                return parser.createAndLoadEntity(entityData, children.get(index));
            }
        } else if ((index == 0) && (format.getText(node).length() > 0)) {
            return parseEntity(parser, entityData, format.getText(node));
        }
        return parseEntity(parser, entityData, format.getAttribute(node, attributeName));
    }

    private int parseEntity(TemplateParser<NODE> parser, EntityData entityData, String text) {
        return Integer.parseInt(parser.parseValue(entityData, text));
    }

    public ComponentDefinition<T> getComponent() {
        return component;
    }
}
