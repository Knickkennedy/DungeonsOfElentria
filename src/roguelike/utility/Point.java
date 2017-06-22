package roguelike.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Point{

	public static final Point NORTH_WEST = new Point(-1, -1);
	public static final Point NORTH = new Point(0, -1);
	public static final Point NORTH_EAST = new Point(1, -1);
	public static final Point EAST = new Point(1, 0);
	public static final Point SOUTH_EAST = new Point(1, 1);
	public static final Point SOUTH = new Point(0, 1);
	public static final Point SOUTH_WEST = new Point(-1, 1);
	public static final Point WEST = new Point(-1, 0);
	public static final Point WAIT = new Point(0, 0);

	public int x;
	public int y;
	public Point parent;

	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}

	public Point(int x, int y, Point p) {
		this.x = x;
		this.y = y;
		parent = p;
	}

public List<Point> neighbors8(){
	List<Point> points = new ArrayList<Point>();
	
	for (int ox = -1; ox < 2; ox++){
		for (int oy = -1; oy < 2; oy++){
			if (ox == 0 && oy == 0)
				continue;
			
			points.add(new Point(x+ox, y+oy));
		}
	}

	Collections.shuffle(points);
	return points;
}
}