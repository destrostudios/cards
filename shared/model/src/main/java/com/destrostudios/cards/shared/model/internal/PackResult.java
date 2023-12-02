package com.destrostudios.cards.shared.model.internal;

import com.destrostudios.cards.shared.model.BaseCardIdentifier;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class PackResult {
    private List<BaseCardIdentifier> cards;
}
