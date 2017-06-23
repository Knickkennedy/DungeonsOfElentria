package roguelike.Mob;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import asciiPanel.AsciiPanel;
import roguelike.AI.AggressiveAI;
import roguelike.AI.playerAi;
import roguelike.Level.Level;

public class MobStore {
	public String mobFileName = "/Mobs.txt";
	public String playerFileName = "/HumanPlayer.txt";
	public Scanner playerFile = null;
	public Scanner mobFile = null;
	public Level thisLevel;
	public HashMap <String, Color> colorDictionary = new HashMap <String, Color> ();
	public HashMap <Integer, String> enemyDictionary = new HashMap <> ();
	public List <String> messages;
	private int enemyCount;
	
	public void initializeColors(){
		colorDictionary.put("brightGreen", AsciiPanel.brightGreen);
		colorDictionary.put("blue", AsciiPanel.brightBlue.brighter().brighter().brighter());
		colorDictionary.put("brightWhite", AsciiPanel.brightWhite);
		colorDictionary.put("darkGreen", AsciiPanel.green.brighter());
	}
	
	public void initializeEnemyDictionary(){
		try{ mobFile = openMobFile(mobFileName); }
		catch(FileNotFoundException e){ System.out.println(e.getMessage()); }
		
		String name = null;
		String[] tokens = null;
		
		while(mobFile.hasNextLine()){
			String tempLine = mobFile.nextLine();
			if((tempLine.isEmpty()) || (tempLine.contains("INSECT FAMILY")) || (tempLine.contains("GOBLIN FAMILY")) || (tempLine.contains("BOSSES"))){
				continue;
			}
			tokens = tempLine.split(":");
			if(!tokens[0].trim().equals("name")){
				name = tokens[0].trim();
				enemyDictionary.put(enemyCount, name);
				enemyCount++;
			}
		}
		
		mobFile.close();
	}
	
	public MobStore(Level level, List <String> messages){
		enemyCount = 0;
		initializeColors();
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
		
		String name = null, ai = null;
		char glyph = 0;
		Color color = null;
		int HP = 0, attack = 0, armor = 0, dodge = 0;
		String[] tokens = null, effects = null;
		
		while(mobFile.hasNextLine()){
			String tempLine = mobFile.nextLine();
			tokens = tempLine.split(":");
			if(tokens[0].trim().equals(entityName)){
				name = tokens[0].trim();
				glyph = tokens[1].trim().charAt(0);
				color = colorDictionary.get(tokens[2].trim());
				HP = Integer.parseInt(tokens[3].trim());
				attack = Integer.parseInt(tokens[4].trim());
				armor = Integer.parseInt(tokens[5].trim());
				dodge = Integer.parseInt(tokens[6].trim());
				effects = tokens[7].trim().split(", ");
				ai = tokens[8].trim();
			}
		}
		
		EnemyEntity newEnemy = new EnemyEntity(thisLevel, glyph, color);
		newEnemy.setName(name);
		newEnemy.setAttack(attack);
		newEnemy.setArmor(armor);
		newEnemy.setMaxHP(HP);
		newEnemy.setDodge(dodge);
		newEnemy.setIsPlayer(false);
		newEnemy.setVisionRadius(10);
		
		thisLevel.addAtEmptyLocation(newEnemy);
		
		if(ai.equals("aggressive")){
			new AggressiveAI(newEnemy, thisLevel.player);
		}
		
		for(String effect : effects){
			if(effect.equals("weak poison")){
				newEnemy.setPoisonType(effect);
			}
			else if(effect.equals("strong poison")){
				newEnemy.setPoisonType(effect);
			}
		}
		
		mobFile.close();
		return newEnemy;
	}
	
	public Player newPlayer(){
		Player newPlayer = newPlayer("@");
		new playerAi(newPlayer, messages);
		return newPlayer;
	}
	
	public Player newPlayer(String playerSymbol){
		try{
			playerFile = openMobFile(playerFileName);
		}
		catch(FileNotFoundException e){
			System.out.println(e.getMessage());
		}
		char glyph = 0;
		Color color = null;
		int strength = 0, constitution = 0, dexterity = 0, intelligence = 0, wisdom = 0, charisma = 0, perception = 0;
		String[] tokens;
		while(playerFile.hasNextLine()){
			String tempLine = playerFile.nextLine();
			tokens = tempLine.split(":");
			if(tokens[0].trim().equals("@")){
				glyph = tokens[0].trim().charAt(0);
				color = colorDictionary.get(tokens[1].trim());
				strength = Integer.parseInt(tokens[2].trim());
				constitution = Integer.parseInt(tokens[3].trim());
				dexterity = Integer.parseInt(tokens[4].trim());
				intelligence = Integer.parseInt(tokens[5].trim());
				wisdom = Integer.parseInt(tokens[6].trim());
				charisma = Integer.parseInt(tokens[7].trim());
				perception = Integer.parseInt(tokens[8].trim());
			}
		}
		Player newPlayer = new Player(this.thisLevel, glyph, color, strength, constitution, dexterity, intelligence, wisdom, charisma, perception);
		return newPlayer;
	}
	
	private static Scanner openMobFile(String fileName) throws FileNotFoundException{
		Scanner sc = new Scanner(MobStore.class.getResourceAsStream(fileName));
		return sc;
	}
}
