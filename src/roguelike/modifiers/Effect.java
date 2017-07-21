package roguelike.modifiers;

import roguelike.Mob.BaseEntity;
import roguelike.utility.RandomGen;

public class Effect implements EffectInterface{
    private int duration, effectValue;
    private double effectChance;
    private String effectType;
    private String onCast;
    private String onUpdate;
    private String onEnd;
    private String killMessage;
    private int durationDice;
    private int durationDiceSize;
    private int effectDice;
    private int effectDiceSize;

    public Effect(String eType, String onCast, String onUpdate, String onEnd){
        this.effectType = eType;
        setOnCast(onCast);
        setOnUpdate(onUpdate);
        setOnEnd(onEnd);
    }

    public Effect(int dur, String eType){
        this.duration = dur;
        this.effectType = eType;
    }

    public void setKillMessage(String value){ this.killMessage = value; }
    public String getKillMessage(){ return this.killMessage; }

    public void setEffectChance(double value){ this.effectChance = value; }
    public double getEffectChance(){ return this.effectChance; }

    public void setDurationDice(int value){ this.durationDice = value; }
    public void setDurationDiceSize(int value){ this.durationDiceSize = value; }
    public int getDurationDice(){ return this.durationDice; }
    public int getDurationDiceSize(){ return this.durationDiceSize; }
    public void setEffectDice(int value){ this.effectDice = value; }
    public void setEffectDiceSize(int value){ this.effectDiceSize = value; }
    public int getEffectDice(){ return this.effectDice; }
    public int getEffectDiceSize(){ return this.effectDiceSize; }

    public void setEffectValue(){
        effectValue = 0;
        for(int i = 0; i < effectDice; i++){
            effectValue += RandomGen.rand(1, effectDiceSize);
        }
    }
    public int getEffectValue(){ return this.effectValue; }

    public String getOnCast() { return onCast; }

    public void setOnCast(String onCast) { this.onCast = onCast; }

    public String getOnUpdate() { return onUpdate; }

    public void setOnUpdate(String onUpdate) { this.onUpdate = onUpdate; }

    public String getOnEnd() { return onEnd; }

    public void setOnEnd(String onEnd) { this.onEnd = onEnd; }

    public void setEffectType(String type){ this.effectType = type; }
    public String getEffectType(){ return this.effectType; }

    public int duration(){ return duration; }
    public void addToDuration(int amount){ this.duration += amount; }
    public void subtractFromDuration(int amount){ this.duration -= amount; }
    public void setDuration(){
        duration = 0;
        for(int i = 0; i < durationDice; i++){
            duration += RandomGen.rand(1, durationDiceSize);
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
