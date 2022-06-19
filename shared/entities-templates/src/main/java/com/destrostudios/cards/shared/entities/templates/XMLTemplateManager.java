package com.destrostudios.cards.shared.entities.templates;

import com.destrostudios.cards.shared.entities.EntityData;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.InputStream;
import java.util.HashMap;

public class XMLTemplateManager{

    public XMLTemplateManager(TemplateReader templateReader) {
        this.templateReader = templateReader;
    }
    private TemplateReader templateReader;
    private HashMap<String, XMLComponentParser> xmlComponentParsers = new HashMap<>();
    private HashMap<String, Document> cachedDocuments = new HashMap<>();

    public <T> void registerComponent(XMLComponentParser<T> xmlComponentParser){
        xmlComponentParsers.put(xmlComponentParser.getElementName(), xmlComponentParser);
    }

    public void loadTemplate(EntityData entityData, int entity, EntityTemplate entityTemplate) {
        Document document = getDocument(entityTemplate.getName());
        if (document != null) {
            XMLTemplateReader templateReader = new XMLTemplateReader(this);
            templateReader.loadTemplate(entityData, entity, entityTemplate, document);
        }
    }

    private Document getDocument(String templateName){
        Document document = cachedDocuments.get(templateName);
        if (document == null) {
            try {
                InputStream inputStream = templateReader.read(templateName);
                document = new SAXBuilder().build(inputStream);
            } catch (Exception ex) {
                System.err.println("Error while loading template '" + templateName + "': " + ex.getMessage());
            }
            cachedDocuments.put(templateName, document);
        }
        return document;
    }

    public <T> XMLComponentParser<T> getComponentParser(Element element){
        XMLComponentParser<T> xmlComponentParser = xmlComponentParsers.get(element.getName());
        if (xmlComponentParser != null) {
            return xmlComponentParser;
        }
        throw new RuntimeException("Unregistered component '" + element.getName() + "'");
    }
}
