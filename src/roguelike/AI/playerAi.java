package roguelike.AI;

import java.util.*;
import roguelike.Level.Level;
import roguelike.Mob.BaseEntity;

public class playerAi extends BaseAI{
	private List <String> messages;
	public playerAi(BaseEntity mob, List <String> messages){
		super(mob);
		this.messages = messages;
	}

	public void onNotify(String message){
		messages.add(message);
	}
	
	public void onEnter(int x, int y, Level level){
		if(level.isGround(x, y)){
			mob.x = x;
			mob.y = y;
		}
		else{
			return;
		}
	}
}
