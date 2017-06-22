package roguelike.modifiers;

import roguelike.Mob.BaseEntity;

public class Healing implements Effect{
	private int duration, heal;
	
	public Healing(int heal, int duration){ this.heal = heal; this.duration = duration; }
	
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
		duration--;
		entity.modifyHP(heal, "died of healing?");
	}
	
	public void end(BaseEntity entity){}
	public boolean isDone(){ return duration < 1; }
}
