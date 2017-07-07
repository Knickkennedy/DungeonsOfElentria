package roguelike.Screens;

import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;
import roguelike.Mob.Player;

public class EquipmentScreen implements Screen{
	public Player player;
	private Screen subscreen;
	//private String alphabet = "abcdefghijklmnopqrstuvwxyz";
	
	public EquipmentScreen(Player player){
		this.player = player;
	}
	
	public void displayOutput(AsciiPanel terminal){
		
		String stats = String.format("Con: %s Str: %s Dex: %s Int: %s Wis: %s Cha: %s Per: %s", player.constitution(), player.strength(), player.dexterity(), player.intelligence(), player.wisdom(), player.charisma(), player.perception());
		String weight = String.format("Currently Carrying: %s      Carrying Capacity: %s", player.currentCarryWeight(), player.maxCarryWeight());
		String helmet = String.format(" a - %-15s : - %s", "Helmet", player.helmetString());
		String armor = String.format(" b - %-15s : - %s", "Chest Piece", player.chestpieceString());
		String rightHand = String.format(" c - %-15s : - %s", "Right Hand", player.rightHandString());
		String leftHand = String.format(" d - %-15s : - %s", "Left Hand", player.leftHandString());
		String legs = String.format(" e - %-15s : - %s", "Cuisses", player.cuissesString());
		String ankles = String.format(" f - %-15s : - %s", "Greaves", player.greavesString());
		String boots = String.format(" g - %-15s : - %s", "Boots", player.bootsString());
		String ranged = String.format(" h - %-15s : - %s", "Ranged Weapon", player.rangedWeaponString());
		String arrows = String.format(" i - %-15s : - %s", "Ammunition", player.rangedAmmunitionString());
		terminal.clear(' ', 0, 0, 88, 28);
		terminal.writeCenter(stats, 1);
		terminal.writeCenter(weight, 2);
		terminal.write(helmet, 0, 4);
		terminal.write(armor, 0, 5);
		terminal.write(rightHand, 0, 6);
		terminal.write(leftHand, 0, 7);
		terminal.write(legs, 0, 8);
		terminal.write(ankles, 0, 9);
		terminal.write(boots, 0, 10);
		terminal.write(ranged, 0, 11);
		terminal.write(arrows, 0, 12);
		for(int i = 5; i < 83; i++){
			terminal.write('_', i, 3);
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
