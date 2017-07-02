package roguelike.Screens;

import asciiPanel.AsciiPanel;
import java.util.List;
import java.util.ArrayList;

import java.awt.event.KeyEvent;

public class HelpScreen implements Screen{

    private List <String> information;

    public HelpScreen(){
        information = new ArrayList<>();
        initializeStrings();
    }

    public void initializeStrings(){
        information.add(" To melee attack, simply press the movement key towards the monster");
        information.add("");
        information.add(" 1 - Move Southwest");
        information.add(" 2 - Move South");
        information.add(" 3 - Move Southeast");
        information.add(" 4 - Move West");
        information.add(" 5 - Wait a turn");
        information.add(" 6 - Move East");
        information.add(" 7 - Move Northwest");
        information.add(" 8 - Move North");
        information.add(" 9 - Move Northeast");
        information.add("");
        information.add(" d - Drop an item from your inventory");
        information.add(" D - Drink a potion in your inventory");
        information.add(" i - Show current items equipped");
        information.add(" I - Show the items you are carrying");
        information.add(" p - Pick up an item from the ground");
        information.add(" t - Use a ranged weapon if you have one equipped");
        information.add(" > - Go down a staircase if one is present");
        information.add(" < - Go up a staircase if one is present");
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.clear();
        for(int i = 0; i < terminal.getHeightInCharacters(); i++){
            terminal.write('#', 0, i, AsciiPanel.brightBlack);
            terminal.write('#', terminal.getWidthInCharacters() - 1, i, AsciiPanel.brightBlack);
        }
        for(int i = 0; i < terminal.getWidthInCharacters(); i++){
            terminal.write('#', i, 0, AsciiPanel.brightBlack);
            terminal.write('#', i, 5, AsciiPanel.brightBlack);
            terminal.write('#', i, terminal.getHeightInCharacters() - 1, AsciiPanel.brightBlack);
        }
        terminal.writeCenter("Dungeons of Elentria", 2);
        terminal.writeCenter("Help Screen", 3);

        for(int i = 0; i < information.size() - 1; i++){
            terminal.write(information.get(i), 1, i + 7);
        }
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        return null;
    }
}
