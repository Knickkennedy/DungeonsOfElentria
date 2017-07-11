package roguelike.levelBuilding;

import java.awt.Color;
import asciiPanel.AsciiPanel;
import roguelike.Mob.Colors;

public enum Tile {
	WATER('=', Colors.getColor("blue"), "water"),
	MOUNTAIN('^', Colors.getColor("white"), "mountain"),
	GRASS('"', Colors.getColor("bright green"), "grass"),
	FOREST('&', Colors.getColor("dark green"), "forest"),
	CAVE('*', Color.darkGray, "cave"),
	START('X', Color.lightGray, "start"),
	ROAD('.', Colors.getColor("brown"), "road"),
    FLOOR('.', Colors.getColor("gray"), "stone floor"),
    WALL('#', Colors.getColor("dark gray"), "wall"),
    PERM_WALL('#', Colors.getColor("dark gray"), "wall"),
    BOUNDS('X', Colors.getColor("dark gray"), ""),
	STAIRS_DOWN('>', Colors.getColor("light gray"), "stairs down"),
    STAIRS_UP('<', Colors.getColor("light gray"), "stairs up"),
	DOOR_CLOSED('+', Colors.getColor("light gray"), "door"),
	DOOR_OPEN('/', Colors.getColor("light gray"), "door");

    private char glyph;
    public char glyph() { return glyph; }

    private Color color;
    public Color color() { return color; }

    private String name;
    public String details() { return name; }
    
    Tile(char glyph, Color color, String name){
        this.glyph = glyph;
        this.color = color;
        this.name = name;
    }
    
    public boolean isGround(){
    	return this != WALL && this != PERM_WALL && this != BOUNDS && this != DOOR_CLOSED;
    }
    
    public boolean canEnter(){
    	return this == Tile.DOOR_OPEN || this == Tile.STAIRS_DOWN || this == Tile.STAIRS_UP
    		|| this == Tile.FLOOR;
    }
    
    public boolean isDiggable() {
        return this == Tile.WALL;
    }
    
    public boolean isBounds(){
    	return this == BOUNDS;
    }
}
