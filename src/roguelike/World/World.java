package roguelike.World;
import java.io.FileNotFoundException;
import java.util.*;

import roguelike.Items.ItemFactory;
import roguelike.Level.Level;
import roguelike.Mob.MobStore;
import roguelike.Mob.Player;
import roguelike.levelBuilding.Tile;
import roguelike.utility.Point;
import roguelike.utility.RandomGen;

public class World {
	public int screenWidth, mapHeight;
	private Player player;
	private MobStore mobStore;
	private ItemFactory itemStore;
	private Level currentLevel;
	private Point start;
	public List <String> messages = new ArrayList <String> ();
	private HashMap <Integer, Level> levels = new HashMap <Integer, Level> ();
	private String surface = "/surface.txt";
	private Scanner surfaceFile = null;
	private Point entranceCoordinates;
	
	public MobStore getMobStore() {return mobStore;}
	public void setMobStore(MobStore mobStore) {this.mobStore = mobStore;}

	public ItemFactory getItemStore() {return itemStore;}
	public void setItemStore(ItemFactory itemStore) {this.itemStore = itemStore;}

	public Level getCurrentLevel() {return currentLevel;}
	public void setCurrentLevel(Level currentLevel) {this.currentLevel = currentLevel;}
	
	public World(int screenWidth, int mapHeight, List <String> messages){
		initializeWorld(screenWidth, mapHeight, messages);
	}

	public Player getPlayer() {return player;}
	public void setPlayer(Player player) {this.player = player;}
	
	public void initializeWorld(int screenWidth, int mapHeight, List <String> messages){
		this.screenWidth = screenWidth;
		this.mapHeight = mapHeight;
		this.messages = messages;
		currentLevel = new Level(initializeSurfaceLevel(), screenWidth, mapHeight);
		mobStore = new MobStore(currentLevel, messages);
		itemStore = new ItemFactory(currentLevel);
		player = mobStore.newPlayer();
		currentLevel.setPlayer(player);
		currentLevel.levelNumber = 1;
		currentLevel.addAtSpecificLocation(player, start.x, start.y);
		levels.put(currentLevel.levelNumber, currentLevel);
	}
	
	public Tile[][] initializeSurfaceLevel(){
		try{ surfaceFile = openFile(surface); }
		catch(FileNotFoundException e){ System.out.println(e.getMessage()); }
		
		String levelLine = null;
		String[] tokens = null;
		Tile[][] surfaceMap = new Tile[this.screenWidth][this.mapHeight];
		
		int index = 0;
		
		for (int x = 0; x < this.screenWidth; x++){
			for(int y = 0; y < this.mapHeight; y++){
				surfaceMap[x][y] = Tile.WALL;
			}
		}

		while(surfaceFile.hasNextLine() && index < 29){
			levelLine = surfaceFile.nextLine();
			tokens = levelLine.split("");
			for(int i = 0; i < tokens.length; i++){
				int x = Integer.parseInt(tokens[i]);
				if(x == 0){
					surfaceMap[i][index] = Tile.WATER;
				}
				else if(x == 1){
					surfaceMap[i][index] = Tile.MOUNTAIN;
				}
				else if(x == 2){
					surfaceMap[i][index] = Tile.GRASS;
				}
				else if(x == 3){
					surfaceMap[i][index] = Tile.FOREST;
				}
				else if(x == 4){
					surfaceMap[i][index] = Tile.ROAD;
				}
				else if(x == 5){
					surfaceMap[i][index] = Tile.CAVE;
				}
				else if(x == 6){
					surfaceMap[i][index] = Tile.ROAD;
					start = new Point(i, index);
				}
			}
			index++;
		}
		
		return surfaceMap;
	}
	
	public void goUpALevel(){
		if(levels.containsKey(getCurrentLevel().levelNumber - 1)){
			levels.get(currentLevel.levelNumber - 1).mobs.add(player);
			player.setLevel(levels.get(currentLevel.levelNumber - 1));
			levels.get(currentLevel.levelNumber).mobs.remove(player);
			setCurrentLevel(levels.get(getCurrentLevel().levelNumber - 1));
			getCurrentLevel().setPlayer(player);
			if(getCurrentLevel().levelNumber == 1){
				getCurrentLevel().addAtSpecificLocation(player, entranceCoordinates.x, entranceCoordinates.y);
			}
			else{
				getCurrentLevel().addAtDownStaircase(player);
			}
		}
		else{
			player.notify("There's no way up from here.");
		}
	}
	
	public void goDownALevel(){
		if(levels.containsKey(currentLevel.levelNumber + 1)){
			player.setLevel(levels.get(currentLevel.levelNumber + 1));
			levels.get(currentLevel.levelNumber).mobs.remove(player);
			setCurrentLevel(levels.get(getCurrentLevel().levelNumber + 1));
			getCurrentLevel().setPlayer(player);
			getCurrentLevel().addAtUpStaircase(player);
		}
		else{
			if(currentLevel.levelNumber == 1){
				entranceCoordinates = new Point(player.x, player.y);
			}
			Level tempLevel = new Level(screenWidth, mapHeight);
			tempLevel.buildLevel();
			mobStore = new MobStore(tempLevel, messages);
			itemStore = new ItemFactory(tempLevel);
			tempLevel.setPlayer(player);
			tempLevel.addAtUpStaircase(player);
			player.setLevel(tempLevel);
			currentLevel.remove(player);
			tempLevel.levelNumber = currentLevel.levelNumber + 1;
			setCurrentLevel(tempLevel);
			initializeMobsOnLevel();
			createRandomItems();
			levels.put(currentLevel.levelNumber, currentLevel);
		}
	}
	
	public void createRandomItems(){
		for(int i = 0; i < 25; i++){
			itemStore.newItemAtRandomLocation();
		}
	}
	
	public void initializeMobsOnLevel(){
		for(int i = 0; i < 25; i++){
			int roll = RandomGen.rand(0, mobStore.enemyDictionary.size() - 1);
			mobStore.newEnemy(mobStore.enemyDictionary.get(roll));
		}
	}
	
	private static Scanner openFile(String fileName) throws FileNotFoundException{
		Scanner scanner = new Scanner(World.class.getResourceAsStream(fileName));
		return scanner;
	}
}
