package roguelike.Mob;

import java.awt.Color;

import roguelike.Level.Level;

public class EnemyEntity extends BaseEntity{
	
	public EnemyEntity(Level level, char glyph, Color color){ 
		super(level, glyph, color);
		setMaxCarryWeight(9999);
		setInventory(this);
		setVisionRadius(5);
	}
}
