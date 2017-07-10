package roguelike.modifiers;

import roguelike.Mob.BaseEntity;

public class Healing extends Effect {
	private int heal;
	
	public Healing(int heal, int duration, String eType){
	    super(duration, eType);
		this.heal = heal;
	}
	
	public int healing(){ return this.heal; }
	public void setHealing(int heal){ this.heal = heal; }
	
	public void start(BaseEntity entity){
		if(entity.isPlayer()){
			entity.doAction("feel better.");
		}
		else{
			entity.doAction("The %s looks better.", entity.name());
		}
	}
	
	public void update(BaseEntity entity){
		updateDuration(1);
		entity.modifyHP(heal, "died of healing?");
	}
}
