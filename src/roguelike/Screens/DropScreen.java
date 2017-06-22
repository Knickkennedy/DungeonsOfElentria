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

public class DropScreen implements Screen{
	private Player player;
	public String alphabet = "abcdefghijklmnopqrstuvwxyz";
	public Map <String, Integer> tempList = new TreeMap <String, Integer> ();
	public List <BaseItem> dropList = new ArrayList <BaseItem> ();
	private Screen subscreen;

	public DropScreen(Player player){
		this.player = player;
		initializeTempList();
	}
	
	public void initializeTempList(){
		for(BaseItem itemToRead : player.inventory().getInventory()){
			Integer frequency = tempList.get(itemToRead.name());
			if(frequency == null){
				tempList.put(itemToRead.name(), new Integer(1));
				dropList.add(itemToRead);
			}
			else{
				frequency++;
				tempList.put(itemToRead.name(), frequency);
			}
		}
	}
	
	public void displayOutput(AsciiPanel terminal){
		String stats = String.format("Con: %s Str: %s Dex: %s Int: %s Wis: %s Cha: %s", player.constitution(), player.strength(), player.dexterity(), player.intelligence(), player.wisdom(), player.charisma());
		String weight = String.format("Currently Carrying: %s      Carrying Capacity: %s", player.currentCarryWeight(), player.maxCarryWeight());
		terminal.clear(' ', 0, 0, 88, 28);
		terminal.writeCenter(stats, 1);
		terminal.writeCenter(weight, 2);
		for(int i = 5; i < 83; i++){
			terminal.write('_', i, 3);
		}
		Set<Map.Entry<String, Integer>> allItems = tempList.entrySet();
		Iterator<Map.Entry<String, Integer>> keyIterator = allItems.iterator();
		int i = 0;
		while(keyIterator.hasNext()){
			Map.Entry<String, Integer> element = keyIterator.next();
			terminal.write(alphabet.charAt(i) + " - " + element.getKey() + " x " + element.getValue(), 1, 4 + i);
			i++;
		}
		
		if(subscreen != null){
			subscreen.displayOutput(terminal);
		}
	}
	
	public Screen respondToUserInput(KeyEvent key){
char characterInput = key.getKeyChar();
		
		if(alphabet.indexOf(characterInput) > -1
			&& tempList.size() > alphabet.indexOf(characterInput)){
			player.dropItem(dropList.get(alphabet.indexOf(characterInput)));
		}
		
		return null;
	}
}
