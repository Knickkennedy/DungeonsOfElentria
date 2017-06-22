package roguelike.Mob;

import java.awt.Color;

import roguelike.Items.*;
import roguelike.Level.Level;
import roguelike.utility.RandomGen;

public class Player extends BaseEntity{
	
	private int strength, constitution, dexterity, intelligence, wisdom, charisma, perception, toHitBonus, damageModifier, numOfDice;
	private Weapon weapon;
	private Boots boots;
	private Greaves greaves;
	private Cuisses cuisses;
	private Chestpiece chestpiece;
	private Helmet helmet;
	
	public Player(Level level, char glyph, Color color, int strength, int constitution, int dexterity, int intelligence, int wisdom, int charisma, int perception){
		super(level, glyph, color);
		setStrength(strength);
		setConstitution(constitution);
		setDexterity(dexterity);
		setIntelligence(intelligence);
		setWisdom(wisdom);
		setCharisma(charisma);
		setPerception(perception);
		setVisionRadius(perception / 2 + 1);
		setPlayerHP();
		setToHitBonus();
		setDodgeChance();
		setDamageModifier();
		setIsPlayer(true);
		setMaxCarryWeight(this.strength * 15);
		setInventory(this);
		setEquipment(this);
		initializeStartingGear();
		setName("Hero");
	}
	
	public Weapon weapon(){ return this.weapon; }
	public Boots boots(){ return this.boots; }
	public Greaves greaves(){ return this.greaves; }
	public Cuisses cuisses(){ return this.cuisses; }
	public Chestpiece chestpiece(){ return this.chestpiece; }
	public Helmet helmet(){ return this.helmet; }
	
	public String helmetString(){ return this.helmet == null ? "" : this.helmet().name(); }
	public String chestpieceString(){ return this.chestpiece == null ? "" : this.chestpiece().name(); }
	public String cuissesString(){ return this.cuisses == null ? "" : this.cuisses().name(); }
	public String greavesString(){ return this.greaves == null ? "" : this.greaves().name(); }
	public String bootsString(){ return this.boots == null ? "" : this.boots().name(); }
	public String weaponString(){ return this.weapon == null ? "" : this.weapon().name(); }
	
	
	public int strength(){ return this.strength; }
	public void setStrength(int strength){ this.strength = strength; }
	
	public int constitution(){ return this.constitution; }
	public void setConstitution(int constitution){ this.constitution = constitution; }
	
	public int dexterity(){ return this.dexterity; }
	public void setDexterity(int dexterity){ this.dexterity = dexterity; }
	
	public int intelligence(){ return this.intelligence; }
	public void setIntelligence(int intelligence){ this.intelligence = intelligence; }
	
	public int wisdom(){ return this.wisdom; }
	public void setWisdom(int wisdom){ this.wisdom = wisdom; }
	
	public int charisma(){ return this.charisma; }
	public void setCharisma(int charisma){ this.charisma = charisma; }
	
	public int perception() { return this.perception; }
	public void setPerception(int perception) { this.perception = perception; }
	
	public int damageModifier(){ return this.damageModifier; }
	public void setDamageModifier(){ this.damageModifier = this.strength / 2 - 5; }
	public void updateDamageModifier(int damageBonus){ this.damageModifier += damageBonus; }
	
	public int toHitBonus(){ return this.toHitBonus; }
	public void setToHitBonus(){ this.toHitBonus = this.strength / 2 - 5 + this.dexterity / 2 - 5; }
	public void updateToHitBonus(int update){ this.toHitBonus += update; }
	
	public void setDodgeChance(){ setDodge(this.dexterity / 2 - 5); }
	
	public void setNumOfDice(int numOfDice){ this.numOfDice = numOfDice; }
	public int numOfDice(){ return this.numOfDice; }
	
	public void setPlayerHP(){
		int maxHealth = this.constitution * 2;
		int random = RandomGen.rand(1, 100);
		
		if(random <= 10){ maxHealth -= 5; }
		else if(random > 10 && random <= 20){ maxHealth -= 4; }
		else if(random > 20 && random <= 30){ maxHealth -= 3; }
		else if(random > 30 && random <= 40){ maxHealth -= 2; }
		else if(random > 40 && random <= 50){ maxHealth -= 1; }
		else if(random > 50 && random <= 60){ maxHealth += 1; }
		else if(random > 60 && random <= 70){ maxHealth += 2; }
		else if(random > 70 && random <= 80){ maxHealth += 3; }
		else if(random > 80 && random <= 90){ maxHealth += 4; }
		else if(random > 90){ maxHealth += 5; }
		this.setMaxHP(maxHealth);
	}
	
	public void unequipWeapon(){
		equipment().remove(weapon);
		inventory().add(weapon);
		setNumOfDice(1);
		setAttack(3);
		updateToHitBonus(-weapon.toHit());
		updateDamageModifier(-weapon.damageBonus());
		this.weapon = null;
	}
	
	public void equipWeapon(Weapon weapon){ 
		if(inventory().contains(weapon)){
			inventory().remove(weapon);
		}
		this.weapon = weapon;
		setNumOfDice(weapon.numberOfDiceRolled());
		setAttack(weapon.damageValue());
		updateToHitBonus(weapon.toHit());
		updateDamageModifier(weapon.damageBonus());
		equipment().add(weapon);
	}
	
	public void unequipHelmet(){
		equipment().remove(helmet);
		inventory().add(helmet);
		updateArmor(-helmet.armorValue());
		updateToHitBonus(-helmet.toHit());
		updateDamageModifier(-helmet.damageValue());
		this.helmet = null;
	}
	
