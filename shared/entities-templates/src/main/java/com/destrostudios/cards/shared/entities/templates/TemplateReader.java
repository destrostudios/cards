package com.destrostudios.cards.shared.entities.templates;

import java.io.InputStream;

public interface TemplateReader {
    InputStream read(String templateName);
}
