package roguelike.screens;

import asciiPanel.AsciiPanel;
import roguelike.mob.Player;
import roguelike.utility.Point;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Created by knich on 7/13/2017.
 */
public class SelectSpellScreen implements Screen {
    private Player player;
    private String letters;

    public SelectSpellScreen(Player player){
        this.player = player;
        this.letters = "abcdefghijklmnopqrstuvwxyz";
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        ArrayList <String> lines = getList();
        int y = 1;
        String stats = String.format("Con: %s Str: %s Dex: %s Int: %s Wis: %s Cha: %s Per: %s", player.constitution(), player.strength(), player.dexterity(), player.intelligence(), player.wisdom(), player.charisma(), player.perception());
        String weight = String.format("Currently Carrying: %s      Carrying Capacity: %s", player.currentCarryWeight(), player.maxCarryWeight());
        terminal.clear(' ', 0, 0, 88, 28);
        terminal.writeCenter(stats, y++);
        terminal.writeCenter(weight, y++);
        for(int i = 5; i < 83; i++){
            terminal.write('_', i, y);
        }
        for(String line : lines){
            terminal.write(line, 1, ++y);
        }
    }

    private ArrayList <String> getList(){
        ArrayList <String> lines = new ArrayList<>();

        for(int i = 0; i < player.getKnownSpells().size(); i++){
            String line = letters.charAt(i) + " : " + player.getKnownSpells().get(i).getName() + " - " + player.getKnownSpells().get(i).getManaCost() + " mana";
            lines.add(line);
        }
        return lines;
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        char c = key.getKeyChar();

        if(key.getKeyCode() == KeyEvent.VK_ESCAPE){
            return null;
        }

        if(player.getKnownSpells().size() > letters.indexOf(c) && letters.indexOf(c) > -1 && player.getKnownSpells().get(letters.indexOf(c)) != null){
            if(player.getKnownSpells().get(letters.indexOf(c)).getCastType().equalsIgnoreCase("line")) return new SelectDirectionScreen(player, player.getKnownSpells().get(letters.indexOf(c)));
            else {
                player.castSpell(player.getKnownSpells().get(letters.indexOf(c)), Point.WAIT);
                return null;
            }
        }
        return this;
    }
}
