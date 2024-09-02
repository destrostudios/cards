package com.destrostudios.cards.shared.rules.expressions;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.ComponentsParsing;
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
        return data.hasComponent(entity, Components.Zone.HAND);
    }

    public boolean getIsOnBoard() {
        return data.hasComponent(entity, Components.BOARD);
    }

    public boolean getIsInGraveyard() {
        return data.hasComponent(entity, Components.Zone.GRAVEYARD);
    }

    public Integer getManaCost() {
        int[] spells = data.getComponent(entity, Components.SPELLS);
        for (int spell : spells) {
            if (SpellUtil.isDefaultCastFromHandSpell(data, spell)) {
                return CostUtil.getEffectiveManaCost(data, spell);
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

    public boolean getIsMachine() {
        return data.hasComponent(entity, Components.Tribe.MACHINE);
    }

    public boolean isAlly(ExpressionEntity other) {
        return ConditionUtil.isAlly(data, entity, other.entity);
    }

    public boolean getIsDefaultCastFromHandSpell() {
        return SpellUtil.isDefaultCastFromHandSpell(data, entity);
    }

    public boolean hasBuff(int buff) {
        int[] buffs = data.getComponent(entity, Components.BUFFS);
        return ArrayUtil.contains(buffs, buff);
    }

    // [] is parsed as new Object[0], which doesn't call the according String[] methods

    public boolean exists(Object[] targetPrefilterNamesBasic, String[] targetPrefilterNamesAdvanced) {
        return exists(new String[0], targetPrefilterNamesAdvanced);
    }

    public boolean exists(String[] targetPrefilterNamesBasic, Object[] targetPrefilterNamesAdvanced) {
        return exists(targetPrefilterNamesBasic, new String[0]);
    }

    public boolean exists(String[] targetPrefilterNamesBasic, String[] targetPrefilterNamesAdvanced) {
        return exists(targetPrefilterNamesBasic, targetPrefilterNamesAdvanced, "");
    }

    public boolean exists(String[] targetPrefilterNamesBasic, String[] targetPrefilterNamesAdvanced, String expression) {
        return count(targetPrefilterNamesBasic, targetPrefilterNamesAdvanced, expression) > 0;
    }

    public int count(Object[] targetPrefilterNamesBasic, String[] targetPrefilterNamesAdvanced) {
        return count(new String[0], targetPrefilterNamesAdvanced);
    }

    public int count(String[] targetPrefilterNamesBasic, Object[] targetPrefilterNamesAdvanced) {
        return count(targetPrefilterNamesBasic, new String[0]);
    }

    public int count(String[] targetPrefilterNamesBasic, String[] targetPrefilterNamesAdvanced) {
        return count(targetPrefilterNamesBasic, targetPrefilterNamesAdvanced, "");
    }

    public int count(String[] targetPrefilterNamesBasic, String[] targetPrefilterNamesAdvanced, String expression) {
        return all(targetPrefilterNamesBasic, targetPrefilterNamesAdvanced, expression).size();
    }

    public List<ExpressionEntity> all(Object[] targetPrefilterNamesBasic, String[] targetPrefilterNamesAdvanced) {
        return all(new String[0], targetPrefilterNamesAdvanced);
    }

    public List<ExpressionEntity> all(String[] targetPrefilterNamesBasic, Object[] targetPrefilterNamesAdvanced) {
        return all(targetPrefilterNamesBasic, new String[0]);
    }

    public List<ExpressionEntity> all(String[] targetPrefilterNamesBasic, String[] targetPrefilterNamesAdvanced) {
        return all(targetPrefilterNamesBasic, targetPrefilterNamesAdvanced, "");
    }

    public List<ExpressionEntity> all(String[] targetPrefilterNamesBasic, String[] targetPrefilterNamesAdvanced, String expression) {
        Components.Prefilters prefilters = ComponentsParsing.parsePrefilters(targetPrefilterNamesBasic, targetPrefilterNamesAdvanced);
        return TargetUtil.getAllConditionTargets(data, entity, prefilters, expression).stream()
                .boxed()
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

    public ExpressionEntity getSource() {
        return wrap(data.getComponent(entity, Components.SOURCE));
    }

    public ExpressionEntity[] map(ExpressionEntity[] expressionEntities, String expression) {
        return Arrays.stream(expressionEntities)
                .map(expressionEntity -> (ExpressionEntity) Expressions.evaluate(expression, Expressions.getContext_Source_Target(data, entity, expressionEntity.entity)))
                .filter(Objects::nonNull)
                .toArray(ExpressionEntity[]::new);
    }

    private ExpressionEntity wrap(Integer otherEntity) {
        return wrap(data, otherEntity);
    }

    public static ExpressionEntity[] wrap(EntityData data, int[] entities) {
        ExpressionEntity[] expressionEntities = new ExpressionEntity[entities.length];
        for (int i = 0; i < expressionEntities.length; i++) {
            expressionEntities[i] = wrap(data, entities[i]);
        }
        return expressionEntities;
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
