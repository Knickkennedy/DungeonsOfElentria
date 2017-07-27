package roguelike.screens;

import asciiPanel.AsciiPanel;
import roguelike.items.Item;
import roguelike.mob.Player;

import java.awt.event.KeyEvent;
import java.util.*;

public class ReadScreen implements Screen{
    public Player player;
    public String alphabet = "abcdefghijklmnopqrstuvwxyz";
    public TreeMap<String, Integer> tempList;
    public List<Item> useList;

    public ReadScreen(Player player){
        this.player = player;
        tempList = new TreeMap<>();
        useList = new ArrayList<>();
        initializeTempList();
    }

    public void initializeTempList(){
        for(Item possibleItems : player.getInventory().getItems()){
            if(possibleItems.getItemType().equalsIgnoreCase("book") || possibleItems.getItemType().equalsIgnoreCase("scroll")){
                Integer frequency = tempList.get(possibleItems.getName());
                if(frequency == null){
                    tempList.put(possibleItems.getName(), new Integer(1));
                    useList.add(possibleItems);
                }
                else{
                    frequency++;
                    tempList.put(possibleItems.getName(), frequency);
                }
            }
        }
    }

    public void displayOutput(AsciiPanel terminal){
        String stats = String.format("Con: %s Str: %s Dex: %s Int: %s Wis: %s Cha: %s Per: %s", player.getConstitution(), player.getStrength(), player.getDexterity(), player.getIntelligence(), player.getWisdom(), player.getCharisma(), player.getPerception());
        String weight = String.format("Currently Carrying: %s      Carrying Capacity: %s", player.currentCarryWeight(), player.getMaxCarryWeight());
        terminal.clear(' ', 0, 0, 88, 28);
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
            player.read(useList.get(alphabet.indexOf(characterInput)));
        }

        return null;
    }
}
