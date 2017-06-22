package roguelike.Items;

import java.awt.Color;

import roguelike.modifiers.Effect;

public class BaseItem implements ItemInterface, Comparable <BaseItem>{
	private String name, appearance, itemType;
	private char glyph;
	private Color color;
	private int armorValue, dodgeValue, toHitBonus, damageValue;
	private double weight;
	public int x, y;
	private Effect effect;
	
	public BaseItem(String name, char glyph, Color color, String itemType, double weight){
		this.name = name;
		this.glyph = glyph;
		this.color = color;
		this.itemType = itemType;
		this.weight = weight;
	}
	
	public String name(){ return this.name; }
	public String details(){ return name(); }
	public String itemType(){ return this.itemType; }
	public char glyph(){ return this.glyph; }
	public Color color(){ return this.color; }
	public double weight(){ return this.weight; }
	
	public Effect effect(){ return this.effect; }
	public void setEffect(Effect effect){ this.effect = effect; }
	
	public int toHit(){ return this.toHitBonus; }
	public void setToHit(int bonus){ this.toHitBonus = bonus; }
	public void modifyToHit(int bonus){ this.toHitBonus += bonus; }
	
	public int damageValue(){ return this.damageValue; }
	public void setDamageValue(int bonus){ this.damageValue = bonus; }
	public void modifyDamageValue(int bonus){ this.damageValue += bonus; }
	
	public int armorValue(){ return this.armorValue; }
	public void setArmorValue(int armor){ this.armorValue = armor; }
	public void modifyArmorValue(int modification){ this.armorValue += modification; }
	
	public int dodgeValue(){ return this.dodgeValue; }
	public void setDodgeValue(int dodge){ this.dodgeValue = dodge; }
	public void modifyDodgeValue(int modification){ this.dodgeValue += modification; }
	
	
	
	
	@Override
	public int compareTo(BaseItem otherItem){ return this.name().compareToIgnoreCase(otherItem.name()); }
}
