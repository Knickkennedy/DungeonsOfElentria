package roguelike.Mob;

import roguelike.modifiers.Effect;

public class Spell {
    private String name;
    private int manaCost;
    private int range;
    private boolean selfTargeting;

    private boolean reflective;

    private Effect effect;

    public Spell(String name, Effect effect, int manaCost, int range, boolean selfTargeting, boolean reflective){
        setName(name);
        setManaCost(manaCost);
        setEffect(effect);
        setRange(range);
        setSelfTargeting(selfTargeting);
        setReflective(reflective);
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

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public boolean isSelfTargeting() {
        return selfTargeting;
    }

    public void setSelfTargeting(boolean selfTargeting) {
        this.selfTargeting = selfTargeting;
    }

    public boolean isReflective() {
        return reflective;
    }

    public void setReflective(boolean reflective) {
        this.reflective = reflective;
    }

}
