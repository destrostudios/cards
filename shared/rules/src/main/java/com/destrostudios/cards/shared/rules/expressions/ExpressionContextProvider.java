package com.destrostudios.cards.shared.rules.expressions;

import com.destrostudios.cards.shared.entities.EntityData;
import org.apache.commons.jexl3.JexlContext;

public interface ExpressionContextProvider {
    void fillExpressionContext(EntityData data, JexlContext context);
}
