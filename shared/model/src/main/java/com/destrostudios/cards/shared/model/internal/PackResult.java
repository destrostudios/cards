package com.destrostudios.cards.shared.model.internal;

import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class PackResult {
    private List<BaseCardIdentifier> cards;
}
