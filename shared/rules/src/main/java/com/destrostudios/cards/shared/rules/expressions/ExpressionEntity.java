package com.destrostudios.cards.shared.rules.expressions;

import com.destrostudios.cards.shared.entities.EntityData;
import com.destrostudios.cards.shared.rules.Components;
import com.destrostudios.cards.shared.rules.util.ConditionUtil;
import com.destrostudios.cards.shared.rules.util.CostUtil;
import com.destrostudios.cards.shared.rules.util.SpellUtil;
import com.destrostudios.cards.shared.rules.util.StatsUtil;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExpressionEntity {

    private EntityData data;
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

    public boolean getIsBeast() {
        return data.hasComponent(entity, Components.Tribe.BEAST);
    }

    public boolean getIsDragon() {
        return data.hasComponent(entity, Components.Tribe.DRAGON);
    }

    public boolean isAlly(ExpressionEntity other) {
        return ConditionUtil.isAlly(data, entity, other.entity);
    }

    public boolean getIsDefaultCastFromHandSpell() {
        return SpellUtil.isDefaultCastFromHandSpell(data, entity);
    }

    public boolean getHasNoCreatures() {
        return data.query(Components.CREATURE_ZONE).list(card -> data.getComponent(card, Components.OWNED_BY) == entity).isEmpty();
    }

    public ExpressionEntity getCaster() {
        return wrap(SpellUtil.getCaster(data, entity));
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
