package roguelike.modifiers;

import roguelike.Mob.BaseEntity;

public class Poison extends Effect {
	private int damage;

	public Poison(int dam, int dur) {
		super(dur);
		this.damage = dam;
	}

	public int damage() {
		return this.damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public void start(BaseEntity entity) {
		if (entity.isPlayer()) {
			entity.doAction("feel sick.");
		} else {
			entity.doAction("The %s looks sick.", entity.name());
		}
	}

	public void update(BaseEntity entity) {
		updateDuration(1);
		entity.modifyHP(-damage(), "died of poison.");
	}

	public void end(BaseEntity entity) {
		if (entity.isPlayer()) {
			entity.doAction("feel better.");
		} else {
			entity.doAction("The %s looks relieved.", entity.name());
		}
	}

}
