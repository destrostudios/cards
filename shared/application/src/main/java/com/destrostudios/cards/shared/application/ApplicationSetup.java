package com.destrostudios.cards.shared.application;

import com.destrostudios.cards.shared.files.FileAssets;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.expressions.Expressions;
import com.destrostudios.cards.shared.rules.util.DiscoverUtil;

public class ApplicationSetup {

    public static void setup() {
        FileAssets.readRootFile();
        Components.setup();
        EntityTemplateSetup.setup();
        DiscoverUtil.setup();
        Expressions.setup();
    }
}
