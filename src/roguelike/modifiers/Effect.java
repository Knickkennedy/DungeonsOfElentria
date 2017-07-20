package roguelike.modifiers;

import roguelike.Mob.BaseEntity;
import roguelike.utility.RandomGen;

public class Effect implements EffectInterface{
    private int duration, chanceToProc;
    private String effectType;

    public Effect(int dur, String eType){
        this.duration = dur;
        this.effectType = eType;
    }

    public Effect(String eType, int durDice, int durSize){
        setEffectType(eType);
        setDuration(durDice, durSize);
    }

    public void setEffectType(String type){ this.effectType = type; }
    public String getEffectType(){ return this.effectType; }

    public void setChanceToProc(int chance){ this. chanceToProc = chance; }
    public int getChanceToProc(){ return this.chanceToProc; }

    public int duration(){ return duration; }
    public void setDuration(int numOfDice, int diceSize){
        for(int i = 0; i < numOfDice; i++){
            duration += RandomGen.rand(1, diceSize);
        }
    }
    public void updateDuration(int update){ duration -= update; }

    public void start(BaseEntity entity){}
    public void update(BaseEntity entity){}
    public void end(BaseEntity entity){}

    public boolean isDone(){
        return duration < 1;
    }
}
