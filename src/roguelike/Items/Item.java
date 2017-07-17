package roguelike.Items;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import asciiPanel.AsciiPanel;
import roguelike.Mob.BaseEntity;
import roguelike.Mob.Colors;
import roguelike.Mob.Spell;
import roguelike.World.Factory;
import roguelike.modifiers.Effect;
import roguelike.modifiers.Healing;
import roguelike.modifiers.Poison;
import roguelike.utility.RandomGen;

public class Item implements ItemInterface, Comparable <Item>{
	private String name, appearance, itemType;
	private char glyph;
	private Color color;
	private int armorValue, dodgeValue, toHitBonus, damageValue, damageBonus, numberOfDiceRolled, range, danger;
	private double weight;
	public int x, y;
	private List<Effect> effects = new ArrayList<Effect>();
	private Spell spell;
	private Factory factory;
	
	public Item(){}

	public void setName(String value){ this.name = value; }
	public String name(){ return this.name; }
	public String details(){ return name(); }

	public void setDanger(int value){ this.danger = value; }
	public int getDanger(){ return this.danger; }

	public void setItemType(String value){ this.itemType = value; }
	public String itemType(){ return this.itemType; }

	public void setGlyph(char value){ this.glyph = value; }
	public char glyph(){ return this.glyph; }

	public void setColor(Color color){ this.color = color; }
	public Color color(){ return this.color; }

	public void setWeight(double value){ this.weight = value; }
	public double weight(){ return this.weight; }

	public void setAttribute(String attribute, String value){
		if(attribute.equals("name")){
			setName(value);
		}
		else if(attribute.equals("symbol")){
			setGlyph(value.charAt(0));
		}
		else if(attribute.equals("color")){
			setColor(Colors.getColorLibrary().get(value));
		}
		else if(attribute.equals("item type")){
		    setItemType(value);
        }
        else if(attribute.equals("spell")){
			setSpell(value.trim());
		}
        else if(attribute.equals("effects")){
		    String effects[] = value.split(", ");
		    for(String effect : effects){
		        if(effect.contains("healing")){
		            String tokens[] = effect.split(" - ");
		            int numberOfDice = Integer.parseInt(tokens[1].trim());
		            int diceSize = Integer.parseInt(tokens[2].trim());
		            int duration = Integer.parseInt(tokens[3].trim());
		            int healing = 0;
		            for(int i = 0; i < numberOfDice; i++){
		                healing += RandomGen.rand(1, diceSize);
                    }
                    Healing heal = new Healing(healing, duration, "Healing");
                    effects().add(heal);
                }
                else if(effect.contains("poison")){
		        	if(effect.equals("cure poison")){
		        		List <Effect> tempEffects = new ArrayList<>();
		        		Effect newEffect = new Effect(1, "Cure Poison"){
		        			public void start(BaseEntity entity){
		        				for(Effect effect : entity.effects()){
		        					if(effect.getEffectType().equals("Poison")){
		        						tempEffects.add(effect);
									}
								}
								for(Effect effect : tempEffects){
		        					entity.effects().remove(effect);
								}
							}
						};
						effects().add(newEffect);
					}
					else {
						String tokens[] = effect.split(" - ");
						int numberOfDice = Integer.parseInt(tokens[1].trim());
						int diceSize = Integer.parseInt(tokens[2].trim());
						int damage = Integer.parseInt(tokens[3].trim());
						int duration = 0;
						for (int i = 0; i < numberOfDice; i++) {
							duration += RandomGen.rand(1, diceSize);
						}
						Poison poison = new Poison(damage, duration, "Poison");
						effects().add(poison);
					}
                }
            }
        }
        else if(attribute.equals("to hit bonus")){
		    setToHitBonus(Integer.parseInt(value));
        }
        else if(attribute.equals("danger")){
        	setDanger(Integer.parseInt(value));
		}
        else if(attribute.equals("damage")){
            setDamageValue(Integer.parseInt(value));
        }
        else if(attribute.equals("damage bonus")){
            setDamageBonus(Integer.parseInt(value));
        }
        else if(attribute.equals("dodge")){
            setDodgeValue(Integer.parseInt(value));
        }
        else if(attribute.equals("armor")){
            setArmorValue(Integer.parseInt(value));
        }
        else if(attribute.equals("weight")){
            setWeight(Double.parseDouble(value));
        }
        else if(attribute.equals("range")){
            setRange(Integer.parseInt(value));
        }
        else if(attribute.equals("dice rolled")){
            setNumberOfDiceRolled(Integer.parseInt(value));
        }
	}

	public void setSpell(String spellName){
		factory = new Factory();
		spell = factory.getSpell(spellName);
	}

	public List <Effect> effects(){ return this.effects; }
	public void addEffect(Effect effect){ this.effects.add(effect); }
	
	public int getToHitBonus(){ return this.toHitBonus; }
	public void setToHitBonus(int bonus){ this.toHitBonus = bonus; }
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

	public int getRange(){ return this.range; }
	public void setRange(int range){ this.range = range; }
	public void modifyRange(int modification){ this.range += modification; }

	public int damageBonus(){ return this.damageBonus; }
	public void setDamageBonus(int bonus){ this.damageBonus = bonus; }
	public void modifyDamageBonus(int modification){ this.damageBonus += modification; }

	public int numberOfDiceRolled(){ return this.numberOfDiceRolled; }
	public void setNumberOfDiceRolled(int num){ this.numberOfDiceRolled = num; }
	public void modifyNumberOfDiceRolled(int modification){ this.numberOfDiceRolled += modification; }

	@Override
	public int compareTo(Item otherItem){
		return this.name().compareToIgnoreCase(otherItem.name());
	}
}
