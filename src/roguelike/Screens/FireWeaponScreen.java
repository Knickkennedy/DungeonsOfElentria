package roguelike.Screens;

import roguelike.Mob.BaseEntity;
import roguelike.Mob.Player;
import roguelike.levelBuilding.Tile;
import squidpony.squidmath.Coord;

public class FireWeaponScreen extends TargetingScreen{

	public FireWeaponScreen(Player player) {
		super(player);
	}

	@Override
	public boolean isAcceptable(Coord target) {
        if (player.canSee(target.x, target.y)) {
            if((x == player.x) && (y == player.y)){
                player.notify("You wouldn't want to hurt yourself, would you?");
                return false;
            }
            else {
                return true;
            }
        } else {
            player.notify("You shouldn't shoot where you can't see!");
            return false;
        }
    }
	
	@Override
	public void selectCoordinate(Coord[] target) {
		for(int i = 0; i < target.length; i++) {
		    if(isAcceptable(target[i])) {
                if ((player.level().glyph(target[i].x, target[i].y) == Tile.DOOR_CLOSED.glyph())
                        || (player.level().glyph(target[i].x, target[i].y) == Tile.WALL.glyph())
                        || (player.level().glyph(target[i].x, target[i].y) == Tile.PERM_WALL.glyph())) {
                    player.notify("You hit a %s.", player.level().tile(target[i].x, target[i].y).details());
                    break;
                } else if (player.level().checkForMob(target[i].x, target[i].y) != null) {
                    BaseEntity otherEntity = player.level().checkForMob(target[i].x, target[i].y);
                    player.rangedAttack(otherEntity, player.getRangedWeapon());
                    break;
                }
                else if(player.level().checkForMob(target[i].x, target[i].y) == null && i == target.length - 1){
                    player.notify("You hit nothing.");
                }
            }
            else{
		        break;
            }
        }
	}
}
