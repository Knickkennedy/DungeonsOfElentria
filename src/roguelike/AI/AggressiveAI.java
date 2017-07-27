package roguelike.AI;

import roguelike.mob.EnemyEntity;
import roguelike.mob.Player;

public class AggressiveAI extends BaseAI{
	
	public Player player;
	private boolean hasSeenPlayer;
	
	public AggressiveAI(EnemyEntity mob){
		super(mob);
		hasSeenPlayer = false;
	}
	
	public void onUpdate(){
		this.player = mob.getLevel().player;
		if(this.canSee(player) || hasSeenPlayer){
			hunt(player);
			hasSeenPlayer = true;
		}
		else if(mob.getLevel().hasItemAlready(mob.x, mob.y)){ mob.pickupItem(); }
		else{
			wander();
		}
	}
}
