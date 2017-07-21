package roguelike.Mob;

import roguelike.modifiers.Effect;
import roguelike.utility.RandomGen;

public class Spell {
    private String name;
    private String castType;
    private String castEffect;
    private String onCast;
    private String deathMessage;
    private int manaCost;
    private int range;
    private int[] duration = new int[2];
    private int[] diceStats = new int[2];
    private boolean fireDamage;
    private boolean magicDamage;
    private boolean healing;
    private boolean poison;
    private boolean reflective;
    private Effect effect;

    public Spell(String[] parameters, String[] input){
        setParameters(parameters, input);
        setEffect();
    }

    public void setParameters(String[] parameters, String[] input){
        for(int i = 0; i < parameters.length; i++){
            switch(parameters[i]){
                case "NAME":{
                    setName(input[i].trim());
                    break;
                }
                case "TYPE":{
                    setSpellType(input[i].trim());
                    break;
                }
                case "MANA":{
                    setManaCost(Integer.parseInt(input[i].trim()));
                    break;
                }
                case "RANGE":{
                    setRange(Integer.parseInt(input[i].trim()));
                    break;
                }
                case "DUR":{
                    setDuration(input[i].trim());
                    break;
                }
                case "DICE":{
                    setDiceStats(input[i].trim());
                    break;
                }
                case "CAST":{
                    setCastType(input[i].trim());
                    break;
                }
                case "REFL":{
                    if(input[i].trim().equalsIgnoreCase("yes")){
                        setReflective(true);
                    }
                    break;
                }
                case "EFFECT":{
                    setCastEffect(input[i].trim());
                    break;
                }
                case "ON-CAST":{
                    setOnCastString(input[i].trim());
                }
                case "DEATH":{
                    setDeathMessage(input[i].trim());
                }
            }
        }
    }

    public void setOnCastString(String onCast){
        this.onCast = onCast;
    }

    public void setDeathMessage(String death){
        this.deathMessage = death;
    }

    public void setEffect(){
        int eDice = this.diceStats[0];
        int eSize = this.diceStats[1];
        String onCast = this.onCast;
        String deathMessage = this.deathMessage;
        switch(this.castEffect){
            case "DAMAGE":{
                this.effect = new Effect(this.castType, this.duration[0], this.duration[1]){
                    @Override
                    public void start(BaseEntity entity){
                        int effectValue = 0;
                        entity.doAction(onCast);
                        for(int i = 0; i < eDice; i++){
                            effectValue += RandomGen.rand(1, eSize);
                        }
                        entity.modifyHP(-effectValue, deathMessage);
                    }
                };
                break;
            }
            case "HEALING":{
                this.effect = new Effect(this.castType, this.duration[0], this.duration[1]) {
                    @Override
                    public void start(BaseEntity entity) {
                        int effectValue = 0;
                        if(entity.isPlayer()){
                            entity.doAction("feel " + onCast);
                        }
                        else {
                            entity.doAction("look " + onCast);
                        }
                        for(int i = 0; i < eDice; i++){
                            effectValue += RandomGen.rand(1, eSize);
                        }
                        entity.modifyHP(effectValue, deathMessage);
                    }
                };
                break;
                }
            case "INVISIBILITY":{
                this.effect = new Effect(this.castType, this.duration[0], this.duration[1]) {
                    @Override
                    public void start(BaseEntity entity) {
                        entity.doAction(onCast);
                        entity.setInvisible(true);
                    }

                    @Override
                    public void end(BaseEntity entity){
                        entity.doAction("fade back into existence");
                        entity.setInvisible(false);
                    }
                };
                break;
            }
            }
        }

    public void setCastEffect(String castEffect) {
        this.castEffect = castEffect;
    }

    public void setDiceStats(String values){
        String[] diceStats = values.split("D");
        this.diceStats[0] = Integer.parseInt(diceStats[0]);
        this.diceStats[1] = Integer.parseInt(diceStats[1]);
    }

    public void setDuration(String values){
        String[] dur = values.split("D");
        if(dur.length > 1){
            this.duration[0] = Integer.parseInt(dur[0]);
            this.duration[1] = Integer.parseInt(dur[1]);
        }
        else{
            this.duration[0] = 1;
            this.duration[1] = 1;
        }
    }

    public int[] getDuration(){ return this.duration; }

    public void setSpellType(String type){
        switch (type){
            case "MAGIC":{
                setMagicDamage(true);
                break;
            }
            case "FIRE":{
                setFireDamage(true);
                break;
            }
            case "HEALING":{
                setHealing(true);
                break;
            }
            case "POISON":{
                setPoison(true);
                break;
            }
        }
    }

    public void setCastType(String type){ this.castType = type; }
    public String getCastType(){ return this.castType; }

    public void setPoison(boolean poison){ this.poison = poison; }
    public boolean isPoison(){ return this.poison; }

    public void setHealing(boolean healing){ this.healing = healing; }
    public boolean isHealing(){ return this.healing; }

    public void setFireDamage(boolean damage){ this.fireDamage = damage; }
    public boolean isFireDamage(){ return this.fireDamage; }

    public void setMagicDamage(boolean damage){ this.magicDamage = damage; }
    public boolean isMagicDamage(){ return this.magicDamage; }

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

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public boolean isReflective() {
        return reflective;
    }

    public void setReflective(boolean reflective) {
        this.reflective = reflective;
    }

}
