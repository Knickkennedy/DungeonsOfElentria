package roguelike.World;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import roguelike.Items.Item;
import roguelike.Mob.*;

public class Factory {
	private String itemFileName = "/Items.txt";
    private String mobFileName = "/Mobs.txt";
    private String spellFileName = "/Spells.txt";
    private Scanner itemFile = null;
    private Scanner mobFile = null;
    private Scanner playerFile = null;
    private Scanner spellFile = null;
	public HashMap <Integer, String> dangerOneItems = new HashMap<>();
	public HashMap <Integer, String> dangerTwoItems = new HashMap<>();
	public HashMap <Integer, String> dangerThreeItems = new HashMap<>();
	public HashMap <String, List<String>> itemsByCategory = new HashMap<>();
    public HashMap <Integer, String> dangerOneEnemies = new HashMap <> ();
    public HashMap <Integer, String> dangerTwoEnemies = new HashMap <> ();
    public HashMap <Integer, String> dangerThreeEnemies = new HashMap <> ();
	
	public Factory(){
		initializeItemDictionary();
		initializeEnemyDictionary();
	}

    public Spell getSpell(String spellName){
        try{ spellFile = openFile(spellFileName); }
        catch(FileNotFoundException e){ System.out.println(e.getMessage()); }

        Spell spellToReturn = null;
        String[] parameters = null;

        while(spellFile.hasNextLine()){
            String line = spellFile.nextLine();
            String[] input = line.split("\\t+");
            if(input[0].trim().equalsIgnoreCase("NAME")){
                parameters = input;
            }
            if(input[0].trim().equalsIgnoreCase(spellName)){
                spellToReturn = new Spell(parameters, input);
            }
        }

        spellFile.close();
        return spellToReturn;
    }

    private void initializeEnemyDictionary(){
        try{ mobFile = openFile(mobFileName); }
        catch(FileNotFoundException e){ System.out.println(e.getMessage()); }

        String name = null;
        String[] tokens;
        int danger;

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

    public EnemyEntity newEnemy(String entityName){
        try{ mobFile = openFile(mobFileName); }
        catch(FileNotFoundException e){ System.out.println(e.getMessage()); }
        String tokens[];
        EnemyEntity newEnemy = new EnemyEntity();

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

        mobFile.close();
        return newEnemy;
    }

    public Player newPlayer(){
        try{
            String playerFileName = "/HumanPlayer.txt";
            playerFile = openFile(playerFileName);
        }
        catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }
        String tokens[];
        Player newPlayer = new Player();
        newPlayer.setAttribute("color", "white");
        newPlayer.setAttribute("symbol", "@");
        while(playerFile.hasNextLine()){
            String tempLine = playerFile.nextLine();
            tokens = tempLine.split(":", 2);
            if(!tokens[0].trim().equals("Name")) {
                newPlayer.setAttribute(tokens[0].trim(), tokens[1].trim());
            }
        }

        newPlayer.learnNewSpell(getSpell("magic missiles"));
        newPlayer.learnNewSpell(getSpell("flame bolt"));
        newPlayer.learnNewSpell(getSpell("healing hands"));
        newPlayer.learnNewSpell(getSpell("poison spit"));
        newPlayer.learnNewSpell(getSpell("fireball"));
        newPlayer.learnNewSpell(getSpell("invisibility"));

        return newPlayer;
    }
	
	private void initializeItemDictionary(){
		try{
			itemFile = openFile(itemFileName);
		}
		catch(FileNotFoundException e){
			System.out.println(e.getMessage());
		}
		itemsByCategory.put("Spears", new ArrayList<>());
		itemsByCategory.put("Swords", new ArrayList<>());
		itemsByCategory.put("Potions", new ArrayList<>());
		itemsByCategory.put("Bows", new ArrayList<>());
		itemsByCategory.put("Arrows", new ArrayList<>());
		itemsByCategory.put("Rocks", new ArrayList<>());
		itemsByCategory.put("Chestpieces", new ArrayList<>());
		itemsByCategory.put("Helmets", new ArrayList<>());
		itemsByCategory.put("Cuisses", new ArrayList<>());
		itemsByCategory.put("Greaves", new ArrayList<>());
		itemsByCategory.put("Boots", new ArrayList<>());
		String name = null;
		String[] tokens;
		int danger;
		
		while(itemFile.hasNextLine()){
			String tempLine = itemFile.nextLine();

			if((tempLine.isEmpty()) || (tempLine.contains("<>"))){
				continue;
			}

            tokens = tempLine.split(":");
            if (tokens[0].trim().equals("name")) {
                name = tokens[1].trim();
            }
            if(tokens[0].trim().equals("item type")){
                switch(tokens[1].trim()){
                    case "melee weapon - Sword": {
                        itemsByCategory.get("Swords").add(name);
                        break;
                    }
                    case "melee weapon - Spear":{
                        itemsByCategory.get("Spears").add(name);
                        break;
                    }
                    case "potion":{
                        itemsByCategory.get("Potions").add(name);
                        break;
                    }
                    case "ranged weapon - Bow":{
                        itemsByCategory.get("Bows").add(name);
                        break;
                    }
                    case "bow ammunition":{
                        itemsByCategory.get("Arrows").add(name);
                        break;
                    }
                    case "thrown ammunition":{
                        itemsByCategory.get("Rocks").add(name);
                        break;
                    }
                    case "chestpiece":{
                        itemsByCategory.get("Chestpieces").add(name);
                        break;
                    }
                    case "helmet":{
                        itemsByCategory.get("Helmets").add(name);
                        break;
                    }
                    case "cuisses":{
                        itemsByCategory.get("Cuisses").add(name);
                        break;
                    }
                    case "greaves":{
                        itemsByCategory.get("Greaves").add(name);
                        break;
                    }
                    case "boots":{
                        itemsByCategory.get("Boots").add(name);
                        break;
                    }
                }
            }
            if (tokens[0].trim().equals("danger level")) {
                danger = Integer.parseInt(tokens[1].trim());
                if (danger == 1) {
                    dangerOneItems.put(dangerOneItems.size() + 1, name);
                } else if (danger == 2) {
                    dangerTwoItems.put(dangerTwoItems.size() + 1, name);
                } else if (danger == 3) {
                    dangerThreeItems.put(dangerThreeItems.size() + 1, name);
                }
            }
        }
        itemFile.close();
	}

    public Item newItem(String itemName){
        try{ itemFile = openFile(itemFileName); }
        catch(FileNotFoundException e){ System.out.println(e.getMessage());}

        String[] tokens;
        Item newItem = new Item();

        boolean found = false;

        while(itemFile.hasNextLine()) {

            String tempLine = itemFile.nextLine();
            tokens = tempLine.split(":");

            if (tempLine.isEmpty() && found) {
                break;
            }
            if (tempLine.isEmpty() || tempLine.startsWith("<")) {
                continue;
            }
            if (tokens[0].trim().equals("name") && tokens[1].trim().equals(itemName)) {
                found = true;
            }
            if (found) {
                newItem.setAttribute(tokens[0].trim(), tokens[1].trim());
            }
        }

        itemFile.close();
        return newItem;
    }

    private static Scanner openFile(String fileName) throws FileNotFoundException{
        return new Scanner(Factory.class.getResourceAsStream(fileName));
    }
}
