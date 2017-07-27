package roguelike.screens;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import asciiPanel.AsciiPanel;
import roguelike.mob.Player;

public class EquipmentScreen implements Screen{
	public Player player;
	private Screen subscreen;
	private String alphabet;
	
	public EquipmentScreen(Player player){
		this.player = player;
		alphabet = "abcdefghijklmnopqrstuvwxyz";
	}
	
	public void displayOutput(AsciiPanel terminal){
		List <String> lines = new ArrayList<>();
		int y = 1;
		String stats = String.format("Con: %s Str: %s Dex: %s Int: %s Wis: %s Cha: %s Per: %s", player.constitution(), player.strength(), player.dexterity(), player.intelligence(), player.wisdom(), player.charisma(), player.perception());
		String weight = String.format("Currently Carrying: %s      Carrying Capacity: %s", player.currentCarryWeight(), player.getMaxCarryWeight());
		String helmet = String.format(" a - %-15s : - %s", "Helmet", player.helmetString());
		String armor = String.format(" b - %-15s : - %s", "Chest Piece", player.chestpieceString());
		String rightHand = String.format(" c - %-15s : - %s", "Right Hand", player.rightHandString());
		String leftHand = String.format(" d - %-15s : - %s", "Left Hand", player.leftHandString());
		String legs = String.format(" e - %-15s : - %s", "Cuisses", player.cuissesString());
		String ankles = String.format(" f - %-15s : - %s", "Greaves", player.greavesString());
		String boots = String.format(" g - %-15s : - %s", "Boots", player.bootsString());
		String ranged = String.format(" h - %-15s : - %s", "Ranged Weapon", player.rangedWeaponString());
		String arrows = String.format(" i - %-15s : - %s", "Ammunition", player.rangedAmmunitionString());
		lines.add(helmet);
		lines.add(armor);
		lines.add(rightHand);
		lines.add(leftHand);
		lines.add(legs);
		lines.add(ankles);
		lines.add(boots);
		lines.add(ranged);
		lines.add(arrows);
		terminal.clear(' ', 0, 0, 88, 28);
		terminal.writeCenter(stats, y++);
		terminal.writeCenter(weight, y++);
		for(int i = 5; i < 83; i++){
			terminal.write('_', i, y);
		}
		for(String line : lines){
			terminal.write(line, 0, ++y);
		}
		if(subscreen != null){
			subscreen.displayOutput(terminal);
		}
	}

	public Screen respondToUserInput(KeyEvent key){
		
		if(subscreen != null){
			subscreen = subscreen.respondToUserInput(key);
		}
		else{
		switch (key.getKeyCode()){
		case KeyEvent.VK_ESCAPE: {
			return null;
		}
		case KeyEvent.VK_A: {
			if(player.getHelmet() != null){
				player.unequipHelmet();
			}
			else{
				subscreen = new EquipScreen(player, "helmet", 'A');
			}
			break;
		}
		case KeyEvent.VK_B: {
			if(player.getChestpiece() != null){
				player.unequipChestpiece();
			}
			else{
				subscreen = new EquipScreen(player, "chestpiece", 'B');
			}
			break;
		}
		case KeyEvent.VK_C: {
			if(player.getRightHand() != null){
				player.unequipRightHand();
			}
			else{
				subscreen = new EquipScreen(player, "melee weapon", 'C');
			}
			break;
		}
            case KeyEvent.VK_D: {
                if(player.getLeftHand() != null){
                    player.unequipLeftHand();
                }
                else{
                    subscreen = new EquipScreen(player, "melee weapon", 'D');
                }
                break;
            }
		case KeyEvent.VK_E: {
			if(player.getCuisses() != null){
				player.unequipCuisses();
			}
			else{
				subscreen = new EquipScreen(player, "cuisses", 'E');
			}
			break;
		}
		case KeyEvent.VK_F: {
			if(player.getGreaves() != null){
				player.unequipGreaves();
			}
			else{
				subscreen = new EquipScreen(player, "greaves", 'F');
			}
			break;
		}
		case KeyEvent.VK_G: {
			if(player.getBoots() != null){
				player.unequipBoots();
			}
			else{
				subscreen = new EquipScreen(player, "boots", 'G');
			}
			break;
		}
            case KeyEvent.VK_H:{
                if(player.getRangedWeapon() != null){
                    player.unequipRangedWeapon();
                }
                else{
                    subscreen = new EquipScreen(player, "ranged weapon", 'H');
                }
                break;
            }
			case KeyEvent.VK_I:{
				if(!player.getRangedAmmunition().isEmpty()){
					player.unequipRangedAmmunition();
				}
				else{
					subscreen = new EquipScreen(player, "ammunition", 'I');
				}
			}
		}
		}
		return this;
	}
}
