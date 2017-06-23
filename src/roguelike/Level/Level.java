package roguelike.Level;

import java.awt.Color;
import java.util.*;
import roguelike.utility.*;
import roguelike.Items.*;
import roguelike.Mob.BaseEntity;
import roguelike.Mob.Player;
import roguelike.levelBuilding.*;

public class Level{
	public Tile[][] map;
	public char[][] pathMap;
	public Point stairsDown;
	public Point stairsUp;
	public int width;
	public int height;
	public int dangerLevel;
	public String levelID;
	public int levelNumber;
	public Point start, current, cave;
	public List <Point> frontier = new ArrayList <Point> ();
	public List <Point> toRemove = new ArrayList <Point> ();
	public List <Point> deadEnds = new ArrayList <Point> ();
	public List <Point> potentialDoors = new ArrayList <Point> ();
	public List <Point> connections = new ArrayList <Point> ();
	public List <String> test = new ArrayList <String> ();
	public List <Room> rooms = new ArrayList <Room> ();
	public List <Door> doors = new ArrayList <Door> ();
	public List <Point> cTR = new ArrayList<Point> ();
	public List <BaseEntity> mobs = new ArrayList <BaseEntity> ();
	public List <Point> extraDoors = new ArrayList <Point> ();
	public List <BaseItem> items = new ArrayList <BaseItem> ();
	public int maxRoomSize;
	public int minRoomSize;
	public int numRoomTries;
	public Player player;
	public boolean[][] roomFlag;
	public boolean[][] connected;
	public boolean[][] revealed;
	

	public Level(int width, int height){
		this.width = width;
		this.height = height;
		pathMap = new char[width][height];
		this.levelNumber = 0;
		map = new Tile[width][height];
		this.minRoomSize = 3;
		this.maxRoomSize = 9;
		this.numRoomTries = 50;
		this.connected = new boolean[width][height];
		this.roomFlag = new boolean[width][height];
		this.revealed = new boolean[width][height];
		this.levelID = "Dungeon Floor";
		this.dangerLevel = 1;
		}

	public Level(Tile[][] map, int screenWidth, int mapHeight){
		this.width = screenWidth;
		this.height = mapHeight;
		pathMap = new char[width][height];
		this.levelNumber = 0;
		this.map = map;
		this.minRoomSize = 3;
		this.maxRoomSize = 9;
		this.numRoomTries = 50;
		this.connected = new boolean[width][height];
		this.roomFlag = new boolean[width][height];
		this.revealed = new boolean[width][height];
		this.levelID = "Surface";
		this.dangerLevel = 1;
		setPathFinding();
	}

	public void setDangerLevel(int depth){ this.dangerLevel = depth; }

	public Level getLevel(){
		return this;
	}
	
	public void setPlayer(Player player){
		this.player = player;
	}
	
	public Player getPlayer(){
		return this.player;
	}
	
	public boolean isWall(int x, int y){
		return map[x][y] == Tile.WALL || map[x][y] == Tile.PERM_WALL;
	}
	
	public void remove(BaseEntity otherEntity){
		mobs.remove(otherEntity);
	}
	
	public void update(){
		List <BaseEntity> toUpdate = new ArrayList <> (mobs);
		for(BaseEntity bE : toUpdate){
			if(player.currentHP() < 1) { return;	}
				bE.update();
		}
	}
	
	public BaseEntity checkForMob(int x, int y){
		for(BaseEntity bE : mobs){
			if(bE.x == x && bE.y == y){ return bE; }
		}
		return null;
	}
	
	public BaseItem checkItems(int x, int y){
		for(BaseItem item : items){
			if(item.x == x && item.y == y){
				return item;
			}
		}
		return null;
	}
	
	public void removeItem(BaseItem item){
		items.remove(item);
	}
	public int getWidth(){
		return this.width;}
	
	public int getHeight(){
		return this.height;}
	
	public char glyph(int x, int y){
		BaseEntity Entity = checkForMob(x, y);
		BaseItem item = checkItems(x, y);
		if(Entity != null){
			return Entity.glyph();
		}
		if(item != null){
			return item.glyph();
		}
		return tile(x, y).glyph();
		}
	
	public char baseGlyph(int x, int y) {
		return tile(x, y).glyph();
	}
	
