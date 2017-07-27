package roguelike.modifiers;

import roguelike.mob.BaseEntity;

public class Healing extends Effect {
	
	public Healing(String eType, String onCast, String onUpdate, String onEnd, int durNumDice, int durDiceSize, int healNumDice, int healDiceSize){
	    super(eType, onCast, onUpdate, onEnd);
	    setDurationDice(durNumDice);
	    setDurationDiceSize(durDiceSize);
	    setEffectDice(healNumDice);
	    setEffectDiceSize(healDiceSize);
	}

	@Override
	public void start(BaseEntity entity){
		setDuration();
		setEffectValue();
		if(entity.isPlayer()){
			entity.doAction("feel " + getOnCast());
		}
		else{
			entity.doAction("look " + getOnCast(), entity.getName());
		}
	}

	@Override
	public void update(BaseEntity entity){
		subtractFromDuration(1);
		setEffectValue();
		if(!getOnUpdate().equalsIgnoreCase("none")){
			entity.doAction(getOnUpdate());
		}
		entity.modifyHP(getEffectValue(), "died of healing?");
	}
}
