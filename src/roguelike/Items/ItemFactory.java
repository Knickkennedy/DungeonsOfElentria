package roguelike.Items;

import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import asciiPanel.AsciiPanel;
import roguelike.Level.Level;
import roguelike.utility.RandomGen;

public class ItemFactory {
	public String itemFileName = "assets/Items.txt";
	public Scanner itemFile = null;
	public Level thisLevel;
	public HashMap <String, Color> colorDictionary = new HashMap <String, Color> ();
	public HashMap <String, BaseItem> itemDictionary = new HashMap <String, BaseItem> ();
	public List <BaseItem> itemList = new ArrayList <BaseItem> ();
	
	public ItemFactory(Level level){
		initializeColors();
		initializeItemDictionary();
		this.thisLevel = level;
	}
	
	public void setThisLevel(Level level){ this.thisLevel = level; }
	
	public void initializeItemDictionary(){
		try{
			itemFile = openItemFile(itemFileName);
		}
		catch(FileNotFoundException e){
			System.out.println(e.getMessage());
		}
		
		String name = null, itemType = null, effect;
		char glyph = 0;
		Color color = null;
		int toHitBonus = 0, numOfDice = 0, attack = 0, attackBonus = 0, dodge = 0, armor = 0;
		double weight = 0.0;
		String[] tokens = null, effects = null;
		
		while(itemFile.hasNextLine()){
			String tempLine = itemFile.nextLine();
			if((tempLine.isEmpty()) || (tempLine.contains("CHESTPIECES"))
				|| (tempLine.contains("HELMETS"))
				|| (tempLine.contains("WEAPONS"))
				|| (tempLine.contains("LEGGINGS"))
				|| (tempLine.contains("BRACERS"))
				|| (tempLine.contains("BOOTS"))
				|| (tempLine.contains("POTIONS"))){
				continue;
			}
			tokens = tempLine.split(":");
			if(tokens.length == 6 && !tokens[0].trim().equals("name")){
				name = tokens[0].trim();
				itemType = tokens[1].trim();
				glyph = tokens[2].trim().charAt(0);
				color = colorDictionary.get(tokens[3].trim());
				weight = Double.parseDouble(tokens[4].trim());
				effect = tokens[5].trim();
				
				Potion newPotion = new Potion(name, glyph, color, itemType, weight, effect);
				itemDictionary.put(name, newPotion);
				itemList.add(newPotion);
			}
			else if(tokens.length == 9 && !tokens[0].trim().equals("name")){
				name = tokens[0].trim();
				itemType = tokens[1].trim();
				glyph = tokens[2].trim().charAt(0);
				color = colorDictionary.get(tokens[3].trim());
				toHitBonus = Integer.parseInt(tokens[4].trim());
				attack = Integer.parseInt(tokens[5].trim());
				dodge = Integer.parseInt(tokens[6].trim());
				armor = Integer.parseInt(tokens[7].trim());
				weight = Double.parseDouble(tokens[8].trim());
				
				if(itemType.equals("chestpiece")){
					Chestpiece newChestpiece = new Chestpiece(name, glyph, color, itemType, weight, toHitBonus, attack, dodge, armor);
					itemDictionary.put(name, newChestpiece);
					itemList.add(newChestpiece);
				}
				else if(itemType.equals("helmet")){
					Helmet newHelmet = new Helmet(name, glyph, color, itemType, weight, toHitBonus, attack, dodge, armor);
					itemDictionary.put(name, newHelmet);
					itemList.add(newHelmet);
				}
				else if(itemType.equals("cuisses")){
					Cuisses newCuisses = new Cuisses(name, glyph, color, itemType, weight, toHitBonus, attack, dodge, armor);
					itemDictionary.put(name, newCuisses);
					itemList.add(newCuisses);
				}
				else if(itemType.equals("greaves")){
					Greaves newGreaves = new Greaves(name, glyph, color, itemType, weight, toHitBonus, attack, dodge, armor);
					itemDictionary.put(name, newGreaves);
					itemList.add(newGreaves);
				}
				else if(itemType.equals("boots")){
					Boots newBoots = new Boots(name, glyph, color, itemType, weight, toHitBonus, attack, dodge, armor);
					itemDictionary.put(name, newBoots);
					itemList.add(newBoots);
				}
			}
			else if(tokens.length == 11 && !tokens[0].trim().equals("name")){
				name = tokens[0].trim();
				itemType = tokens[1].trim();
				glyph = tokens[2].trim().charAt(0);
				color = colorDictionary.get(tokens[3].trim());
				toHitBonus = Integer.parseInt(tokens[4].trim());
				numOfDice = Integer.parseInt(tokens[5].trim());
				attack = Integer.parseInt(tokens[6].trim());
				attackBonus = Integer.parseInt(tokens[7].trim());
				dodge = Integer.parseInt(tokens[8].trim());
				armor = Integer.parseInt(tokens[9].trim());
				weight = Double.parseDouble(tokens[10].trim());
				
				Weapon newWeapon = new Weapon(name, glyph, color, itemType, weight, toHitBonus, numOfDice, attack, attackBonus, dodge, armor);
				itemDictionary.put(name, newWeapon);
				itemList.add(newWeapon);
			}
		}
		itemFile.close();
	}
	
