package roguelike.modifiers;

import roguelike.Mob.BaseEntity;

public class Poison implements Effect{
	private int duration, damage;
	public Poison(int damage, int duration){ this.duration = duration; this.damage = damage;}
	
	public int damage(){ return this.damage; }
	public void setDamage(int damage){ this.damage = damage; }
	
	public void start(BaseEntity entity){
		if(entity.isPlayer()){
			entity.doAction("feel sick.");
		}
		else{
			entity.doAction("The %s looks sick.", entity.name());
		}
	}
	
	public void update(BaseEntity entity){
		this.duration--;
		entity.modifyHP(-damage, "died of poison.");
	}
	
	public void end(BaseEntity entity){
		if(entity.isPlayer()){
			entity.doAction("feel better.");
		}
		else{
			entity.doAction("The %s looks relieved.", entity.name());
		}
	}
	
	public boolean isDone(){ return duration < 1; }
}
