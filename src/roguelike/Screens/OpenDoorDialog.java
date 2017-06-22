package roguelike.Screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;
import roguelike.Level.Level;
import roguelike.levelBuilding.Door;

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
		terminal.writeCenter("Open this door? (y/n)", 28);
	}

	@Override
	public Screen respondToUserInput(KeyEvent key) {
		switch(key.getKeyChar()){
		case 'y':{
			level.player.notify("You open the door.");
			for(Door d : level.doors){
				if(d.x == level.player.x + this.x && d.y == level.player.y + this.y){
					level.map[level.player.x + this.x][level.player.y + this.y] = d.open();
					level.setPathFinding();
				}
			}
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
