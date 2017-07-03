package roguelike.Mob;

import java.awt.Color;
import java.util.HashMap;

import asciiPanel.AsciiPanel;
import roguelike.AI.AggressiveAI;
import roguelike.AI.RangedAI;
import roguelike.Level.Level;
import roguelike.modifiers.*;

public class EnemyEntity extends BaseEntity{

    public HashMap<String, Color> colorDictionary = new HashMap <> ();

	public EnemyEntity(Level level, char glyph, Color color){ 
		super(level, glyph, color);
		setMaxCarryWeight(9999);
		setInventory(this);
		setVisionRadius(5);
	}

	public EnemyEntity(Level level){
	    super(level);
	    initializeColors();
	    setMaxCarryWeight(9999);
	    setInventory(this);
	    setVisionRadius(5);
    }

    public void initializeColors(){
        colorDictionary.put("brightGreen", AsciiPanel.brightGreen);
        colorDictionary.put("blue", AsciiPanel.brightBlue.brighter().brighter().brighter());
        colorDictionary.put("brightWhite", AsciiPanel.brightWhite);
        colorDictionary.put("darkGreen", AsciiPanel.green.brighter());
        colorDictionary.put("brown", new Color(102, 51, 0));
        colorDictionary.put("gray", new Color(130, 130, 130));
        colorDictionary.put("white", AsciiPanel.white);
    }

	public void setAttributes(String attribute, String value){
	    if(attribute.equals("name")){
	        setName(value);
        }
        else if(attribute.equals("symbol")){
	        setGlyph(value.charAt(0));
        }
        else if(attribute.equals("color")){
            setColor(colorDictionary.get(value));
        }
        else if(attribute.equals("AI")){
            if(value.equals("aggressive")){
                new AggressiveAI(this);
            }
            else if(value.equals("ranged")){
                new RangedAI(this);
            }
        }
        else if(attribute.equals("effects")){
            if(value.equals("none")){
                return;
            }
            else{
                String effects[] = value.split(", ");
                for(String effect : effects){
                    if(effect.contains("poison")){
                        String tokens[] = effect.split(" - ");
                        Poison newPoison = new Poison(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
                        newPoison.setChanceToProc(Integer.parseInt(tokens[3].trim()));
                        addOffensiveEffect(newPoison);
                    }
                }
            }
        }
        else if(attribute.equals("health")){
            setMaxHP(Integer.parseInt(value));
        }
        else if(attribute.equals("mana")){
            setMaxMana(Integer.parseInt(value));
        }
        else if(attribute.equals("health regen")){
            setHealthRegenRate(Integer.parseInt(value));
        }
        else if(attribute.equals("mana regen")){
            setManaRegenRate(Integer.parseInt(value));
        }
        else if(attribute.equals("damage")){
            setAttack(Integer.parseInt(value));
        }
        else if(attribute.equals("armor")){
            setArmor(Integer.parseInt(value));
        }
        else if(attribute.equals("dodge")){
            setDodge(Integer.parseInt(value));
        }
        else if(attribute.equals("range")){
            setRange(Integer.parseInt(value));
        }
        else if(attribute.equals("ranged damage")){
            setRangedDamage(Integer.parseInt(value));
        }
    }
}
