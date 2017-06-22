package roguelike.Items;

import java.awt.Color;

import roguelike.modifiers.*;
import roguelike.utility.RandomGen;

public class Potion extends BaseItem{
	
	public Potion(String name, char glyph, Color color, String itemType, double weight, String effectName){
		super(name, glyph, color, itemType, weight);
		setEffect(effectName);
	}
	
	public void setEffect(String effectName){
		if(effectName.equals("weak poison")){
			int totalLength = 0;
			for(int i = 0; i < 4; i++){
				int roll = RandomGen.rand(1, 3);
				totalLength += roll;
			}
			setEffect(new Poison(1, totalLength));
		}
		else if(effectName.equals("strong poison")){
			int totalLength = 0;
			for(int i = 0; i < 4; i++){
				int roll = RandomGen.rand(1, 3);
				totalLength += roll;
			}
			setEffect(new Poison(2, totalLength));
		}
		else if(effectName.equals("weak healing")){
			int totalHealing = 0;
			for(int i = 0; i < 4; i++){
				int roll = RandomGen.rand(1, 3);
				totalHealing += roll;
			}
			setEffect(new Healing(totalHealing, 1));
		}
		else if(effectName.equals("strong healing")){
			int totalHealing = 0;
			for(int i = 0; i < 8; i++){
				int roll = RandomGen.rand(1, 3);
				totalHealing += roll;
			}
			setEffect(new Healing(totalHealing, 1));
		}
	}
}
