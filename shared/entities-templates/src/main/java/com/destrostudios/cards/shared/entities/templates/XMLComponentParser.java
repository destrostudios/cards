package com.destrostudios.cards.shared.entities.templates;

import com.destrostudios.cards.shared.entities.ComponentDefinition;
import com.destrostudios.cards.shared.entities.EntityData;
import org.jdom2.Element;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Carl
 */
public abstract class XMLComponentParser<T>{

    public XMLComponentParser(ComponentDefinition<T> component){
        this.component = component;
    }
    private ComponentDefinition<T> component;
    protected XMLTemplateManager xmlTemplateManager;
    protected EntityData entityData;
    protected Element element;

    public void prepare(XMLTemplateManager xmlTemplateManager,EntityData entityData, Element element){
        this.xmlTemplateManager = xmlTemplateManager;
        this.entityData = entityData;
        this.element = element;
    }

    public String getElementName(){
        return component.getName();
    }

    public abstract T parseValue();

    protected int[] createChildEntities(int offset, String parameterName){
        LinkedList<Integer> childEntities = new LinkedList<>();
        List children = element.getChildren();
        if(children.size() > 0){
            for(int i=0;i<children.size();i++){
                childEntities.add(xmlTemplateManager.createAndLoadEntity(entityData, (Element) children.get(i)));
            }
        }
        else if(element.getText().length() > 0){
            String[] textParts = element.getText().split(",");
            for(String textPart : textParts){
                childEntities.add(parseEntity(textPart));
            }
        }
        int parameterIndex = 0;
        String attributeValue;
        while((attributeValue = element.getAttributeValue(parameterName + parameterIndex)) != null){
            childEntities.add(parseEntity(attributeValue));
            parameterIndex++;
        }
        return Util.convertToArray_Integer(childEntities);
    }

    protected int createChildEntity(int index, String parameterName){
        List children = element.getChildren();
        if(children.size() > 0){
            if(index < children.size()){
                return xmlTemplateManager.createAndLoadEntity(entityData, (Element) children.get(index));
            }
        }
        else if((index == 0) && (element.getText().length() > 0)){
            return parseEntity(element.getText());
        }
        return parseEntity(element.getAttributeValue(parameterName));
    }

    private int parseEntity(String text){
        return Integer.parseInt(xmlTemplateManager.parseValue(entityData, text));
    }

    public ComponentDefinition<T> getComponent() {
        return component;
    }
}
