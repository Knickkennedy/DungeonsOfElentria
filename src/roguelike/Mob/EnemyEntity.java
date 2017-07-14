package roguelike.Mob;

import roguelike.AI.*;
import roguelike.Items.Item;
import roguelike.World.Factory;
import roguelike.Level.Level;
import roguelike.modifiers.*;
import roguelike.utility.RandomGen;

public class EnemyEntity extends BaseEntity{

    private Factory itemStore;

	public EnemyEntity(Level level){
	    super(level);
	    itemStore = new Factory(level);
	    setMaxCarryWeight(9999);
	    setInventory(this);
	    setVisionRadius(5);
    }

	public void setAttributes(String attribute, String value){
	    if(attribute.equals("name")){
	        setName(value);
        }
        else if(attribute.equals("experience")){
                setExperience(Integer.parseInt(value));
        }
        else if(attribute.equals("symbol")){
	        setGlyph(value.charAt(0));
        }
        else if(attribute.equals("color")){
            setColor(Colors.getColor(value));
        }
        else if(attribute.equals("AI")){
            if(value.equals("aggressive")){
                new AggressiveAI(this);
            }
            else if(value.equals("ranged")){
                new RangedAI(this);
            }
            else if(value.equals("Elena")){
                new ElenaAI(this);
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
                        Poison newPoison = new Poison(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]), "Poison");
                        newPoison.setChanceToProc(Integer.parseInt(tokens[3].trim()));
                        addOffensiveEffect(newPoison);
                    }
                }
            }
        }
        else if(attribute.equals("drops")){
            String dropArray[];
            String drops[] = value.split(", ");
            int chances = 0;
            double odds = 0.0;
            for(String drop : drops){
                dropArray = drop.split(" - ");
                chances = Integer.parseInt(dropArray[1].trim());
                odds = Double.parseDouble(dropArray[2].trim());
                for(int i = 0; i < chances; i++){
                    double check = RandomGen.dRand(0.0, 100.0);
                    if(check < odds){
                        if(dropArray[0].trim().equals("dangerOneItems")){
                            int roll = RandomGen.rand(1, itemStore.dangerOneItems.size());
                            inventory().add(itemStore.newItem(itemStore.dangerOneItems.get(roll)));
                        }
                        else if(dropArray[0].trim().equals("dangerTwoItems")){
                            int roll = RandomGen.rand(1, itemStore.dangerTwoItems.size());
                            inventory().add(itemStore.newItem(itemStore.dangerTwoItems.get(roll)));
                        }
                        else if(dropArray[0].trim().equals("dangerThreeItems")){
                            int roll = RandomGen.rand(1, itemStore.dangerThreeItems.size());
                            inventory().add(itemStore.newItem(itemStore.dangerThreeItems.get(roll)));
                        } else if(dropArray[0].trim().contains(":")){
                            String[] temp = dropArray[0].trim().split(":");
                            String itemType = temp[0].trim();
                            int lowerEnd = Integer.parseInt(temp[1].trim());
                            int upperEnd = Integer.parseInt(temp[2].trim());
                            Item tempItem;
                            do {
                                int roll = RandomGen.rand(0, itemStore.itemsByCategory.get(itemType).size() - 1);
                                tempItem = itemStore.newItem(itemStore.itemsByCategory.get(itemType).get(roll));
                            }
                            while(tempItem.getDanger() < lowerEnd && tempItem.getDanger() > upperEnd);
                            inventory().add(tempItem);
                        }
                        else {
                            inventory().add(itemStore.newItem(dropArray[0].trim()));
                        }
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
