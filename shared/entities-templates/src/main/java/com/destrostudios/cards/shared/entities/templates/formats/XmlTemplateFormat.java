package com.destrostudios.cards.shared.entities.templates.formats;

import com.destrostudios.cards.shared.entities.templates.TemplateFormat;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class XmlTemplateFormat implements TemplateFormat<Element> {

    @Override
    public Element readRoot(InputStream inputStream) {
        try {
            Document document = new SAXBuilder().build(inputStream);
            return document.getRootElement();
        } catch (JDOMException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getName(Element element) {
        return element.getName();
    }

    @Override
    public String getText(Element element) {
        return element.getText();
    }

    @Override
    public String getAttribute(Element element, String name) {
        return element.getAttributeValue(name);
    }

    @Override
    public List<Element> getChildren(Element element) {
        return element.getChildren();
    }

    @Override
    public List<Element> getChildren(Element element, String name) {
        return element.getChildren(name);
    }

    @Override
    public Element getChild(Element element, String name) {
        return element.getChild(name);
    }
}
