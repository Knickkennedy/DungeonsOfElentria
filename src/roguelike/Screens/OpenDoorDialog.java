package roguelike.Screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;
import roguelike.Level.Level;
import roguelike.levelBuilding.Door;
import roguelike.levelBuilding.Tile;

public class OpenDoorDialog implements Screen{
	public int x, y;
	public Level level;
	
	public OpenDoorDialog(Level level, int x, int y){
		this.x = x;
		this.y = y;
		this.level = level;
	}
	@Override
	public void displayOutput(AsciiPanel terminal) {
		terminal.writeCenter("Open this door? (y/n)", PlayScreen.messageBuffer - 1);
	}

	@Override
	public Screen respondToUserInput(KeyEvent key) {
		switch(key.getKeyChar()){
		case 'y':{
			level.player.notify("You open the door.");
			level.map[x][y] = Tile.DOOR_OPEN;
			level.setPathFinding();
			break;
		}
		case 'n':{
			level.player.notify("You decide to not open the door."); 
		} break;
		}
		level.update();
		return null;
	}
	
}
