package roguelike.modifiers;

import roguelike.mob.BaseEntity;
import roguelike.utility.RandomGen;

public class Invisibility extends Effect{

    public Invisibility(String eType, String onCast, String onUpdate, String onEnd, int numOfDice, int diceSize){
        super(eType, onCast, onUpdate, onEnd);
        setDurationDice(numOfDice);
        setDurationDiceSize(diceSize);
    }

    @Override
    public boolean isDone() { return duration() < 1; }

    @Override
    public void update(BaseEntity entity) {
        subtractFromDuration(1);
    }

    @Override
    public void start(BaseEntity entity) {
        for(int i = 0; i < getDurationDice(); i++){
            addToDuration(RandomGen.rand(1, getDurationDice()));
        }
        entity.doAction(getOnCast());
        entity.setInvisible(true);
    }

    @Override
    public void end(BaseEntity entity) {
        entity.doAction(getOnEnd());
        entity.setInvisible(false);
    }
}
