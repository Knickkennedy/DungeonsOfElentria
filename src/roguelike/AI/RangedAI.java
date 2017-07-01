package roguelike.AI;

import roguelike.Mob.Player;
import roguelike.Mob.EnemyEntity;

public class RangedAI extends BaseAI{

    public Player player;
    private boolean hasSeenPlayer;

    public RangedAI(EnemyEntity mob){
        super(mob);
        hasSeenPlayer = false;
    }

    public void onUpdate(){
        this.player = mob.level().player;
        if(canSee(player.x, player.y) && !mob.isNextTo(player) && mob.isWithinRange(player)){
            hasSeenPlayer = true;
            mob.rangedAttack(player);
        }
        else if(hasSeenPlayer){ hunt(player); }
        else if(mob.isNextTo(player)){ hunt(player); }
        else if(mob.level().hasItemAlready(mob.x, mob.y)){ mob.pickupItem(); }
        else{ wander(); }
    }






}
