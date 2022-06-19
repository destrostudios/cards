package com.destrostudios.cards.shared.entities.templates;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import org.jdom2.Element;

import java.util.LinkedList;
import java.util.List;

public abstract class XMLComponentParser<T> {

    public XMLComponentParser(ComponentDefinition<T> component) {
        this.component = component;
    }
    private ComponentDefinition<T> component;

    public String getElementName() {
        return component.getName();
    }

    public abstract T parseValue(XMLTemplateReader templateReader, EntityData entityData, Element element);

    protected int[] createChildEntities(XMLTemplateReader templateReader, EntityData entityData, Element element, int offset, String parameterName) {
        LinkedList<Integer> childEntities = new LinkedList<>();
        List<Element> children = element.getChildren();
        if (children.size() > 0) {
            for (Element child : children) {
                childEntities.add(templateReader.createAndLoadEntity(entityData, child));
            }
        } else if (element.getText().length() > 0) {
            String[] textParts = element.getText().split(",");
            for (String textPart : textParts) {
                childEntities.add(parseEntity(templateReader, entityData, textPart));
            }
        }
        int parameterIndex = 0;
        String attributeValue;
        while ((attributeValue = element.getAttributeValue(parameterName + parameterIndex)) != null) {
            childEntities.add(parseEntity(templateReader, entityData,  attributeValue));
            parameterIndex++;
        }
        return Util.convertToArray_Integer(childEntities);
    }

    protected int createChildEntity(XMLTemplateReader templateReader, EntityData entityData, Element element, int index, String parameterName){
        List<Element> children = element.getChildren();
        if (children.size() > 0) {
            if (index < children.size()) {
                return templateReader.createAndLoadEntity(entityData, children.get(index));
            }
        } else if ((index == 0) && (element.getText().length() > 0)) {
            return parseEntity(templateReader, entityData, element.getText());
        }
        return parseEntity(templateReader, entityData, element.getAttributeValue(parameterName));
    }

    private int parseEntity(XMLTemplateReader templateReader, EntityData entityData, String text) {
        return Integer.parseInt(templateReader.parseValue(entityData, text));
    }

    public ComponentDefinition<T> getComponent() {
        return component;
    }
}
