package com.destrostudios.cards.shared.rules.expressions;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.TargetPrefilter;
import com.destrostudios.cards.shared.rules.util.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ExpressionEntity {

    private EntityData data;
    @Getter
    private int entity;

    public String getName() {
        return data.getComponent(entity, Components.NAME);
    }

    public boolean getIsPlayer() {
        return data.hasComponent(entity, Components.NEXT_PLAYER);
    }

    public boolean getIsCreatureCard() {
        return data.hasComponent(entity, Components.CREATURE_CARD);
    }

    public boolean getIsSpellCard() {
        return data.hasComponent(entity, Components.SPELL_CARD);
    }

    public boolean getIsInHand() {
        return data.hasComponent(entity, Components.HAND);
    }

    public boolean getIsOnBoard() {
        return data.hasComponent(entity, Components.BOARD);
    }

    public boolean getIsInGraveyard() {
        return data.hasComponent(entity, Components.GRAVEYARD);
    }

    public Integer getManaCost() {
        int[] spells = data.getComponent(entity, Components.SPELLS);
        if (spells != null) {
            for (int spell : spells) {
                if (SpellUtil.isDefaultCastFromHandSpell(data, spell)) {
                    return CostUtil.getEffectiveManaCost(data, spell);
                }
            }
        }
        return null;
    }

    public Integer getAttack() {
        return StatsUtil.getEffectiveAttack(data, entity);
    }

    public Integer getHealth() {
        return StatsUtil.getEffectiveHealth(data, entity);
    }

    public boolean getIsDamaged() {
        return StatsUtil.isDamaged(data, entity);
    }

    public boolean getIsBeast() {
        return data.hasComponent(entity, Components.Tribe.BEAST);
    }

    public boolean getIsDragon() {
        return data.hasComponent(entity, Components.Tribe.DRAGON);
    }

    public boolean getIsGoblin() {
        return data.hasComponent(entity, Components.Tribe.GOBLIN);
    }

    public boolean isAlly(ExpressionEntity other) {
        return ConditionUtil.isAlly(data, entity, other.entity);
    }

    public boolean getIsDefaultCastFromHandSpell() {
        return SpellUtil.isDefaultCastFromHandSpell(data, entity);
    }

    public boolean exists(String targetPrefilterName, String expression) {
        return count(targetPrefilterName, expression) > 0;
    }

    public int count(String targetPrefilterName, String expression) {
        return all(targetPrefilterName, expression).size();
    }

    public List<ExpressionEntity> all(String targetPrefilterName, String expression) {
        return TargetUtil.getAllConditionTargets(data, entity, TargetPrefilter.valueOf(targetPrefilterName), expression).stream()
                .map(this::wrap)
                .collect(Collectors.toList());
    }

    public ExpressionEntity getOwner() {
        return wrap(data.getComponent(entity, Components.OWNED_BY));
    }

    public ExpressionEntity getOpponent() {
        return wrap(data.getComponent(entity, Components.NEXT_PLAYER));
    }

    public ExpressionEntity getTopLibraryCard() {
        return wrap(ZoneUtil.getTopLibraryCard(data, entity));
    }

    public ExpressionEntity getDefaultCastFromHandSpell() {
        return wrap(SpellUtil.getDefaultCastFromHandSpell(data, entity));
    }

    public ExpressionEntity getCaster() {
        return wrap(SpellUtil.getCaster(data, entity));
    }

    public ExpressionEntity[] map(ExpressionEntity[] expressionEntities, String expression) {
        return Arrays.stream(expressionEntities)
                .map(expressionEntity -> (ExpressionEntity) Expressions.evaluate(data, expression, entity, expressionEntity.entity))
                .filter(Objects::nonNull)
                .toArray(ExpressionEntity[]::new);
    }

    private ExpressionEntity wrap(Integer otherEntity) {
        return wrap(data, otherEntity);
    }

    public static ExpressionEntity wrap(EntityData data, Integer otherEntity) {
        return ((otherEntity != null) ? new ExpressionEntity(data, otherEntity) : null);
    }

    // Needed to be compared in expressions

    @Override
    public boolean equals(Object object) {
        return ((object instanceof ExpressionEntity other) && (entity == other.entity));
    }

    @Override
    public int hashCode() {
        return entity;
    }
}
