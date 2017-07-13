package roguelike.Mob;

import roguelike.modifiers.Effect;

/**
 * Created by knich on 7/13/2017.
 */
public class Spell {
    private String name;
    private int manaCost;
    private Effect effect;

    public Spell(String name, int manaCost, Effect effect){
        setName(name);
        setManaCost(manaCost);
        setEffect(effect);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }
}
