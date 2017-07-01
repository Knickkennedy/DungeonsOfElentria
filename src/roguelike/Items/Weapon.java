package roguelike.Items;

import java.awt.Color;

public class Weapon extends BaseItem{
    private int damageBonus, numberOfDiceRolled, range;

    public Weapon(String name, char glyph, Color color, String itemType, double weight, int toHit, int attackBonus){
        super(name, glyph, color, itemType, weight);
        this.setDamageBonus(attackBonus);
        this.setToHit(toHit);
    }

    public int getRange(){ return this.range; }
    public void setRange(int range){ this.range = range; }
    public void modifyRange(int modification){ this.range += modification; }

    public int damageBonus(){ return this.damageBonus; }
    public void setDamageBonus(int bonus){ this.damageBonus = bonus; }
    public void modifyDamageBonus(int modification){ this.damageBonus += modification; }

    public int numberOfDiceRolled(){ return this.numberOfDiceRolled; }
    public void setNumberOfDiceRolled(int num){ this.numberOfDiceRolled = num; }
    public void modifyNumberOfDiceRolled(int modification){ this.numberOfDiceRolled += modification; }

    public String toString(){
        return "Name: " + name() + "\nDamage: " + damageValue() + "\nBonus Damage: " + damageBonus() + "\nTo Hit Bonus: "
                + toHit() + "\nNumber of Dice: " + numberOfDiceRolled();
    }

}
