package roguelike.AI;

import roguelike.Mob.EnemyEntity;
import roguelike.Mob.Player;

public class AggressiveAI extends BaseAI{
	
	public Player player;
	private boolean hasSeenPlayer;
	
	public AggressiveAI(EnemyEntity mob){
		super(mob);
		hasSeenPlayer = false;
	}
	
	public void onUpdate(){
		this.player = mob.level().player;
		if(this.canSee(player) || hasSeenPlayer){
			hunt(player);
			hasSeenPlayer = true;
		}
		else if(mob.level().hasItemAlready(mob.x, mob.y)){ mob.pickupItem(); }
		else{
			wander();
		}
	}
}
