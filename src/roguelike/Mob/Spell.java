package roguelike.Mob;

import roguelike.modifiers.Effect;
import roguelike.utility.RandomGen;

public class Spell {
    private String name;
    private int manaCost;
    private int range;
    private boolean fireDamage;
    private boolean magicDamage;
    private boolean healing;
    private boolean poison;
    private boolean selfTargeting;
    private boolean reflective;

    private Effect effect;

    public Spell(String[] parameters, String[] input){
        setParameters(parameters, input);
    }

    public void setParameters(String[] parameters, String[] input){
        for(int i = 0; i < parameters.length; i++){
            switch(parameters[i]){
                case "NAME":{
                    setName(input[i].trim());
                    break;
                }
                case "TYPE":{
                    setSpellType(input[i].trim(), input[i + 1].trim(), input[i + 2].trim());
                    i++;
                    i++;
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
                case "SELF":{
                    if(input[i].trim().equalsIgnoreCase("yes")){
                        setSelfTargeting(true);
                    }
                    break;
                }
                case "REFL":{
                    if(input[i].trim().equalsIgnoreCase("yes")){
                        setReflective(true);
                    }
                    break;
                }
            }
        }
        /*setName(input[0].trim());
        setSpellType(input[1].trim(), input[2].trim(), input[3].trim());
        setManaCost(Integer.parseInt(input[4].trim()));
        setRange(Integer.parseInt(input[5].trim()));
        if(input[6].trim().equalsIgnoreCase("yes")){
            setSelfTargeting(true);
        }
        if(input[7].trim().equalsIgnoreCase("yes")){
            setReflective(true);
        }*/
    }

    public void setSpellType(String type, String duration, String dice){
        String[] diceStats = dice.split("D");
        int numberOfDice = Integer.parseInt(diceStats[0]);
        int diceSize = Integer.parseInt(diceStats[1]);

        String[] dur = duration.split("D");
        int durDice = 1;
        int durSize = 1;
        int totalDuration = 0;
        if(dur.length > 1){
            durDice = Integer.parseInt(dur[0]);
            durSize = Integer.parseInt(dur[1]);
        }
        switch (type){
            case "MAGIC":{
                setMagicDamage(true);
                this.effect = new Effect(type, durDice, durSize){
                    @Override
                    public void start(BaseEntity entity){
                        if(entity.isPlayer()){
                            entity.doAction("sizzle!");
                            int damage = 0;
                            for(int i = 0; i < numberOfDice; i++){
                                damage += RandomGen.rand(1, diceSize);
                            }
                            entity.modifyHP(-damage, "by overpowering magical energies");
                        }
                        else{
                            entity.doAction("The %s sizzles!", entity.name());
                            int damage = 0;
                            for(int i = 0; i < numberOfDice; i++){
                                damage += RandomGen.rand(1, diceSize);
                            }
                            entity.modifyHP(-damage, "by overpowering magical energies");
                        }
                    }
                };
                break;
            }
            case "FIRE":{
                setFireDamage(true);
                this.effect = new Effect(type, durDice, durSize){
                    @Override
                    public void start(BaseEntity entity){
                        if(entity.isPlayer()){
                            entity.doAction("burn!");
                            int damage = 0;
                            for(int i = 0; i < numberOfDice; i++){
                                damage += RandomGen.rand(1, diceSize);
                            }
                            entity.modifyHP(-damage, "burned to a crisp");
                        }
                        else{
                            entity.doAction("The %s burns!", entity.name());
                            int damage = 0;
                            for(int i = 0; i < numberOfDice; i++){
                                damage += RandomGen.rand(1, diceSize);
                            }
                            entity.modifyHP(-damage, "burned to a crisp");
                        }
                    }
                };
                break;
            }
            case "HEALING":{
                setHealing(true);
                this.effect = new Effect(type, durDice, durSize){
                    @Override
                    public void start(BaseEntity entity){
                        if(entity.isPlayer()){
                            entity.doAction("feel relieved!");
                            int healing = 0;
                            for(int i = 0; i < numberOfDice; i++){
                                healing += RandomGen.rand(1, diceSize);
                            }
                            entity.modifyHP(healing, "by overhealing?");
                        }
                        else{
                            entity.doAction("The %s looks relieved!", entity.name());
                            int healing = 0;
                            for(int i = 0; i < numberOfDice; i++){
                                healing += RandomGen.rand(1, diceSize);
                            }
                            entity.modifyHP(healing, "by overhealing?");
                        }
                    }
                };
                break;
            }
            case "POISON":{
                setPoison(true);
                this.effect = new Effect(type, durDice, durSize){
                    @Override
                    public void start(BaseEntity entity){
                        if(entity.isPlayer()){
                            entity.doAction("are poisoned!");
                            int damage = 0;
                            for(int i = 0; i < numberOfDice; i++){
                                damage += RandomGen.rand(1, diceSize);
                            }
                            entity.modifyHP(-damage, "with virulent poison through your veins");
                        }
                        else{
                            entity.doAction("The %s is poisoned!", entity.name());
                            int damage = 0;
                            for(int i = 0; i < numberOfDice; i++){
                                damage += RandomGen.rand(1, diceSize);
                            }
                            entity.modifyHP(-damage, "with virulent poison in their veins");
                        }
                    }
                };
                break;
            }
        }
    }

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
