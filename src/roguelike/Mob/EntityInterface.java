package roguelike.Mob;

import java.awt.Color;

import roguelike.Level.Level;

public interface EntityInterface {
	public Level level();
	public String name();
	public Color color();
	public char glyph();
	public int maxHP();
	public int currentHP();
	public int armor();
	public void move(int x, int y);
	public void meleeAttack(BaseEntity otherEntity);
	public void notify(String message, Object...params);
}
