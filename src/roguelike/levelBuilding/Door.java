package roguelike.levelBuilding;

public class Door {
	public int x;
	public int y;
	public char glyph;
	
	public Door(int x, int y){
		this.x = x;
		this.y = y;
		this.glyph = '+';
	}
	
	public Tile close(){
		this.glyph = '+';
		return Tile.DOOR_CLOSED;
	}
	
	public Tile open(){
		this.glyph = '/';
		return Tile.DOOR_OPEN;
	}
	
	public boolean overlaps(Door d){
		return ((this.x + 1 == d.x && this.y == d.y) || (this.x - 1 == d.x && this.y == d.y) || (this.x == d.x && this.y + 1 == d.y) || (this.x == d.x && this.y - 1 == d.y));
	}
}
