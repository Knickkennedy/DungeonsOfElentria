package roguelike.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Point {

	public static final Point NORTH_WEST = new Point(-1, -1);
	public static final Point NORTH = new Point(0, -1);
	public static final Point NORTH_EAST = new Point(1, -1);
	public static final Point EAST = new Point(1, 0);
	public static final Point SOUTH_EAST = new Point(1, 1);
	public static final Point SOUTH = new Point(0, 1);
	public static final Point SOUTH_WEST = new Point(-1, 1);
	public static final Point WEST = new Point(-1, 0);
	public static final List <Point> cardinal = Arrays.asList(NORTH, EAST, WEST, SOUTH);
	public static final List <Point> direction = Arrays.asList(NORTH_WEST, NORTH, NORTH_EAST,
													EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST);
	public static final Point WAIT = new Point(0, 0);

	public int x;
	public int y;
	public Point parent;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point(int x, int y, Point p) {
		this.x = x;
		this.y = y;
		parent = p;
	}

	public void add(Point other) {
		this.x += other.x;
		this.y += other.y;
	}

	public Point getNeighbor(Point direction){
		return new Point(x + direction.x, y + direction.y);
	}

	public List <Point> neighbors(){
		List <Point> neighbors = new ArrayList<>();
		for(Point direction : direction){
			neighbors.add(getNeighbor(direction));
		}

		return neighbors;
	}

	public void flipHorizontally() {
		this.x *= -1;
	}

	public void flipVertically() {
		this.y *= -1;
	}

}