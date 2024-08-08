package com.destrostudios.cards.frontend.application.appstates.services;

import com.destrostudios.cardgui.BoardSettings;
import com.destrostudios.cardgui.transformations.speeds.TimeBasedRotationTransformationSpeed;
import com.destrostudios.cardgui.transformations.speeds.TimeBasedVectorTransformationSpeed3f;

public class BoardUtil {

    public static BoardSettings.BoardSettingsBuilder getDefaultSettings(String inputActionPrefix) {
        return BoardSettings.builder()
                .inputActionPrefix(inputActionPrefix)
                .cardInZonePositionTransformationSpeed(() -> new TimeBasedVectorTransformationSpeed3f(0.8f))
                .cardInZoneRotationTransformationSpeed(() -> new TimeBasedRotationTransformationSpeed(0.4f))
                .cardInZoneScaleTransformationSpeed(() -> new TimeBasedVectorTransformationSpeed3f(0.8f));
    }
}
