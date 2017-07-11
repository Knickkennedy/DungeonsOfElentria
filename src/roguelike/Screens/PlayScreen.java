package roguelike.Screens;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import asciiPanel.AsciiPanel;
import roguelike.World.World;
import roguelike.levelBuilding.Tile;
import roguelike.utility.Point;
import squidpony.squidgrid.FOV;

public class PlayScreen implements Screen {
	
	public Point currentDirection = new Point(0, 0);
	
	private int screenWidth;
	private int screenHeight;
	private int mapHeight;
	static int messageBuffer;
	private List <String> messages;
	private List <String> tempMessages;
	private Screen subscreen;
	private World world;
	private FOV fov;
	private double[][] resistanceMap;
	
	public PlayScreen(){
		screenWidth = 88;
		screenHeight = 32;
		this.messageBuffer = 4;
		mapHeight = screenHeight - 4 - messageBuffer;
		messages = new ArrayList <String> ();
		tempMessages = new ArrayList <String> ();
		world = new World(screenWidth, mapHeight, messages);
		fov = new FOV(FOV.SHADOW);
		}
	
	@Override
	public void displayOutput(AsciiPanel terminal) {
		displayTiles(terminal);
		displayMessages(terminal, messages);

		String health = String.format("Hp: %d/%d Mp: %d/%d", world.getPlayer().currentHP(), world.getPlayer().maxHP(), world.getPlayer().getCurrentMana(), world.getPlayer().getMaxMana());
		terminal.write(health, 0, mapHeight + messageBuffer + 1);
		String defensiveStats = String.format("DV:%d/AV:%d", world.getPlayer().dodge(), world.getPlayer().armor());
		terminal.write(defensiveStats, 0, mapHeight + messageBuffer + 2);

        String temp = "Depth: " + String.valueOf(world.getCurrentLevel().levelNumber) + " Danger: " + String.valueOf(world.getCurrentLevel().dangerLevel + " Experience Points: " + world.getPlayer().getExperience());
        terminal.write(temp, 0, mapHeight + messageBuffer);

        String help = "Press ? for help";
        terminal.write(help, screenWidth - help.length() - 1, screenHeight - 1);

		if (subscreen != null) {
			subscreen.displayOutput(terminal);
		}

	}
	
	public void displayMessages(AsciiPanel terminal, List <String> messages){
		for(int i = 0; i < messages.size() - 1; i++){
			if(messages.get(i).equals(messages.get(i + 1))){
				tempMessages.add(messages.get(i));
			}
		}
		for(String s : tempMessages){
			messages.remove(s);
		}
		
		for(int i = 0; i < messages.size() && i < messageBuffer; i++){
			terminal.writeCenter(messages.get(i), i);
		}
		if(subscreen == null){
			messages.clear();
			tempMessages.clear();
		}
	}
	
