package roguelike.World;
import java.io.FileNotFoundException;
import java.util.*;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap;
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
	private Point start, bossLocation;
	public List <String> messages = new ArrayList <> ();
	private HashMap <Integer, Level> mainDungeon = new HashMap <> ();
	private HashMap <Point, String> bossMinions = new HashMap<>();
	private String surface = "/surface.txt";
	private String ElenaBossRoom = "/ElenasBossRoom.txt";
	private Scanner surfaceLevel = null;
	private Scanner bossLevel = null;
	
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
		currentLevel = new Level(initializeSurfaceLevel(), screenWidth, mapHeight, "Surface");
		mobStore = new MobStore(currentLevel, messages);
		itemStore = new ItemFactory(currentLevel);
		player = mobStore.newPlayer();
		currentLevel.setPlayer(player);
		currentLevel.levelNumber = 0;
		currentLevel.addAtSpecificLocation(player, start.x, start.y);
		mainDungeon.put(currentLevel.levelNumber, currentLevel);
	}
	
	public Tile[][] initializeSurfaceLevel(){
		try{ surfaceLevel = openFile(surface); }
		catch(FileNotFoundException e){ System.out.println(e.getMessage()); }
		
		String levelLine;
		Tile[][] surfaceMap = new Tile[this.screenWidth][this.mapHeight];
		
		int index = 0;
		
		for (int x = 0; x < this.screenWidth; x++){
			for(int y = 0; y < this.mapHeight; y++){
				surfaceMap[x][y] = Tile.WALL;
			}
		}

		while(surfaceLevel.hasNextLine()){
			levelLine = surfaceLevel.nextLine();
			for(int i = 0; i < levelLine.length(); i++){
				char c = levelLine.charAt(i);
				if(c == '='){
					surfaceMap[i][index] = Tile.WATER;
				}
				else if(c == '^'){
					surfaceMap[i][index] = Tile.MOUNTAIN;
				}
				else if(c == '"'){
					surfaceMap[i][index] = Tile.GRASS;
				}
				else if(c == '&'){
					surfaceMap[i][index] = Tile.FOREST;
				}
				else if(c == '.'){
					surfaceMap[i][index] = Tile.ROAD;
				}
				else if(c == '*'){
					surfaceMap[i][index] = Tile.CAVE;
				}
				else if(c == 'X'){
					surfaceMap[i][index] = Tile.ROAD;
					start = new Point(i, index);
				}
			}
			index++;
		}

		return surfaceMap;
	}

    public Tile[][] initializeBossRoom(){
        try{ bossLevel = openFile(ElenaBossRoom); }
        catch(FileNotFoundException e){ System.out.println(e.getMessage()); }

        String levelLine;
        Tile[][] bossMap = new Tile[this.screenWidth][this.mapHeight];

        int index = 0;

        for (int x = 0; x < this.screenWidth; x++){
            for(int y = 0; y < this.mapHeight; y++){
                bossMap[x][y] = Tile.WALL;
            }
        }

        String one = null, two = null, three = null, four = null;

        while(bossLevel.hasNextLine()) {
			levelLine = bossLevel.nextLine();
			String[] tokens = levelLine.split(":");
			if(tokens.length > 1){
			    switch(tokens[0].trim().charAt(0)){
                    case '1':{
                        one = tokens[1].trim();
                        break;
                    }
                    case '2':{
                        two = tokens[1].trim();
                        break;
                    }
                    case '3':{
                        three = tokens[1].trim();
                        break;
                    }
                    case '4':{
                        four = tokens[1].trim();
                        break;
                    }
                }
			    continue;
            }

            if(levelLine.isEmpty()){
			    continue;
            }

				for (int i = 0; i < levelLine.length(); i++) {
					char c = levelLine.charAt(i);
					if (c == '=') {
						bossMap[i][index] = Tile.WATER;
					} else if (c == '#') {
						bossMap[i][index] = Tile.WALL;
					} else if (c == '^') {
						bossMap[i][index] = Tile.MOUNTAIN;
					} else if (c == '"') {
						bossMap[i][index] = Tile.GRASS;
					} else if (c == '1') {
						bossMap[i][index] = Tile.FLOOR;
						Point newPoint = new Point(i, index);
						bossMinions.put(newPoint, one);
					} else if (c == '2') {
						bossMap[i][index] = Tile.FLOOR;
						Point newPoint = new Point(i, index);
						bossMinions.put(newPoint, two);
					} else if (c == '3') {
						bossMap[i][index] = Tile.FLOOR;
						Point newPoint = new Point(i, index);
						bossMinions.put(newPoint, three);
					} else if (c == '4') {
						bossMap[i][index] = Tile.FLOOR;
						Point newPoint = new Point(i, index);
						bossMinions.put(newPoint, four);
					} else if (c == '&') {
						bossMap[i][index] = Tile.FOREST;
					} else if (c == '.') {
						bossMap[i][index] = Tile.FLOOR;
					} else if (c == '*') {
						bossMap[i][index] = Tile.CAVE;
					} else if (c == '<') {
						bossMap[i][index] = Tile.STAIRS_UP;
					} else if (c == '+') {
						bossMap[i][index] = Tile.DOOR_CLOSED;
					} else if (c == 'h') {
						bossMap[i][index] = Tile.FLOOR;
						bossLocation = new Point(i, index);
						bossMinions.put(bossLocation, "Elena");
					}
				}
				index++;
		}
        return bossMap;
    }
	
	public void goUpALevel(){
		if(mainDungeon.containsKey(getCurrentLevel().levelNumber - 1)){
			mainDungeon.get(currentLevel.levelNumber - 1).mobs.add(player);
			player.setLevel(mainDungeon.get(currentLevel.levelNumber - 1));
			mainDungeon.get(currentLevel.levelNumber).mobs.remove(player);
			setCurrentLevel(mainDungeon.get(getCurrentLevel().levelNumber - 1));
			getCurrentLevel().setPlayer(player);
			getCurrentLevel().addAtDownStaircase(player);
		}
		else{
			player.notify("There's no way up from here.");
		}
	}
	
	public void goDownALevel(){
		if(mainDungeon.containsKey(currentLevel.levelNumber + 1)){
			player.setLevel(mainDungeon.get(currentLevel.levelNumber + 1));
			mainDungeon.get(currentLevel.levelNumber).mobs.remove(player);
			setCurrentLevel(mainDungeon.get(getCurrentLevel().levelNumber + 1));
			getCurrentLevel().setPlayer(player);
			getCurrentLevel().addAtUpStaircase(player);
		}
		else if(currentLevel.levelNumber == 4){
            Level tempLevel = new Level(initializeBossRoom(), screenWidth, mapHeight, "The Throne Room");
            mobStore = new MobStore(tempLevel, messages);
            itemStore = new ItemFactory(tempLevel);
            tempLevel.setPlayer(player);
            tempLevel.addAtUpStaircase(player);
            player.setLevel(tempLevel);
            currentLevel.remove(player);
            tempLevel.levelNumber = currentLevel.levelNumber + 1;
            tempLevel.dangerLevel = currentLevel.dangerLevel + 1;
            setCurrentLevel(tempLevel);
            initializeMonstersOnLevel(bossMinions);
            initializeItemsOnLevel();
            mainDungeon.put(currentLevel.levelNumber, currentLevel);
            System.out.println("Success!");
        }
		else{
			Level tempLevel = new Level(screenWidth, mapHeight);
			tempLevel.buildLevel();
			mobStore = new MobStore(tempLevel, messages);
			itemStore = new ItemFactory(tempLevel);
			tempLevel.setPlayer(player);
			tempLevel.addAtUpStaircase(player);
			player.setLevel(tempLevel);
			currentLevel.remove(player);
			tempLevel.levelNumber = currentLevel.levelNumber + 1;
            tempLevel.dangerLevel = currentLevel.dangerLevel + 1;
			setCurrentLevel(tempLevel);
			initializeMobsOnLevel();
			initializeItemsOnLevel();
			mainDungeon.put(currentLevel.levelNumber, currentLevel);
		}
	}

	public void initializeMonstersOnLevel(HashMap<Point, String> minions){
        Set<HashMap.Entry<Point, String>> allKeys = minions.entrySet();
        Iterator<HashMap.Entry<Point, String>> pointIterator = allKeys.iterator();
        while(pointIterator.hasNext()){
            HashMap.Entry<Point, String> mob = pointIterator.next();
            mobStore.newEnemyAtSpecificLocation(mob.getValue(), mob.getKey().x, mob.getKey().y);
        }
    }

	public void initializeItemsOnLevel(){
		if(currentLevel.dangerLevel == 1){
			for(int i = 0; i < 25; i++){
				int dangerCheck = RandomGen.rand(1, 100);
				if(dangerCheck < 98) {
					int roll = RandomGen.rand(1, itemStore.dangerOneItems.size());
					getCurrentLevel().addAtEmptyLocation(itemStore.newItem(itemStore.dangerOneItems.get(roll)));
				}
				else{
					int roll = RandomGen.rand(1, itemStore.dangerTwoItems.size());
					getCurrentLevel().addAtEmptyLocation(itemStore.newItem(itemStore.dangerTwoItems.get(roll)));
				}
			}
		}
		else if(currentLevel.dangerLevel == 2){
			for(int i = 0; i < 25; i++){
				int dangerCheck = RandomGen.rand(1, 100);
				if(dangerCheck < 98) {
					int roll = RandomGen.rand(1, itemStore.dangerTwoItems.size());
					getCurrentLevel().addAtEmptyLocation(itemStore.newItem(itemStore.dangerTwoItems.get(roll)));
				}
				else{
					int roll = RandomGen.rand(1, itemStore.dangerThreeItems.size());
					getCurrentLevel().addAtEmptyLocation(itemStore.newItem(itemStore.dangerThreeItems.get(roll)));
				}
			}
		}
		else{
			for(int i = 0; i < 25; i++){
				int roll = RandomGen.rand(1, itemStore.dangerThreeItems.size());
				getCurrentLevel().addAtEmptyLocation(itemStore.newItem(itemStore.dangerThreeItems.get(roll)));
			}
		}
	}
	
	public void initializeMobsOnLevel(){
		if(currentLevel.dangerLevel == 1){
			for(int i = 0; i < 25; i++){
			    int dangerCheck = RandomGen.rand(1, 100);
			    if(dangerCheck < 98) {
                    int roll = RandomGen.rand(1, mobStore.dangerOneEnemies.size());
                    mobStore.newEnemy(mobStore.dangerOneEnemies.get(roll));
                }
                else{
			        int roll = RandomGen.rand(1, mobStore.dangerTwoEnemies.size());
			        mobStore.newEnemy(mobStore.dangerTwoEnemies.get(roll));
                }
            }
		}
		else if(currentLevel.dangerLevel == 2){
		    for(int i = 0; i < 25; i++){
                int dangerCheck = RandomGen.rand(1, 100);
                if(dangerCheck < 98) {
                    int roll = RandomGen.rand(1, mobStore.dangerTwoEnemies.size());
                    mobStore.newEnemy(mobStore.dangerTwoEnemies.get(roll));
                }
                else{
                    int roll = RandomGen.rand(1, mobStore.dangerThreeEnemies.size());
                    mobStore.newEnemy(mobStore.dangerThreeEnemies.get(roll));
                }
            }
        }
        else{
            for(int i = 0; i < 25; i++){
                int roll = RandomGen.rand(1, mobStore.dangerThreeEnemies.size());
                mobStore.newEnemy(mobStore.dangerThreeEnemies.get(roll));
            }
        }
	}
	
	private static Scanner openFile(String fileName) throws FileNotFoundException{
		return new Scanner(World.class.getResourceAsStream(fileName));
	}
}
