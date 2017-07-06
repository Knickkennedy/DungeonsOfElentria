package roguelike.Items;

import java.awt.Color;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

import asciiPanel.AsciiPanel;
import roguelike.Level.Level;

public class ItemFactory {
	public String itemFileName = "/Items.txt";
	public Scanner itemFile = null;
	public Level thisLevel;
	public HashMap <String, Color> colorDictionary = new HashMap <> ();
	public HashMap <Integer, String> dangerOneItems = new HashMap<>();
	public HashMap <Integer, String> dangerTwoItems = new HashMap<>();
	public HashMap <Integer, String> dangerThreeItems = new HashMap<>();
	public HashMap <Integer, String> stackableItems = new HashMap<>();
	public HashMap <Integer, String> arrows = new HashMap<>();
	private int itemCount;
	
	public ItemFactory(Level level){
		itemCount = 0;
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
		
		String name = null;
		String[] tokens = null;
		int danger = 0;
		
		while(itemFile.hasNextLine()){
			String tempLine = itemFile.nextLine();

			if((tempLine.isEmpty()) || (tempLine.contains("<>"))){
				continue;
			}

            tokens = tempLine.split(":");
            if (tokens[0].trim().equals("name")) {
                name = tokens[1].trim();
            }
            if (tokens[0].trim().equals("danger level")) {
                if (tokens[1].trim().equals("stackable")) {
                    stackableItems.put(stackableItems.size() + 1, name);
                } else if(tokens[1].trim().equals("arrows")){
                    arrows.put(arrows.size() + 1, name);
                }
                else {
                    danger = Integer.parseInt(tokens[1].trim());
                }
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
	
	public void initializeColors(){
		colorDictionary.put("brightGreen", AsciiPanel.brightGreen);
		colorDictionary.put("blue", AsciiPanel.brightBlue.brighter().brighter().brighter());
		colorDictionary.put("brightWhite", AsciiPanel.brightWhite);
		colorDictionary.put("darkGreen", AsciiPanel.green.brighter());
		colorDictionary.put("brown", new Color(102, 51, 0));
		colorDictionary.put("gray", new Color(130, 130, 130));
		colorDictionary.put("white", AsciiPanel.white);
	}

    public Item newItem(String itemName){
        try{ itemFile = openItemFile(itemFileName); }
        catch(FileNotFoundException e){ System.out.println(e.getMessage());}

        String[] tokens = null;
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
	
	private static Scanner openItemFile(String fileName) throws FileNotFoundException{
		Scanner sc = new Scanner(ItemFactory.class.getResourceAsStream(fileName));
		return sc;
	}
}
