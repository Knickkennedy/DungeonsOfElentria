package roguelike.AI;

import roguelike.levelBuilding.*;
import roguelike.utility.*;
import squidpony.squidai.DijkstraMap;
import squidpony.squidmath.Coord;

import java.util.ArrayList;

import roguelike.Level.Level;
import roguelike.Mob.BaseEntity;

public class BaseAI {
	protected BaseEntity mob;
	public Door door;
	public DijkstraMap path;
	public ELOS los;
	
	public BaseAI(BaseEntity mob){
		this.mob = mob;
		this.mob.setMobAi(this);
	}

	public void initializePathFinding(){
		path = new DijkstraMap(mob.level().pathMap, DijkstraMap.Measurement.EUCLIDEAN);
		los = new ELOS();
	}

	public boolean canSee(int wx, int wy) {
		int radius = (mob.x - wx)*(mob.x - wx) + (mob.y - wy)*(mob.y - wy);
		if(radius > mob.visionRadius()*mob.visionRadius()) {
			return false;
		}
		else if(los.isReachable(mob.level().pathMap, mob.x, mob.y, wx, wy)) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean canSee(BaseEntity entity) {
		int radius = (mob.x - entity.x)*(mob.x - entity.x) + (mob.y - entity.y)*(mob.y - entity.y);
		if(entity.isInvisible()) return false;
		if(radius > mob.visionRadius()*mob.visionRadius()) return false;
		else if(los.isReachable(mob.level().pathMap, mob.x, mob.y, entity.x, entity.y)) return true;
		else return false;
	}

	public void hunt(BaseEntity target){
		path.setGoal(target.x, target.y);
		ArrayList <Coord> coords = new ArrayList <Coord> ();
		ArrayList <Coord> blocked = new ArrayList <Coord> ();
		
		for(BaseEntity entity : mob.level().mobs) {
			if(!entity.isPlayer()) {
				blocked.add(Coord.get(entity.x, entity.y));
			}
		}
		
		coords = path.findPath(8, blocked, null, Coord.get(mob.x, mob.y), Coord.get(target.x, target.y));
		
		if(coords == null || coords.isEmpty()) { return; }
		int myX = coords.get(0).x - mob.x;
		int myY = coords.get(0).y - mob.y;
		
		mob.move(myX, myY);
	}

	public void skipTurn(){
		return;
	}

	public void wander(){
		int x = RandomGen.rand(-1, 1);
		int y = RandomGen.rand(-1, 1);
		BaseEntity otherEntity = mob.level().checkForMob(mob.x + x, mob.y + y);
		if(otherEntity != null || !mob.level().isGround(mob.x + x, mob.y + y)){
			return;
		}
		else{
			mob.move(x, y);
		}	
	}
	
	public void onNotify(String message){}
	
	public void onEnter(int x, int y, Level level){
		if(level.isGround(x, y)){
			mob.x = x;
			mob.y = y;
		}
		else{
			return;
		}
	}
	
	public void onUpdate(){}
	
}
