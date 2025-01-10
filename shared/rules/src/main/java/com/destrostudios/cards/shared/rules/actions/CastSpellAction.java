package com.destrostudios.cards.shared.rules.actions;

import com.destrostudios.cards.shared.rules.effects.EffectOptions;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class CastSpellAction extends Action {
    private int source;
    private int spell;
    private int[] targets;
    private EffectOptions options;
}