	public boolean isGround(int x, int y){
		return ((map[x][y] == Tile.FLOOR) || (map[x][y] == Tile.STAIRS_DOWN)
				|| (map[x][y] == Tile.STAIRS_UP) || (map[x][y] == Tile.DOOR_OPEN)
				|| (map[x][y] == Tile.GRASS) || (map[x][y] == Tile.FOREST)
				|| (map[x][y] == Tile.ROAD) || (map[x][y] == Tile.START)
				|| (map[x][y] == Tile.CAVE) || (map[x][y] == Tile.WATER)); 
	}
	
	public Color color(int x, int y){
		BaseEntity Entity = checkForMob(x, y);
		BaseItem item = checkItems(x, y);
		if(Entity != null){
			return Entity.color();
		}
		if(item != null){
			return item.color();
		}
		return tile(x, y).color();
	}
	
	public Color baseColor(int x, int y) {
		return tile(x, y).color();
	}
	
	public Level buildLevel(){
		initializeMap();
		placeRooms();
		startMaze();
		findConnections();
		placeAllDoors();
		removeAllDeadEnds();
		placeStairs();
		setPathFinding();
		return this;}
	
	public void setPathFinding() {
		for(int x = 0; x < this.width; x++) {
			for(int y = 0; y < this.height; y++) {
				pathMap[x][y] = map[x][y].glyph();
			}
		}
	}
	
	public void removeAllDeadEnds(){
		for(int i = 0; i < 100; i++){
			removeDeadEnds();
		}
	}
	
	public boolean isClosedDoor(int x, int y){
		return map[x][y] == Tile.DOOR_CLOSED;
	}
	
	public boolean isUpStaircase(int x, int y){
		return map[x][y] == Tile.STAIRS_UP;
	}
	
	public boolean isDownStaircase(int x, int y){
		return map[x][y] == Tile.STAIRS_DOWN;
	}
	
	public void addAtSpecificLocation(BaseItem item, int x, int y){
		item.x = x;
		item.y = y;
		items.add(item);
	}
	
	public void addAtSpecificLocation(BaseEntity entity, int x, int y){
		entity.x = x;
		entity.y = y;
		mobs.add(entity);
	}
	
	public void addAtEmptyLocation(BaseItem item){
		int x;
		int y;
		do{
			x = RandomGen.rand(0, width - 1);
			y = RandomGen.rand(0, height - 1);
		}
		while((!isGround(x, y)) || (hasItemAlready(x, y)));
		
		item.x = x;
		item.y = y;
		items.add(item);
	}
	
	public boolean hasItemAlready(int x, int y){
		for(BaseItem itemToCompare : items){
			if(itemToCompare.x == x && itemToCompare.y == y){
				return true;
			}
		}
		return false;
	}
	
	public void addAtEmptyLocation(BaseEntity Entity){
		int x;
		int y;
		do{
			x = RandomGen.rand(0, width - 1);
			y = RandomGen.rand(0, height - 1);
		}
		while(!isGround(x, y) || checkForMob(x,y) != null);
		
		Entity.x = x;
		Entity.y = y;
		mobs.add(Entity);
	}
	
	public void addAtUpStaircase(BaseEntity Entity){
		Entity.x = stairsUp.x;
		Entity.y = stairsUp.y;
		mobs.add(Entity);
	}
	
	public void addAtDownStaircase(BaseEntity Entity){
		Entity.x = stairsDown.x;
		Entity.y = stairsDown.y;
		mobs.add(Entity);
	}
	
	public void placeStairs(){
		Collections.shuffle(rooms);
		Room upstairs = rooms.get(0);
		Room downstairs = rooms.get(rooms.size() - 1);
		int x1 = RandomGen.rand(upstairs.x1 + 1, upstairs.x2 - 1);
		int y1 = RandomGen.rand(upstairs.y1 + 1, upstairs.y2 - 1);
		int x2 = RandomGen.rand(downstairs.x1 + 1, downstairs.x2 - 1);
		int y2 = RandomGen.rand(downstairs.y1 + 1, downstairs.y2 - 1);
		map[x1][y1] = Tile.STAIRS_UP;
		map[x2][y2] = Tile.STAIRS_DOWN;
		stairsUp = new Point(x1, y1);
		stairsDown = new Point(x2, y2);
	}
	
