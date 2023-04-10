package com.destrostudios.cards.backend.application.modules.bot;

import com.destrostudios.gametools.network.shared.modules.game.NetworkRandom;
import lombok.AllArgsConstructor;

import java.util.Random;

@AllArgsConstructor
public class SimpleRandom implements NetworkRandom {

    private Random random;

    @Override
    public int nextInt(int maxExclusive) {
        return random.nextInt(maxExclusive);
    }
}
