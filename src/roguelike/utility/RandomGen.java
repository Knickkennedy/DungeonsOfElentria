package roguelike.utility;

import java.util.*;

public class RandomGen {
	private static Random random = new Random(System.currentTimeMillis());
	
	public static int rand(int min, int max){
		return min + (int)(random.nextDouble() * ((max - min) + 1));
	}
}