	public void placeAllDoors(){
		int start = RandomGen.rand(0, rooms.size() - 1);
		Room tempRoom = rooms.get(start);
		floodFill(tempRoom.centerX, tempRoom.centerY);
		while(!connections.isEmpty()){
			findDoors();
			placeDoor();
			removeExtraConnectors();
		}
		createExtraDoors();
	}
	public void initializeMap(){
		for (int x = 0; x < this.width; x++){
			for(int y = 0; y < this.height; y++){
				map[x][y] = Tile.WALL;
				connected[x][y] = false;
				revealed[x][y] = false;
			}
		}
	}
	public void placeRooms(){
		for(int i = 0; i < numRoomTries; i++){
			placeRoom();
		}
	}
	
	public void createExtraDoors(){
		for(Point p : extraDoors){
			boolean failed = false;
			Door newDoor = new Door(p.x, p.y);
			for(Door d : doors){
				if(newDoor.overlaps(d)){
					failed = true;
					break;
				}
			}
			if(!failed){
				map[p.x][p.y] = newDoor.close();
				doors.add(newDoor);
			}
				
		}
	}
	public void placeDoor(){
		int door = RandomGen.rand(0, potentialDoors.size() - 1);
		Door newDoor = new Door(potentialDoors.get(door).x, potentialDoors.get(door).y);
		map[potentialDoors.get(door).x][potentialDoors.get(door).y] = newDoor.close();
		potentialDoors.clear();
		doors.add(newDoor);
		floodFill(newDoor.x, newDoor.y);
	}
	public void findDoors(){
		Collections.shuffle(connections);
		for(Point p : connections){
			if((connected[p.x - 1][p.y]) && (!connected[p.x + 1][p.y])){
				potentialDoors.add(p);
			}
			if((connected[p.x + 1][p.y]) && (!connected[p.x - 1][p.y])){
				potentialDoors.add(p);
			}
			if((connected[p.x][p.y - 1]) && (!connected[p.x][p.y + 1])){
				potentialDoors.add(p);
			}
			if((connected[p.x][p.y + 1]) && (!connected[p.x][p.y - 1])){
				potentialDoors.add(p);
			}
		}
	}
	public void removeExtraConnectors(){
		Collections.shuffle(connections);
		for(Point p : connections){
			if(connected[p.x - 1][p.y] && connected[p.x + 1][p.y]){
				double saveChance = Math.random();
				cTR.add(p);
				if(saveChance > .95){
					extraDoors.add(p);
				}
				continue;
			}
			if(connected[p.x][p.y - 1] && connected[p.x][p.y + 1]){
				double saveChance = Math.random();
				cTR.add(p);
				if(saveChance > .95){
					extraDoors.add(p);
				}
				continue;	
			}
		}
		for(Point p : cTR){
			connections.remove(p);
		}
		cTR.clear();
	}
	public void floodFill(int x, int y){
		if(((map[x][y] == Tile.FLOOR) || (map[x][y] == Tile.DOOR_CLOSED)) && (!connected[x][y])){
			connected[x][y] = true;
		}
		else{
			return;
		}
		floodFill(x + 1, y);
		floodFill(x - 1, y);
		floodFill(x, y + 1);
		floodFill(x, y - 1);
	}
	
	public void findConnections(){
		for(int i = 1; i < width - 1; i++){
			for(int j = 1; j < height - 1; j++){
				if((map[i][j] == Tile.WALL)
						&& (map[i - 1][j] == Tile.FLOOR)
						&& (map[i + 1][j] == Tile.FLOOR)
						&& (roomFlag[i + 1][j] || roomFlag[i - 1][j])){
					connections.add(new Point(i, j));
				}
				if((map[i][j] == Tile.WALL)
						&& (map[i][j - 1] == Tile.FLOOR)
						&& (map[i][j + 1] == Tile.FLOOR)
						&& (roomFlag[i][j - 1] || roomFlag[i][j + 1])){
					connections.add(new Point(i, j));
				}
			}
		}
	}
	public void startMaze(){
		for(int i = 1; i < width - 2; i++){
			for(int j = 1; j < height - 2; j++){
				if(isSolid(i, j)){
					generateMaze(i, j);
				}
			}
		}
	}
	
	public boolean isSolid(int x, int y){
		if((map[x][y] == Tile.WALL) 
			&& (map[x + 1][y] == Tile.WALL)
			&& (map[x - 1][y] == Tile.WALL)
			&& (map[x][y - 1] == Tile.WALL)
			&& (map[x][y + 1] == Tile.WALL)
			&& (map[x+1][y+1] == Tile.WALL)
			&& (map[x+1][y-1] == Tile.WALL)
			&& (map[x-1][y+1] == Tile.WALL)
			&& (map[x-1][y-1] == Tile.WALL)){
				return true;
			}
		else{
			return false;
		}
	}
	