	private double[][] generateResistances(char[][] map){
		int width = map.length;
        int height = map[0].length;
        double[][] portion = new double[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                switch (map[i][j]) {
                    case '#':
                    case '+':
                        portion[i][j] = 1.0;
                        break;
                    default:
                        portion[i][j] = 0.0;
                }
            }
        }
        return portion;
	}
	
	private void displayTiles(AsciiPanel terminal) {
		resistanceMap = generateResistances(world.getCurrentLevel().pathMap);
		double[][] light;
		light = fov.calculateFOV(resistanceMap, world.getPlayer().x, world.getPlayer().y, world.getPlayer().visionRadius());

        for (int x = 0; x < screenWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                if (light[x][y] > 0.0f || world.getPlayer().canSee(x, y)) {
                    terminal.write(world.getCurrentLevel().glyph(x, y), x, y + messageBuffer, world.getCurrentLevel().color(x, y));
                    world.getCurrentLevel().revealed[x][y] = true;
                } else {
                    if (world.getCurrentLevel().revealed[x][y] && !world.getPlayer().canSee(x, y)) {
                        terminal.write(world.getCurrentLevel().baseGlyph(x, y), x, y + messageBuffer, world.getCurrentLevel().baseColor(x, y));
                    }
                }
            }
        }
    }
	
	public void doAction(Point currentDirection){
		if(world.getCurrentLevel().isClosedDoor(world.getPlayer().x + currentDirection.x, world.getPlayer().y + currentDirection.y)){
			subscreen = new OpenDoorDialog(world.getCurrentLevel(), currentDirection.x, currentDirection.y);
		}
		else{
			world.getPlayer().move(currentDirection.x, currentDirection.y);
		}
	}
	
	@Override
	public Screen respondToUserInput(KeyEvent key) {
        if (subscreen != null) {
                subscreen = subscreen.respondToUserInput(key);
            } else {
                switch (key.getKeyCode()) {
                    case KeyEvent.VK_NUMPAD4: {
                        currentDirection = Point.WEST;
                        doAction(currentDirection);
                        break;
                    }
                    case KeyEvent.VK_NUMPAD6: {
                        currentDirection = Point.EAST;
                        doAction(currentDirection);
                        break;
                    }
                    case KeyEvent.VK_NUMPAD8: {
                        currentDirection = Point.NORTH;
                        doAction(currentDirection);
                        break;
                    }
                    case KeyEvent.VK_NUMPAD2: {
                        currentDirection = Point.SOUTH;
                        doAction(currentDirection);
                        break;
                    }
                    case KeyEvent.VK_NUMPAD7: {
                        currentDirection = Point.NORTH_WEST;
                        doAction(currentDirection);
                        break;
                    }
                    case KeyEvent.VK_NUMPAD9: {
                        currentDirection = Point.NORTH_EAST;
                        doAction(currentDirection);
                        break;
                    }
                    case KeyEvent.VK_NUMPAD1: {
                        currentDirection = Point.SOUTH_WEST;
                        doAction(currentDirection);
                        break;
                    }
                    case KeyEvent.VK_NUMPAD3: {
                        currentDirection = Point.SOUTH_EAST;
                        doAction(currentDirection);
                        break;
                    }
                    case KeyEvent.VK_NUMPAD5: {
                        currentDirection = Point.WAIT;
                        doAction(currentDirection);
                        break;
                    }
                    default: {
                        currentDirection = Point.WAIT;
                        doAction(currentDirection);
                    }
                }

            switch (key.getKeyChar()) {
                case 'd': {
                    subscreen = new DropScreen(world.getPlayer());
                    break;
                }
                case 'D': {
                    subscreen = new DrinkScreen(world.getPlayer());
                    break;
                }
                case 'i': {
                    subscreen = new EquipmentScreen(world.getPlayer());
                    break;
                }
                case 'I': {
                    subscreen = new InventoryScreen(world.getPlayer());
                    break;
                }
                case 'p': {
                    world.getPlayer().pickupItem();
                    break;
                }
                case 't': {
                    if (world.getPlayer().checkIfAmmunitionAndRangedWeaponMatch()) {
                        subscreen = new FireWeaponScreen(world.getCurrentLevel().player);
                    }
                    break;
                }
                case '>': {
                    if (world.getCurrentLevel().tile(world.getPlayer().x, world.getPlayer().y) == Tile.STAIRS_DOWN
                            || world.getCurrentLevel().tile(world.getPlayer().x, world.getPlayer().y) == Tile.CAVE) {
                        world.goDownALevel();
                    } else {
                        world.getPlayer().notify("There's no way to go down from here.");
                    }
                    break;
                }
                case '<': {
                    if (world.getCurrentLevel().tile(world.getPlayer().x, world.getPlayer().y) == Tile.STAIRS_UP) {
                        world.goUpALevel();
                    } else {
                        world.getPlayer().notify("There's no way to go up from here.");
                    }
                    break;
                }
                case '?': {
                    subscreen = new HelpScreen();
                    break;
                }
            }
        }

        if(world.getPlayer().currentHP() < 1) { return new LoseScreen(); }
		
		if(subscreen == null && !key.isShiftDown()){ world.getCurrentLevel().update();}
		
		return this;
	}
}
