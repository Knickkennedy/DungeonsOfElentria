package roguelike.Screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import asciiPanel.AsciiPanel;
import roguelike.Items.*;
import roguelike.Mob.Player;

public class EquipScreen implements Screen{
	public Player player;
	public String alphabet = "abcdefghijklmnopqrstuvwxyz";
	public String itemToEquip;
	public TreeMap <String, Integer> tempList = new TreeMap <String, Integer> ();
	public List <BaseItem> equipList = new ArrayList <BaseItem> ();
	
	public EquipScreen(Player player, String itemToEquip){
		this.player = player;
		this.itemToEquip = itemToEquip;
		initializeTempList();
	}
	
	public void initializeTempList(){
		for(BaseItem possibleItems : player.inventory().getInventory()){
			if(possibleItems.itemType().equals(itemToEquip)){
				Integer frequency = tempList.get(possibleItems.name());
				if(frequency == null){
					tempList.put(possibleItems.name(), new Integer(1));
					equipList.add(possibleItems);
				}
				else{
					frequency++;
					tempList.put(possibleItems.name(), frequency);
				}
			}
		}
	}
	
	public void displayOutput(AsciiPanel terminal){
		String stats = String.format("Con: %s Str: %s Dex: %s Int: %s Wis: %s Cha: %s", player.constitution(), player.strength(), player.dexterity(), player.intelligence(), player.wisdom(), player.charisma());
		String weight = String.format("Currently Carrying: %s      Carrying Capacity: %s", player.currentCarryWeight(), player.maxCarryWeight());
		terminal.clear(' ', 0, 4, 88, 24);
		terminal.writeCenter(stats, 1);
		terminal.writeCenter(weight, 2);
		Set<Map.Entry<String, Integer>> allItems = tempList.entrySet();
		Iterator<Map.Entry<String, Integer>> keyIterator = allItems.iterator();
		int i = 0;
		while(keyIterator.hasNext()){
			Map.Entry<String, Integer> element = keyIterator.next();
			terminal.write(alphabet.charAt(i) + " - " + element.getKey() + " x " + element.getValue(), 1, 4 + i);
			i++;
		}
	}
	
	public Screen respondToUserInput(KeyEvent key){
		char characterInput = key.getKeyChar();
		
		if(alphabet.indexOf(characterInput) > -1
			&& tempList.size() > alphabet.indexOf(characterInput)){
			player.equipItem(equipList.get(alphabet.indexOf(characterInput)));
		}
		
		return null;
	}
}