	public void generateMaze(int x, int y){
		Point start = new Point(x, y, null);
		map[start.x][start.y] = Tile.FLOOR;
		buildFrontier(start);
		updateFrontier();
		while(!frontier.isEmpty()){
			Point current = frontier.remove(RandomGen.rand(0, frontier.size() - 1));
			carvePath(current);
			buildFrontier(current);
			updateFrontier();
		}
	}
	
	public void buildFrontier(Point p){
		if((p.x + 2 <= width - 1) && (p.y - 1 >= 0) && (p.y + 1 <= height - 1)){ // East
			if(map[p.x + 1][p.y] == Tile.WALL){
				if(map[p.x + 2][p.y] != Tile.FLOOR){
					if(map[p.x + 1][p.y - 1] != Tile.FLOOR){
						if(map[p.x + 1][p.y + 1] != Tile.FLOOR){
							if(map[p.x + 2][p.y + 1] != Tile.FLOOR){
								if(map[p.x + 2][p.y - 1] != Tile.FLOOR){
									frontier.add(new Point(p.x + 1, p.y, p));
								}
							}
						}
					}
				}
			}
		}
		if((p.x - 2 >= 0) && (p.y - 1 >= 0) && (p.y + 1 <= height - 1)){ // West
			if(map[p.x - 1][p.y] == Tile.WALL){
				if(map[p.x - 2][p.y] != Tile.FLOOR){
					if(map[p.x - 1][p.y - 1] != Tile.FLOOR){
						if(map[p.x - 1][p.y + 1] != Tile.FLOOR){
							if(map[p.x - 2][p.y + 1] != Tile.FLOOR){
								if(map[p.x - 2][p.y - 1] != Tile.FLOOR){
									frontier.add(new Point(p.x - 1, p.y, p));
								}
							}
						}
					}
				}
			}
		}
		if((p.x - 1 >= 0) && (p.x + 1 <= width - 1) && (p.y - 2 >= 0)){ // North
			if(map[p.x][p.y - 1] == Tile.WALL){
				if(map[p.x][p.y - 2] != Tile.FLOOR){
					if(map[p.x - 1][p.y - 1] != Tile.FLOOR){
						if(map[p.x + 1][p.y - 1] != Tile.FLOOR){
							if(map[p.x + 1][p.y - 2] != Tile.FLOOR){
								if(map[p.x - 1][p.y - 2] != Tile.FLOOR){
									frontier.add(new Point(p.x, p.y - 1, p));
								}
							}
						}
					}
				}
			}
		}
		if((p.x - 1 >= 0) && (p.x + 1 <= width - 1) && (p.y + 2 <= height - 1)){ // South
			if(map[p.x][p.y + 1] == Tile.WALL){
				if(map[p.x][p.y + 2] != Tile.FLOOR){
					if(map[p.x - 1][p.y + 1] != Tile.FLOOR){
						if(map[p.x + 1][p.y + 1] != Tile.FLOOR){
							if(map[p.x + 1][p.y + 2] != Tile.FLOOR){
								if(map[p.x - 1][p.y + 2] != Tile.FLOOR){
									frontier.add(new Point(p.x, p.y + 1, p));
								}
							}
						}
					}
				}
			}
		}
	}
	