	public void equipHelmet(Helmet helmet){ 
		if(inventory().contains(helmet)){
			inventory().remove(helmet);
		}
		this.helmet = helmet;
		updateArmor(helmet.armorValue());
		updateToHitBonus(helmet.toHit());
		updateDamageModifier(helmet.damageValue());
		equipment().add(helmet);
		}
	
	public void unequipChestpiece(){
		equipment().remove(chestpiece);
		inventory().add(chestpiece);
		updateArmor(-chestpiece.armorValue());
		updateToHitBonus(-chestpiece.toHit());
		updateDamageModifier(-chestpiece.damageValue());
		this.chestpiece = null;
	}
	
	public void equipChestpiece(Chestpiece chestpiece){ 
		if(inventory().contains(chestpiece)){
			inventory().remove(chestpiece);
		}
		this.chestpiece = chestpiece; 
		updateArmor(chestpiece.armorValue());
		updateToHitBonus(chestpiece.toHit());
		updateDamageModifier(chestpiece.damageValue());
		equipment().add(chestpiece);
		}
	
	public void unequipCuisses(){
		equipment().remove(cuisses);
		inventory().add(cuisses);
		updateArmor(-cuisses.armorValue());
		updateToHitBonus(-cuisses.toHit());
		updateDamageModifier(-cuisses.damageValue());
		this.cuisses = null;
	}
	
	public void equipCuisses(Cuisses cuisses){ 
		if(inventory().contains(cuisses)){
			inventory().remove(cuisses);
		}
		this.cuisses = cuisses;
		updateArmor(cuisses.armorValue());
		updateToHitBonus(cuisses.toHit());
		updateDamageModifier(cuisses.damageValue());
		equipment().add(cuisses);
		}
	
	public void unequipBoots(){
		equipment().remove(boots);
		inventory().add(boots);
		updateArmor(-boots.armorValue());
		updateToHitBonus(-boots.toHit());
		updateDamageModifier(-boots.damageValue());
		this.boots = null;
	}
	
	public void equipBoots(Boots boots){ 
		if(inventory().contains(boots)){
			inventory().remove(boots);
		}
		this.boots = boots;
		updateArmor(boots.armorValue());
		updateToHitBonus(boots.toHit());
		updateDamageModifier(boots.damageValue());
		equipment().add(boots);
		}
	
	public void unequipGreaves(){
		equipment().remove(greaves);
		inventory().add(greaves);
		updateArmor(-greaves.armorValue());
		updateToHitBonus(-greaves.toHit());
		updateDamageModifier(-greaves.damageValue());
		this.greaves = null;
	}
	
	public void equipGreaves(Greaves greaves){
		if(inventory().contains(greaves)){
			inventory().remove(greaves);
		}
		this.greaves = greaves;
		updateArmor(greaves.armorValue());
		updateToHitBonus(greaves.toHit());
		updateDamageModifier(greaves.damageValue());
		equipment().add(greaves);
		}
	
	public void equipItem(BaseItem itemToEquip){
		if(itemToEquip.itemType().equals("weapon")){
			equipWeapon((Weapon)itemToEquip);
		}
		else if(itemToEquip.itemType().equals("helmet")){
			equipHelmet((Helmet)itemToEquip);
		}
		else if(itemToEquip.itemType().equals("cuisses")){
			equipCuisses((Cuisses)itemToEquip);
		}
		else if(itemToEquip.itemType().equals("greaves")){
			equipGreaves((Greaves)itemToEquip);
		}
		else if(itemToEquip.itemType().equals("boots")){
			equipBoots((Boots)itemToEquip);
		}
		else if(itemToEquip.itemType().equals("chestpiece")){
			equipChestpiece((Chestpiece)itemToEquip);
		}
	}
	
	public void initializeStartingGear(){
		ItemFactory startingItems = new ItemFactory(this.level());
		equipItem(startingItems.newItem("iron shortsword"));
		equipItem(startingItems.newItem("iron cap"));
		equipItem(startingItems.newItem("leather armor"));
		equipItem(startingItems.newItem("iron cuisses"));
		equipItem(startingItems.newItem("leather boots"));
		equipItem(startingItems.newItem("iron greaves"));
		inventory().add(startingItems.newItem("potion of strong healing"));
	}
	
	public void meleeAttack(BaseEntity otherEntity){ commonAttack(otherEntity, attackDamage(), otherEntity.name()); }
	
	private void commonAttack(BaseEntity otherEntity, int attackDamage, String otherName){
		int toHitRoll = RandomGen.rand(1, 100);
		int diceRoll = 0, tempDamage = 0;
		toHitRoll += toHitBonus();
		
		for(int numberOfDice = 0; numberOfDice < weapon().numberOfDiceRolled(); numberOfDice++){
			diceRoll = RandomGen.rand(1, attackDamage);
			tempDamage += diceRoll;
		}
		tempDamage += damageModifier();
		int damageAmount = tempDamage - otherEntity.armor();
		String action;
		
		if(toHitRoll < otherEntity.dodge()){ action = "dodge"; doDeflectAction(action, otherEntity); }
		else if(damageAmount < 1){ action = "deflect"; doDeflectAction(action, otherEntity); }
		else{ action = "attack"; doAttackAction(action, otherEntity, damageAmount); otherEntity.modifyHP(-damageAmount, "killed by a " + name()); }
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
