package com.destrostudios.cards.shared.rules.effects;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class EffectOptions {
    int discoverIndex;
}
