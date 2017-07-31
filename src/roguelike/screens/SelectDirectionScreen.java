package roguelike.screens;

import asciiPanel.AsciiPanel;
import roguelike.mob.Player;
import roguelike.mob.Spell;
import roguelike.utility.Point;

import java.awt.event.KeyEvent;

/**
 * Created by knich on 7/14/2017.
 */
public class SelectDirectionScreen implements Screen{
    private Player player;
    private Spell spell;

    public SelectDirectionScreen(Player player, Spell spellToCast){
        this.player = player;
        this.spell = spellToCast;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.clear(' ', 0, 0, PlayScreen.screenWidth, PlayScreen.messageBuffer);
        String spellPrompt = String.format("Which direction would you like to cast %s?", spell.getName());
        terminal.write(spellPrompt, 0, 0);
    }

    @Override
    public Screen respondToUserInput(KeyEvent key) {
        switch (key.getKeyCode()) {
            case KeyEvent.VK_NUMPAD4: {
                player.castSpell(spell, Point.WEST);
                break;
            }
            case KeyEvent.VK_NUMPAD6: {
                player.castSpell(spell, Point.EAST);
                break;
            }
            case KeyEvent.VK_NUMPAD8: {
                player.castSpell(spell, Point.NORTH);
                break;
            }
            case KeyEvent.VK_NUMPAD2: {
                player.castSpell(spell, Point.SOUTH);
                break;
            }
            case KeyEvent.VK_NUMPAD7: {
                player.castSpell(spell, Point.NORTH_WEST);
                break;
            }
            case KeyEvent.VK_NUMPAD9: {
                player.castSpell(spell, Point.NORTH_EAST);
                break;
            }
            case KeyEvent.VK_NUMPAD1: {
                player.castSpell(spell, Point.SOUTH_WEST);
                break;
            }
            case KeyEvent.VK_NUMPAD3: {
                player.castSpell(spell, Point.SOUTH_EAST);
                break;
            }
            case KeyEvent.VK_NUMPAD5: {
                player.castSpell(spell, Point.WAIT);
                break;
            }
        }
        return null;
    }
}
