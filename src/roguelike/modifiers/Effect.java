package roguelike.modifiers;

import roguelike.Mob.BaseEntity;

public class Effect implements EffectInterface{
    private int duration;

    public Effect(int dur){
        this.duration = dur;
    }

    public int duration(){ return duration; }
    public void updateDuration(int update){ duration -= update; }

    public void start(BaseEntity entity){}
    public void update(BaseEntity entity){}
    public void end(BaseEntity entity){}

    public boolean isDone(){
        return duration < 1;
    }
}
