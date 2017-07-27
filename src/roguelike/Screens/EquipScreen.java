package roguelike.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import asciiPanel.AsciiPanel;
import roguelike.items.*;
import roguelike.mob.Player;

public class EquipScreen implements Screen{
	public Player player;
	public String alphabet = "abcdefghijklmnopqrstuvwxyz";
	public String itemToEquip;
	public char input;
	public TreeMap <Item, Integer> tempList = new TreeMap <> ();
	public List <Item> equipList = new ArrayList <> ();
	
	public EquipScreen(Player player, String itemToEquip, char inp){
		this.player = player;
		this.itemToEquip = itemToEquip;
		this.input = inp;
		initializeTempList();
	}
	
	public void initializeTempList(){
		for(Item possibleItems : player.getInventory().getItems()){
			if(possibleItems.getItemType().contains(itemToEquip)){
				Integer frequency = tempList.get(possibleItems);
				if(frequency == null){
					tempList.put(possibleItems, new Integer(1));
				}
				else{
					frequency++;
					tempList.put(possibleItems, frequency);
				}
			}
		}
	}
	
	public void displayOutput(AsciiPanel terminal){
		String stats = String.format("Con: %s Str: %s Dex: %s Int: %s Wis: %s Cha: %s Per: %s", player.getConstitution(), player.getStrength(), player.getDexterity(), player.getIntelligence(), player.getWisdom(), player.getCharisma(), player.getPerception());
		String weight = String.format("Currently Carrying: %s      Carrying Capacity: %s", player.currentCarryWeight(), player.getMaxCarryWeight());
		terminal.clear(' ', 0, 4, 88, 24);
		terminal.writeCenter(stats, 1);
		terminal.writeCenter(weight, 2);
		Set<Map.Entry<Item, Integer>> allItems = tempList.entrySet();
		Iterator<Map.Entry<Item, Integer>> keyIterator = allItems.iterator();
		int i = 0;
		while(keyIterator.hasNext()){
			Map.Entry<Item, Integer> element = keyIterator.next();
			equipList.add(element.getKey());
			terminal.write(alphabet.charAt(i) + " - " + element.getKey().getName() + " x " + element.getValue(), 1, 4 + i);
			i++;
		}
	}
	
	public Screen respondToUserInput(KeyEvent key){
		char characterInput = key.getKeyChar();
		
		if(alphabet.indexOf(characterInput) > -1
			&& tempList.size() > alphabet.indexOf(characterInput)){
			player.equipItem(equipList.get(alphabet.indexOf(characterInput)), input);
			return null;
		}

		switch(key.getKeyCode()){
			case KeyEvent.VK_ESCAPE: {
				return null;
			}
		}
		
		return this;
	}
}
