package com.destrostudios.cards.shared.entities.templates;

import java.io.InputStream;
import java.util.List;

public interface TemplateFormat<NODE> {

    NODE readRoot(InputStream inputStream);

    String getName(NODE node);

    String getText(NODE node);

    String getAttribute(NODE node, String name);

    List<NODE> getChildren(NODE node);

    List<NODE> getChildren(NODE node, String name);

    NODE getChild(NODE node, String name);
}
