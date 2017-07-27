package roguelike.items;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import roguelike.mob.BaseEntity;
import roguelike.mob.Colors;
import roguelike.mob.Spell;
import roguelike.world.Factory;
import roguelike.modifiers.Effect;
import roguelike.modifiers.Healing;
import roguelike.modifiers.Poison;

public @Data class Item implements Comparable <Item>{
	private String name, appearance, itemType, onUse, onUpdate, onEnd;
	private char glyph;
	private Color color;
	private int armorValue, dodgeValue, toHitBonus, damageValue, damageBonus, numberOfDiceRolled, range, danger;
	private double weight;
	public int x, y;
	private List<Effect> effects = new ArrayList<Effect>();
	private Spell spell;
	private Factory factory;
	
	public Item(){}

	public String details(){ return getName(); }

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
		else if(attribute.equalsIgnoreCase("on use")){
        	setOnUse(value.trim());
		}
		else if(attribute.equalsIgnoreCase("on update")){
			setOnUpdate(value.trim());
		}
		else if(attribute.equalsIgnoreCase("on end")){
			setOnEnd(value.trim());
		}
        else if(attribute.equals("effects")){
		    String effects[] = value.split(", ");
		    for(String effect : effects){
		        if(effect.contains("healing")){
		            String tokens[] = effect.split(" - ");
		            String[] durStats = tokens[1].trim().split("D");
		            String[] effectStats = tokens[2].trim().split("D");
                    Healing heal = new Healing("Healing", onUse, onUpdate, onEnd, Integer.parseInt(durStats[0]), Integer.parseInt(durStats[1]), Integer.parseInt(effectStats[0]), Integer.parseInt(effectStats[1]));
                    effects().add(heal);
                }
                else if(effect.contains("poison")){
		        	if(effect.equals("cure poison")){
		        		List <Effect> tempEffects = new ArrayList<>();
		        		Effect newEffect = new Effect(1, "Cure Poison"){
		        			public void start(BaseEntity entity){
		        				for(Effect effect : entity.getEffects()){
		        					if(effect.getEffectType().equals("Poison")){
		        						tempEffects.add(effect);
									}
								}
								for(Effect effect : tempEffects){
		        					entity.getEffects().remove(effect);
								}
							}
						};
						effects().add(newEffect);
					}
					else {
							String tokens[] = effect.split(" - ");
							String[] durStats = tokens[1].trim().split("D");
							String[] effectStats = tokens[2].trim().split("D");
							Poison poison = new Poison("Poison", onUse, onUpdate, onEnd, Integer.parseInt(durStats[0]), Integer.parseInt(durStats[1]), Integer.parseInt(effectStats[0]), Integer.parseInt(effectStats[1]));
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

	public Spell getSpell(){ return this.spell; }

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
		return this.getName().compareToIgnoreCase(otherItem.getName());
	}
}
