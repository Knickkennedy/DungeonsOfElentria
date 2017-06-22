package roguelike.utility;

import squidpony.squidgrid.LOS;
import squidpony.squidgrid.Radius;

public class ELOS extends LOS{

	Radius radiusStrategy = Radius.CIRCLE;
	
	public ELOS() {
		super();
	}
	
	@Override
	public boolean isReachable(char[][] walls, int startx, int starty, int targetx, int targety) {
        if(walls.length < 1) return false;
        double[][] resMap = new double[walls.length][walls[0].length];
        for(int x = 0; x < walls.length; x++){
            for(int y = 0; y < walls[0].length; y++){
                resMap[x][y] = (walls[x][y] == '#' || walls[x][y] == '+') ? 1.0 : 0.0;
            }
        }
        return isReachable(resMap, startx, starty, targetx, targety, radiusStrategy);
    }
}
