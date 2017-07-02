package roguelike.Screens;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public class HelpScreen implements Screen{
    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.clear();
        for(int i = 0; i < terminal.getHeightInCharacters(); i++){
            terminal.write('#', 0, i, AsciiPanel.brightBlack);
            terminal.write('#', terminal.getWidthInCharacters() - 1, i, AsciiPanel.brightBlack);
        }
        for(int i = 0; i < terminal.getWidthInCharacters(); i++){
            terminal.write('#', i, 0, AsciiPanel.brightBlack);
            terminal.write('#', i, 4, AsciiPanel.brightBlack);
            terminal.write('#', i, terminal.getHeightInCharacters() - 1, AsciiPanel.brightBlack);
        }
        terminal.writeCenter("Dungeons of Elentria", 2);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        return null;
    }
}