	public void initializeColors(){
		colorDictionary.put("brightGreen", AsciiPanel.brightGreen);
		colorDictionary.put("blue", AsciiPanel.brightBlue.brighter().brighter().brighter());
		colorDictionary.put("brightWhite", AsciiPanel.brightWhite);
		colorDictionary.put("darkGreen", AsciiPanel.green.brighter());
		colorDictionary.put("brown", new Color(139, 69, 19));
		colorDictionary.put("gray", new Color(211, 211, 211).darker());
		colorDictionary.put("white", AsciiPanel.white);
	}
	
	public BaseItem newItem(String itemName){
		try{ itemFile = openItemFile(itemFileName); }
		catch(FileNotFoundException e){ System.out.println(e.getMessage());}
		
		String name = null, itemType = null, effect = null;
		char glyph = 0;
		Color color = null;
		int toHitBonus = 0, numOfDice = 0, attack = 0, attackBonus = 0, dodge = 0, armor = 0;
		double weight = 0.0;
		String[] tokens = null, effects = null;
		
		while(itemFile.hasNextLine()){
			String tempLine = itemFile.nextLine();
			if((tempLine.isEmpty()) || (tempLine.contains("CHESTPIECES"))
				|| (tempLine.contains("HELMETS"))
				|| (tempLine.contains("WEAPONS"))
				|| (tempLine.contains("LEGGINGS"))
				|| (tempLine.contains("BRACERS"))
				|| (tempLine.contains("BOOTS"))
				|| (tempLine.contains("POTIONS"))){
				continue;
			}
			tokens = tempLine.split(":");
			if(tokens.length == 6 && !tokens[0].trim().equals("name") && (tokens[0].trim().equals(itemName))){
				name = tokens[0].trim();
				itemType = tokens[1].trim();
				glyph = tokens[2].trim().charAt(0);
				color = colorDictionary.get(tokens[3].trim());
				weight = Double.parseDouble(tokens[4].trim());
				effect = tokens[5].trim();
			}
			else if(tokens.length == 9 && !tokens[0].trim().equals("name") && (tokens[0].trim().equals(itemName))){
				name = tokens[0].trim();
				itemType = tokens[1].trim();
				glyph = tokens[2].trim().charAt(0);
				color = colorDictionary.get(tokens[3].trim());
				toHitBonus = Integer.parseInt(tokens[4].trim());
				attack = Integer.parseInt(tokens[5].trim());
				dodge = Integer.parseInt(tokens[6].trim());
				armor = Integer.parseInt(tokens[7].trim());
				weight = Double.parseDouble(tokens[8].trim());
			}
			else if(tokens.length == 11 && !tokens[0].trim().equals("name") && (tokens[0].trim().equals(itemName))){
				name = tokens[0].trim();
				itemType = tokens[1].trim();
				glyph = tokens[2].trim().charAt(0);
				color = colorDictionary.get(tokens[3].trim());
				toHitBonus = Integer.parseInt(tokens[4].trim());
				numOfDice = Integer.parseInt(tokens[5].trim());
				attack = Integer.parseInt(tokens[6].trim());
				attackBonus = Integer.parseInt(tokens[7].trim());
				dodge = Integer.parseInt(tokens[8].trim());
				armor = Integer.parseInt(tokens[9].trim());
				weight = Double.parseDouble(tokens[10].trim());
			}
		}
		if(itemType.equals("chestpiece")){
			Chestpiece newChestpiece = new Chestpiece(name, glyph, color, itemType, weight, toHitBonus, attack, dodge, armor);
			return newChestpiece;
		}
		else if(itemType.equals("helmet")){
			Helmet newHelmet = new Helmet(name, glyph, color, itemType, weight, toHitBonus, attack, dodge, armor);
			return newHelmet;
		}
		else if(itemType.equals("cuisses")){
			Cuisses newCuisses = new Cuisses(name, glyph, color, itemType, weight, toHitBonus, attack, dodge, armor);
			return newCuisses;
		}
		else if(itemType.equals("greaves")){
			Greaves newGreaves = new Greaves(name, glyph, color, itemType, weight, toHitBonus, attack, dodge, armor);
			return newGreaves;
		}
		else if(itemType.equals("boots")){
			Boots newBoots = new Boots(name, glyph, color, itemType, weight, toHitBonus, attack, dodge, armor);
			newBoots.name();
			return newBoots;
		}
		else if(itemType.equals("potion")){
			Potion newPotion = new Potion(name, glyph, color, itemType, weight, effect);
			return newPotion;
		}
		else if(itemType.equals("weapon")){
			Weapon newWeapon = new Weapon(name, glyph, color, itemType, weight, toHitBonus, numOfDice, attack, attackBonus, dodge, armor);
			return newWeapon;
		}
		else{
			return null;
		}
	}
	
	public void newItemAtRandomLocation(){
		int roll = RandomGen.rand(0, itemDictionary.size() - 1);
		this.thisLevel.addAtEmptyLocation(newItem(itemList.get(roll).name()));
	}
	
	private static Scanner openItemFile(String fileName) throws FileNotFoundException{
		File file = new File(fileName);
		Scanner sc = new Scanner(file);
		return sc;
	}
}
