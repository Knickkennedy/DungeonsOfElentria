package roguelike.AI;

import roguelike.mob.Player;
import roguelike.mob.EnemyEntity;

public class RangedAI extends BaseAI{

    public Player player;
    private boolean hasSeenPlayer;

    public RangedAI(EnemyEntity mob){
        super(mob);
        hasSeenPlayer = false;
    }

    public void onUpdate(){
        this.player = mob.getLevel().player;
        if(canSee(player) && !mob.isNextTo(player) && mob.isWithinRange(player)){
            hasSeenPlayer = true;
            mob.rangedAttack(player);
        }
        else if(canSee(player) || hasSeenPlayer){ hunt(player); }
        else if(canSee(player) && mob.isNextTo(player)){ hunt(player); }
        else if(mob.getLevel().hasItemAlready(mob.x, mob.y)){ mob.pickupItem(); }
        else{ wander(); }
    }






}
