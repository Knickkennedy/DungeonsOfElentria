package roguelike.AI;

import java.util.*;
import roguelike.level.Level;
import roguelike.mob.BaseEntity;

public class playerAI extends BaseAI{
	private List <String> messages;
	public playerAI(BaseEntity mob, List <String> messages){
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
