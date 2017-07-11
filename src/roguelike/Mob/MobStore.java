package roguelike.Mob;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import asciiPanel.AsciiPanel;
import roguelike.AI.playerAI;
import roguelike.Level.Level;

public class MobStore {
	public String mobFileName = "/Mobs.txt";
	public String playerFileName = "/HumanPlayer.txt";
	public Scanner playerFile = null;
	public Scanner mobFile = null;
	public Level thisLevel;
	public HashMap <Integer, String> dangerOneEnemies = new HashMap <> ();
	public HashMap <Integer, String> dangerTwoEnemies = new HashMap <> ();
	public HashMap <Integer, String> dangerThreeEnemies = new HashMap <> ();
	public List <String> messages;
	private int enemyCount;
	
	public void initializeEnemyDictionary(){
		try{ mobFile = openMobFile(mobFileName); }
		catch(FileNotFoundException e){ System.out.println(e.getMessage()); }
		
		String name = null;
		String[] tokens = null;
		int danger = 0;
		
		while(mobFile.hasNextLine()){
            String tempLine = mobFile.nextLine();

			if((tempLine.isEmpty()) || (tempLine.contains("FAMILY")) || (tempLine.contains("BOSSES"))){
				continue;
			}
			tokens = tempLine.split(":", 2);
			if(tokens[0].trim().equals("name")) {
                name = tokens[1].trim();
            }
            if(tokens[0].trim().equals("danger level")){
			    danger = Integer.parseInt(tokens[1].trim());
                if(danger == 1){
                    dangerOneEnemies.put(dangerOneEnemies.size() + 1, name);
                }
                else if(danger == 2){
                    dangerTwoEnemies.put(dangerTwoEnemies.size() + 1, name);
                }
                else if(danger == 3){
                    dangerThreeEnemies.put(dangerThreeEnemies.size() + 1, name);
                }
            }
		}
		
		mobFile.close();
	}
	
	public MobStore(Level level, List <String> messages){
		enemyCount = 0;
		initializeEnemyDictionary();
		this.messages = messages;
		this.thisLevel = level;
	}
	
	public void setLevel(Level level){
		this.thisLevel = level;
	}

	public EnemyEntity newEnemy(String entityName){
        try{ mobFile = openMobFile(mobFileName); }
        catch(FileNotFoundException e){ System.out.println(e.getMessage()); }
        String tokens[];
        EnemyEntity newEnemy = new EnemyEntity(thisLevel);

        boolean found = false;

        while (mobFile.hasNextLine()) {

            String tempLine = mobFile.nextLine();
            tokens = tempLine.split(":", 2);

            if(found && tempLine.isEmpty()){
                break;
            }
            if ((tempLine.isEmpty()) || (tempLine.contains("<>"))) {
                continue;
            }
            if (tokens[0].trim().equals("name") && tokens[1].trim().equals(entityName)) {
                found = true;
            }
            if (found) {
                newEnemy.setAttributes(tokens[0].trim(), tokens[1].trim());
            }
        }

        thisLevel.addAtEmptyLocation(newEnemy);

        mobFile.close();
        return newEnemy;
    }

	public EnemyEntity newEnemyAtSpecificLocation(String entityName, int x, int y){
		try{ mobFile = openMobFile(mobFileName); }
		catch(FileNotFoundException e){ System.out.println(e.getMessage()); }
		String tokens[];
		EnemyEntity newEnemy = new EnemyEntity(thisLevel);

		boolean found = false;

		while (mobFile.hasNextLine()) {
			String tempLine = mobFile.nextLine();
			tokens = tempLine.split(":", 2);

			if(found && tempLine.isEmpty()){
				break;
			}
			if ((tempLine.isEmpty()) || (tempLine.contains("<>"))) {
				continue;
			}
			if (tokens[0].trim().equals("name") && tokens[1].trim().equals(entityName)) {
				found = true;
			}
			if (found) {
				newEnemy.setAttributes(tokens[0].trim(), tokens[1].trim());
			}
		}

		thisLevel.addAtSpecificLocation(newEnemy, x, y);

		mobFile.close();
		return newEnemy;
	}
	
	public Player newPlayer(){
		try{
			playerFile = openMobFile(playerFileName);
		}
		catch(FileNotFoundException e){
			System.out.println(e.getMessage());
		}
		String tokens[];
        Player newPlayer = new Player(this.thisLevel);
        newPlayer.setAttribute("color", "white");
        newPlayer.setAttribute("symbol", "@");
		while(playerFile.hasNextLine()){
			String tempLine = playerFile.nextLine();
			tokens = tempLine.split(":", 2);
			if(!tokens[0].trim().equals("Name")) {
                newPlayer.setAttribute(tokens[0].trim(), tokens[1].trim());
            }
		}
		new playerAI(newPlayer, messages);
		return newPlayer;
	}
	
	private static Scanner openMobFile(String fileName) throws FileNotFoundException{
		return new Scanner(MobStore.class.getResourceAsStream(fileName));
	}
}