	public void updateFrontier(){
		for(int i = 0; i < frontier.size() - 1; i++){
			int floorCount = 0;
			if(frontier.get(i).x > frontier.get(i).parent.x){
				if(map[frontier.get(i).x + 1][frontier.get(i).y] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x - 1][frontier.get(i).y] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x][frontier.get(i).y + 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x][frontier.get(i).y - 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x + 1][frontier.get(i).y + 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x + 1][frontier.get(i).y - 1] == Tile.FLOOR){
					floorCount++;
				}
			}
			if(frontier.get(i).x < frontier.get(i).parent.x){
				if(map[frontier.get(i).x + 1][frontier.get(i).y] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x - 1][frontier.get(i).y] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x][frontier.get(i).y + 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x][frontier.get(i).y - 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x - 1][frontier.get(i).y + 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x - 1][frontier.get(i).y - 1] == Tile.FLOOR){
					floorCount++;
				}
			}
			if(frontier.get(i).y < frontier.get(i).parent.y){
				if(map[frontier.get(i).x + 1][frontier.get(i).y] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x - 1][frontier.get(i).y] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x][frontier.get(i).y + 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x][frontier.get(i).y - 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x - 1][frontier.get(i).y - 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x + 1][frontier.get(i).y - 1] == Tile.FLOOR){
					floorCount++;
				}
			}
			if(frontier.get(i).y > frontier.get(i).parent.y){
				if(map[frontier.get(i).x + 1][frontier.get(i).y] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x - 1][frontier.get(i).y] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x][frontier.get(i).y + 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x][frontier.get(i).y - 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x - 1][frontier.get(i).y + 1] == Tile.FLOOR){
					floorCount++;
				}
				if(map[frontier.get(i).x + 1][frontier.get(i).y + 1] == Tile.FLOOR){
					floorCount++;
				}
			}
				if(floorCount > 1){
					toRemove.add(frontier.get(i));
				}
		}
		for(Point p : toRemove){
			frontier.remove(p);
		}
	}
	
	public void carvePath(Point s){
		map[s.x][s.y] = Tile.FLOOR;
	}
	public void placeRoom(){
		int h = RandomGen.rand(minRoomSize, maxRoomSize);
		if(h % 2 == 0){
			h = h + 1;
		}
		int w = RandomGen.rand(h, maxRoomSize);
		if(w % 2 == 0){
			w = w + 1;
		}
		int x = RandomGen.rand(1, (this.width - w - 2));
		int y = RandomGen.rand(1, (this.height - h - 2));
		if(x % 2 == 0){
			x += 1;
		}
		if(y % 2 == 0){
			y += 1;
		}
		
		Room newRoom = new Room(x, y, w, h);
		
		boolean failed = false;
		for(Room otherRoom : rooms){
			if(newRoom.intersects(otherRoom)){
				failed = true;
				break;
			}
		}
		if(!failed){
			createRoom(newRoom);
			rooms.add(newRoom);
		}
	}
	public void createRoom(Room newRoom){
		for(int i = 0; i < newRoom.width ; i++){
			for(int j = 0; j < newRoom.height; j++){
				map[newRoom.getX1() + i][newRoom.getY1() + j] = Tile.FLOOR;
				roomFlag[newRoom.getX1() + i][newRoom.getY1() + j] = true;
			}
		}
	}
	
	public void removeDeadEnds(){
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				int wallCount = 0;
				if(map[i][j] == Tile.FLOOR){
					if((map[i - 1][j] == Tile.WALL) || (map[i - 1][j] == Tile.PERM_WALL)){
						wallCount++;
					}
					if((map[i + 1][j] == Tile.WALL) || (map[i + 1][j] == Tile.PERM_WALL)){
						wallCount++;
					}
					if((map[i][j - 1] == Tile.WALL) || (map[i][j - 1] == Tile.PERM_WALL)){
						wallCount++;
					}
					if((map[i][j + 1] == Tile.WALL) || (map[i][j + 1] == Tile.PERM_WALL)){
						wallCount++;
					}
					if(wallCount >= 3){
						Point newP = new Point(i, j);
						deadEnds.add(newP);
					}
				}
				if(map[i][j] == Tile.DOOR_CLOSED){
					if((map[i - 1][j] == Tile.WALL) || (map[i - 1][j] == Tile.PERM_WALL)){
						wallCount++;
					}
					if((map[i + 1][j] == Tile.WALL) || (map[i + 1][j] == Tile.PERM_WALL)){
						wallCount++;
					}
					if((map[i][j - 1] == Tile.WALL) || (map[i][j - 1] == Tile.PERM_WALL)){
						wallCount++;
					}
					if((map[i][j + 1] == Tile.WALL) || (map[i][j + 1] == Tile.PERM_WALL)){
						wallCount++;
					}
					if(wallCount >= 3){
						Point newP = new Point(i, j);
						deadEnds.add(newP);
					}
				}
			}
		}
		for(Point p : deadEnds){
			map[p.x][p.y] = Tile.WALL;
		}
		deadEnds.clear();
	}
	
	public Tile tile(int x, int y){
		if (x < 0 || x >= width || y < 0 || y >= height)
			return Tile.BOUNDS;
		else
			return map[x][y];}

}

