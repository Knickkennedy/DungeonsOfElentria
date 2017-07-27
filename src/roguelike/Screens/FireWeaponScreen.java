package roguelike.screens;

import roguelike.mob.BaseEntity;
import roguelike.mob.Player;
import roguelike.level.Tile;
import squidpony.squidmath.Coord;

public class FireWeaponScreen extends TargetingScreen{

	public FireWeaponScreen(Player player) {
		super(player);
	}

	@Override
	public boolean isAcceptable(Coord target) {
            if((x == player.x) && (y == player.y)){
                player.notify("You wouldn't want to hurt yourself, would you?");
                return false;
            }
            else {
                return true;
            }
    }

	@Override
	public void selectCoordinate(Coord[] target) {
		for(int i = 0; i < target.length; i++) {
		    if(isAcceptable(target[i])) {
                if ((player.getLevel().glyph(target[i].x, target[i].y) == Tile.DOOR_CLOSED.glyph())
                        || (player.getLevel().glyph(target[i].x, target[i].y) == Tile.WALL.glyph())
                        || (player.getLevel().glyph(target[i].x, target[i].y) == Tile.PERM_WALL.glyph())) {
                    player.notify("You hit a %s.", player.getLevel().tile(target[i].x, target[i].y).details());
                    player.consumeEquippedAmmunition(target[i - 1].x, target[i - 1].y);
                    break;
                } else if (player.getLevel().checkForMob(target[i].x, target[i].y) != null) {
                    BaseEntity otherEntity = player.getLevel().checkForMob(target[i].x, target[i].y);
                    player.rangedAttack(otherEntity);
                    break;
                }
                else if(player.getLevel().checkForMob(target[i].x, target[i].y) == null && i == target.length - 1){
                    player.consumeEquippedAmmunition(target[i].x, target[i].y);
                    player.notify("You hit nothing.");
                }
            }
            else{
		        break;
            }
        }
	}
}
