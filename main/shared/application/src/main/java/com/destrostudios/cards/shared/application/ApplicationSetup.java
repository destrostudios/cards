package com.destrostudios.cards.shared.application;

import com.destrostudios.cards.shared.files.FileAssets;

public class ApplicationSetup {

    public static void setup() {
        FileAssets.readRootFile();
        EntityTemplateSetup.setup();
    }
}
