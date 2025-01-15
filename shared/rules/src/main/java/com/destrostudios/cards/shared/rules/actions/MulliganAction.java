package com.destrostudios.cards.shared.rules.actions;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
public class MulliganAction extends Action {
    private int[] cards;
}