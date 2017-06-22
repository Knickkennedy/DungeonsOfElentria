package roguelike.levelBuilding;

public class Room{
	// The four corners of the room
	public int x1;
	public int x2;
	public int y1;
	public int y2;
	public int area;
	
	//Width and height of room in terms of grid
	public int width;
	public int height;
	
	//center point of room
	public int centerX;
	public int centerY;
	
	public Room(int x, int y, int w, int h){
		super();
		setX1(x);
		setX2(x, w);
		setY1(y);
		setY2(y, h);
		setWidth(w);
		setHeight(h);
		setCenterX();
		setCenterY();
		this.area = w * h;
	}
	
	public int getCenterX(){
		return this.centerX;
	}
	
	public int getCenterY(){
		return this.centerY;
	}
	
	public void setCenterX(){
		centerX = (x1 + x2) / 2;
	}
	
	public void setCenterY(){
		centerY = (y1 + y2) / 2;
	}
	
	public int getHeight(){
		return this.height;
	}
	
	public void setHeight(int h){
		this.height = h;
	}
	
	public int getWidth(){
		return this.width;
	}
	
	public void setWidth(int w){
		this.width = w;
	}
	
	public void setY2(int y, int h){
		this.y2 = (y + h) - 1;
	}
	
	public int getY2(){
		return this.y2;
	}
	
	public void setY1(int y){
		this.y1 = y;
	}
	
	public int getY1(){
		return this.y1;
	}
	
	public void setX2(int x, int w){
		this.x2 = (x + w) - 1;
	}
	
	public int getX2(){
		return this.x2;
	}
	
	public void setX1(int x){
		this.x1 = x;
	}
	
	public int getX1(){
		return this.x1;
	}
	
	public boolean intersects(Room newRoom){
		return (this.x1 <= newRoom.getX2() + 1 && this.x2 + 1 >= newRoom.getX1()
				&& this.y1 <= newRoom.getY2() + 1 && this.getY2() + 1 >= newRoom.getY1());
	}
}
