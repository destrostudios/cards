package com.destrostudios.cards.shared.application;

import com.destrostudios.cards.shared.files.FileAssets;
import com.destrostudios.cards.shared.rules.expressions.Expressions;

public class ApplicationSetup {

    public static void setup() {
        FileAssets.readRootFile();
        EntityTemplateSetup.setup();
        Expressions.setup();
    }
}
