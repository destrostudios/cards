package com.destrostudios.cards.shared.rules;

import com.destrostudios.cards.shared.rules.wrappers.Vanilla;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class TestVanilla extends TestGame {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @ParameterizedTest
    @CsvFileSource(resources = "/vanilla.csv")
    public @interface CardTest {}
    public record Params (String template, String name, int manaCost, int attack, int health) {}
    public static class Aggregator extends RecordAggregator<Params> {}

    @CardTest
    public void testCreation(@AggregateWith(Aggregator.class) Params params) {
        int card = create(params.template, player);
        assertComponent(card, Components.NAME, params.name);
        assertManaCost(card, params.manaCost);
        assertAttack(card, params.attack);
        assertHealth(card, params.health);
    }

    @CardTest
    public void testSummon(@AggregateWith(Aggregator.class) Params params) {
        int card = create(params.template, player, Components.HAND);
        castFromHand(card);
        assertHasComponent(card, Components.BOARD);
    }

    @CardTest
    public void testAttack(@AggregateWith(Aggregator.class) Params params) {
        int card = create(params.template, player, Components.CREATURE_ZONE);
        Vanilla target = createVanilla(0, 0, params.attack + 1, opponent, Components.CREATURE_ZONE);
        attack(card, target.card);
        assertHealth(target.card, target.health - params.attack);
    }

    @CardTest
    public void testDeath(@AggregateWith(Aggregator.class) Params params) {
        int card = create(params.template, player, Components.CREATURE_ZONE);
        destroy(card);
        assertHasComponent(card, Components.GRAVEYARD);
    }
}
