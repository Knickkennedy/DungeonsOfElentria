package roguelike.modifiers;

import roguelike.Mob.BaseEntity;

public interface Effect{
	public boolean isDone();
	public void update(BaseEntity entity);
	public void start(BaseEntity entity);
	public void end(BaseEntity entity);
}
