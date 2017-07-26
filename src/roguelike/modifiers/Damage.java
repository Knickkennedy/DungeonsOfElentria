package roguelike.modifiers;

import roguelike.mob.BaseEntity;

public class Damage extends Effect{
    public Damage(String eType, String onCast, String onUpdate, String onEnd, String killMessage, int durNumDice, int durDiceSize, int healNumDice, int healDiceSize){
        super(eType, onCast, onUpdate, onEnd);
        setDurationDice(durNumDice);
        setDurationDiceSize(durDiceSize);
        setEffectDice(healNumDice);
        setEffectDiceSize(healDiceSize);
        setKillMessage(killMessage);
    }

    @Override
    public void start(BaseEntity entity){
        setDuration();
        setEffectValue();
        entity.doAction(getOnCast());
    }

    @Override
    public void update(BaseEntity entity){
        subtractFromDuration(1);
        setEffectValue();
        if(!getOnUpdate().equalsIgnoreCase("none")){
            entity.doAction(getOnUpdate());
        }
        entity.modifyHP(-getEffectValue(), getKillMessage());
    }
}
