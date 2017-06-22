package roguelike.Items;

import java.awt.Color;

public class Cuisses extends Armor{

	public Cuisses(String name, char glyph, Color color, String itemType, double weight, int toHit, int attack, int dodge, int armor){
		super(name, glyph, color, itemType, weight);
		this.setToHit(toHit);
		this.setArmorValue(armor);
		this.setDodgeValue(dodge);
		this.setDamageValue(attack);
	}
}
