package com.destrostudios.cards.shared.entities.templates;

import com.destrostudios.cards.shared.entities.EntityData;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Stack;

/**
 *
 * @author Carl
 */
public class XMLTemplateManager {

    public XMLTemplateManager(TemplateReader templateReader){
        this.templateReader = templateReader;
    }
    private TemplateReader templateReader;
    private HashMap<String, XMLComponentParser> xmlComponentParsers = new HashMap<>();
    private HashMap<String, Document> cachedDocuments = new HashMap<>();
    private String currentDirectory;
    private Stack<HashMap<String, Integer>> cachedEntities = new Stack<>();
    private Stack<HashMap<String, String>> cachedValues = new Stack<>();

    public <T> void registerComponent(XMLComponentParser<T> xmlComponentParser){
        xmlComponentParsers.put(xmlComponentParser.getElementName(), xmlComponentParser);
    }

    public void loadTemplate(EntityData entityData, int entity, String templateName, String[] parameters){
        currentDirectory = "";
        String[] directories = templateName.split("/");
        for(int i=0;i<(directories.length - 1);i++){
            currentDirectory += directories[i] + "/";
        }
        Document document = getDocument(templateName);
        if (document != null) {
            loadTemplate(entityData, entity, document, parameters);
        }
    }

    private Document getDocument(String templateName){
        Document document = cachedDocuments.get(templateName);
        if (document == null) {
            try {
                InputStream inputStream = templateReader.read(templateName);
                document = new SAXBuilder().build(inputStream);
            } catch(Exception ex) {
                System.err.println("Error while reading template '" + templateName + "'.");
            }
            cachedDocuments.put(templateName, document);
        }
        return document;
    }

    public void loadTemplate(EntityData entityData, int entity, Document document, String[] parameters){
        Element templateElement = document.getRootElement();
        cachedEntities.push(new HashMap<String, Integer>(10));
        HashMap<String, String> values = new HashMap<String, String>();
        String defaultParameterText = templateElement.getAttributeValue("defaultParameters");
        if(defaultParameterText != null){
            String[] defaultParameters = defaultParameterText.split(",");
            for(int i=0;i<defaultParameters.length;i++){
                values.put("parameter" + i, defaultParameters[i]);
            }
        }
        for(int i=0;i<parameters.length;i++){
            if(!parameters[i].equals("default")){
                values.put("parameter" + i, parameters[i]);
            }
        }
        cachedValues.push(values);
        boolean isFirstEntity = true;
        for(Object entityElementObject : templateElement.getChildren()){
            Element entityElement = (Element) entityElementObject;
            if(entityElement.getName().equals("entity")){
                if(isFirstEntity){
                    String id = entityElement.getAttributeValue("id");
                    if(id != null){
                        cachedEntities.lastElement().put(id, entity);
                    }
                    loadEntity(entityData, entity, entityElement);
                }
                else{
                    createAndLoadEntity(entityData, entityElement);
                }
                isFirstEntity = false;
            }
            else if(entityElement.getName().equals("value")){
                String valueName = entityElement.getAttributeValue("name");
                String value = parseValue(entityData, entityElement.getText());
                cachedValues.lastElement().put(valueName, value);
            }
        }
        cachedEntities.pop();
        cachedValues.pop();
    }

    public int createAndLoadEntity(EntityData entityData, Element entityElement){
        if(entityElement.getName().equals("empty")){
            return -1;
        }
        Integer entity = null;
        String id = entityElement.getAttributeValue("id");
        if(id != null){
            entity = cachedEntities.lastElement().get(id);
        }
        if(entity == null){
            entity = createEntity(entityData, id);
        }
        loadEntity(entityData, entity, entityElement);
        return entity;
    }

    private int createEntity(EntityData entityData, String id){
        int entity = entityData.createEntity();
        if(id != null){
            cachedEntities.lastElement().put(id, entity);
        }
        return entity;
    }

    private void loadEntity(EntityData entityData, int entity, Element entityElement){
        String templateXMLText = entityElement.getAttributeValue("template");
        if(templateXMLText != null){
            EntityTemplate.loadTemplate(entityData, entity, parseTemplate(entityData, templateXMLText));
        }
        for(Object componentElementObject : entityElement.getChildren()){
            Element componentElement = (Element) componentElementObject;
            XMLComponentParser<Object> componentParser = getComponentParser(entityData, componentElement);
            if (componentParser != null) {
                Object value = componentParser.parseValue();
                entityData.setComponent(entity, componentParser.getComponent(), value);
            }
        }
    }

    public String parseTemplate(EntityData entityData, String templateXMLText){
        String template = templateXMLText.replaceAll("\\./", currentDirectory);
        if(template.matches("(.*)\\((.*)\\)")){
            int bracketStart = template.indexOf("(");
            int bracketEnd = template.indexOf(")");
            String[] parameters = template.substring(bracketStart + 1, bracketEnd).split(",");
            template = template.substring(0, bracketStart);
            for(String parameter : parameters){
                template += "," + parseValue(entityData, parameter);
            }
        }
        return template;
    }

    public String parseValue(EntityData entityData, String text){
        if(text.startsWith("#")){
            String entityID = text.substring(1);
            Integer entity;
            if(entityID.startsWith("#")){
                entityID = entityID.substring(1);
                entity = createEntity(entityData, entityID);
            }
            else{
                entity = cachedEntities.lastElement().get(entityID);
                if(entity == null){
                    System.err.println("Undefined entity id '" + entityID + "'.");
                }
            }
            return entity.toString();
        }
        HashMap<String, String> values = cachedValues.lastElement();
        for(Entry<String, String> valueEntry : values.entrySet()){
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
