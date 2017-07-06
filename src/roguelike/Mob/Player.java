package roguelike.Mob;

import java.awt.Color;

import roguelike.Items.*;
import roguelike.Level.Level;
import roguelike.utility.RandomGen;

import java.util.ArrayList;
import java.util.List;

public class Player extends BaseEntity{
	
	private int strength, constitution, dexterity, intelligence, wisdom, charisma, perception, toHitBonus, damageModifier, numOfDice;
	private Item leftHand, rightHand, rangedWeapon, boots, greaves, cuisses, chestpiece, helmet;
	private List <Item> rangedAmmunition;

	public Player(Level level, char glyph, Color color){
		super(level, glyph, color);
	}

	public void initializeHero(){
        setToHitBonus();
        setDodgeChance();
        setDamageModifier();
        setIsPlayer(true);
        setInventory(this);
        setEquipment(this);
        rangedAmmunition = new ArrayList<>();
        initializeStartingGear();
        setName("Hero");
    }

	public void setAttribute(String attribute, int value){
		if(attribute.equals("Strength")){
			setStrength(value);
            setMaxCarryWeight(this.strength * 15);
		}
		else if(attribute.equals("Constitution")){
			setConstitution(value);
            setPlayerHP();
		}
		else if(attribute.equals("Dexterity")){
			setDexterity(value);
		}
		else if(attribute.equals("Intelligence")){
			setIntelligence(value);
            setMaxMana((int)(intelligence*2.5));
		}
		else if(attribute.equals("Wisdom")){
			setWisdom(value);
		}
		else if(attribute.equals("Charisma")){
			setCharisma(value);
		}
		else if(attribute.equals("Perception")){
			setPerception(value);
            setVisionRadius(perception / 2 + 1);
		}
		else if(attribute.equals("HP Regen")){
			setHealthRegenRate(value);
		}
		else if(attribute.equals("Mana regen")){
			setManaRegenRate(value);
		}
	}

	public Item getLeftHand(){ return this.leftHand; }
	public Item getRightHand(){ return this.rightHand; }
	public Item getRangedWeapon(){ return this.rangedWeapon; }
	public Item getBoots(){ return this.boots; }
	public Item getGreaves(){ return this.greaves; }
	public Item getCuisses(){ return this.cuisses; }
	public Item getChestpiece(){ return this.chestpiece; }
	public Item getHelmet(){ return this.helmet; }
	public List <Item> getRangedAmmunition(){ return this.rangedAmmunition; }
	
	public String helmetString(){ return this.helmet == null ? "" : this.helmet.name(); }
	public String chestpieceString(){ return this.chestpiece == null ? "" : this.chestpiece.name(); }
	public String cuissesString(){ return this.cuisses == null ? "" : this.cuisses.name(); }
	public String greavesString(){ return this.greaves == null ? "" : this.greaves.name(); }
	public String bootsString(){ return this.boots == null ? "" : this.boots.name(); }
	public String leftHandString(){ return this.leftHand == null ? "" : this.leftHand.name(); }
	public String rightHandString(){ return this.rightHand == null ? "" : this.rightHand.name(); }
	public String rangedWeaponString(){ return this.rangedWeapon == null ? "" : this.rangedWeapon.name(); }
	public String rangedAmmunitionString(){
	    if(rangedAmmunition.isEmpty()){
	        return "";
        }
        else{
	        return rangedAmmunition.get(0).name() + " x " + rangedAmmunition.size();
        }
    }

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

	public void unequipLeftHand(){
        equipment().remove(leftHand);
        inventory().add(leftHand);
        setNumOfDice(1);
        setAttack(3);
        this.leftHand = null;
    }

    public void equipLeftHand(Item weapon){
        if(inventory().contains(weapon)){
            inventory().remove(weapon);
        }
        this.leftHand = weapon;
        equipment().add(weapon);
    }

    public void unequipRightHand(){
        equipment().remove(rightHand);
        inventory().add(rightHand);
        setNumOfDice(1);
        setAttack(3);
        this.rightHand = null;
    }

    public void equipRightHand(Item weapon){
        if(inventory().contains(weapon)){
            inventory().remove(weapon);
        }
        this.rightHand = weapon;
        equipment().add(weapon);
    }

    public void unequipRangedWeapon(){
        equipment().remove(rangedWeapon);
        inventory().add(rangedWeapon);
        this.rangedWeapon = null;
        setRange(strength / 5 + 1);
    }

    public void equipRangedWeapon(Item weapon){
        if(inventory().contains(weapon)){
            inventory().remove(weapon);
        }
        this.rangedWeapon = weapon;
        equipment().add(weapon);
        setRange(weapon.getRange());
    }

	public void unequipHelmet(){
		equipment().remove(helmet);
		inventory().add(helmet);
		updateArmor(-helmet.armorValue());
		updateToHitBonus(-helmet.getToHitBonus());
		updateDamageModifier(-helmet.damageValue());
		this.helmet = null;
	}
	
	public void equipHelmet(Item helmet){
		if(inventory().contains(helmet)){
			inventory().remove(helmet);
		}
		this.helmet = helmet;
		updateArmor(helmet.armorValue());
		updateToHitBonus(helmet.getToHitBonus());
		updateDamageModifier(helmet.damageValue());
		equipment().add(helmet);
		}
	
	public void unequipChestpiece(){
		equipment().remove(chestpiece);
		inventory().add(chestpiece);
		updateArmor(-chestpiece.armorValue());
		updateToHitBonus(-chestpiece.getToHitBonus());
		updateDamageModifier(-chestpiece.damageValue());
		this.chestpiece = null;
	}
	
	public void equipChestpiece(Item chestpiece){
		if(inventory().contains(chestpiece)){
			inventory().remove(chestpiece);
		}
		this.chestpiece = chestpiece;
		updateArmor(chestpiece.armorValue());
		updateToHitBonus(chestpiece.getToHitBonus());
		updateDamageModifier(chestpiece.damageValue());
		equipment().add(chestpiece);
		}
	
	public void unequipCuisses(){
		equipment().remove(cuisses);
		inventory().add(cuisses);
		updateArmor(-cuisses.armorValue());
		updateToHitBonus(-cuisses.getToHitBonus());
		updateDamageModifier(-cuisses.damageValue());
		this.cuisses = null;
	}
	
	public void equipCuisses(Item cuisses){
		if(inventory().contains(cuisses)){
			inventory().remove(cuisses);
		}
		this.cuisses = cuisses;
		updateArmor(cuisses.armorValue());
		updateToHitBonus(cuisses.getToHitBonus());
		updateDamageModifier(cuisses.damageValue());
		equipment().add(cuisses);
		}
	
	public void unequipBoots(){
		equipment().remove(boots);
		inventory().add(boots);
		updateArmor(-boots.armorValue());
		updateToHitBonus(-boots.getToHitBonus());
		updateDamageModifier(-boots.damageValue());
		this.boots = null;
	}
	
	public void equipBoots(Item boots){
		if(inventory().contains(boots)){
			inventory().remove(boots);
		}
		this.boots = boots;
		updateArmor(boots.armorValue());
		updateToHitBonus(boots.getToHitBonus());
		updateDamageModifier(boots.damageValue());
		equipment().add(boots);
		}
	
	public void unequipGreaves(){
		equipment().remove(greaves);
		inventory().add(greaves);
		updateArmor(-greaves.armorValue());
		updateToHitBonus(-greaves.getToHitBonus());
		updateDamageModifier(-greaves.damageValue());
		this.greaves = null;
	}
	
	public void equipGreaves(Item greaves){
		if(inventory().contains(greaves)){
			inventory().remove(greaves);
		}
		this.greaves = greaves;
		updateArmor(greaves.armorValue());
		updateToHitBonus(greaves.getToHitBonus());
		updateDamageModifier(greaves.damageValue());
		equipment().add(greaves);
		}

    public void equipRangedAmmunition(Item ammunition){
	    List <Item> tempList = new ArrayList<>();
	    for(Item item : inventory().getInventory()){
	        if(item.name().equalsIgnoreCase(ammunition.name())){
	            tempList.add(item);
            }
        }
        if(!tempList.isEmpty()){
	        for(Item item : tempList){
	            equipment().add(item);
	            rangedAmmunition.add(item);
	            inventory().remove(item);
            }
        }
        else{
            equipment().add(ammunition);
            rangedAmmunition.add(ammunition);
        }
    }

    public void unequipRangedAmmunition(){
        int index = rangedAmmunition.size() - 1;
        for(Item arrow : rangedAmmunition){
            equipment().remove(arrow);
            inventory().add(arrow);
        }
        rangedAmmunition.clear();
    }
	
	public void equipItem(Item itemToEquip, char input){
		if(input == 'A'){
            equipHelmet(itemToEquip);
		}
		else if(input == 'B'){
            equipChestpiece(itemToEquip);
		}
		else if(input == 'C'){
		    equipRightHand(itemToEquip);
        }
        else if(input == 'D'){
		    equipLeftHand(itemToEquip);
        }
		else if(input == 'E'){
			equipCuisses(itemToEquip);
		}
		else if(input == 'F'){
			equipGreaves(itemToEquip);
		}
		else if(input == 'G'){
			equipBoots(itemToEquip);
		}
		else if(input == 'H'){
			equipRangedWeapon(itemToEquip);
		}
		else if(input == 'I'){
		    equipRangedAmmunition(itemToEquip);
        }
	}
	
	public void initializeStartingGear(){
		ItemFactory startingItems = new ItemFactory(this.level());
		equipItem(startingItems.newItem("iron shortsword"), 'C');
		equipItem(startingItems.newItem("leather helmet"), 'A');
		equipItem(startingItems.newItem("leather armor"), 'B');
		equipItem(startingItems.newItem("leather pants"), 'E');
		equipItem(startingItems.newItem("leather boots"), 'G');
		equipItem(startingItems.newItem("leather greaves"), 'F');
		equipItem(startingItems.newItem("short bow"), 'H');
		for(int i = 0; i < 30; i++){
		    equipItem(startingItems.newItem("iron arrow"), 'I');
		    inventory().add(startingItems.newItem("steel arrow"));
		    inventory().add(startingItems.newItem("rock"));
        }
        inventory().add(startingItems.newItem("potion of weak healing"));
        inventory().add(startingItems.newItem("potion of weak poison"));
	}

    public void move(int x, int y){
        if(x == 0 && y == 0){ return; }
        if(this.level.isWall(this.x + x, this.y + y)){ notify("You bump into the %s.", level.tile(this.x + x, this.y + y).details()); }
        BaseEntity otherEntity = this.level.checkForMob(this.x + x, this.y + y);
        if(otherEntity == null){
            if(this.level.hasItemAlready(this.x + x, this.y + y)){
                notify("You see %s here.", this.level.checkItems(this.x + x, this.y + y).name());
            }
            getAi().onEnter(this.x + x, this.y + y, this.level);
        }
        else if(!isPlayer() && !otherEntity.isPlayer()){
            return;
        }
        else{
            if(rightHand != null) {
                meleeAttack(otherEntity, rightHand);
            }
            else if(leftHand != null){
                meleeAttack(otherEntity, leftHand);
            }
            else{
                meleeAttack(otherEntity);
            }
        }
    }

    public void consumeEquippedAmmunition(int x, int y){
        int roll = RandomGen.rand(1, 100);
        if(roll > 25){
            level().addAtSpecificLocation(rangedAmmunition.get(0), x, y);
            equipment().remove(rangedAmmunition.get(0));
            rangedAmmunition.remove(0);
        }
        else{
            equipment().remove(rangedAmmunition.get(0));
            rangedAmmunition.remove(0);
        }
    }

	public void meleeAttack(BaseEntity otherEntity, Item weaponUsed){ commonAttack(otherEntity, weaponUsed); }
	public void rangedAttack(BaseEntity otherEntity, Item weaponUsed, List <Item> ammunition){ commonRangedAttack(otherEntity, weaponUsed, ammunition); }

	private void commonAttack(BaseEntity otherEntity, Item weaponUsed){
		int toHitRoll = RandomGen.rand(1, 100);
		int diceRoll = 0, tempDamage = 0;
		toHitRoll += toHitBonus() + weaponUsed.getToHitBonus();
		
            for(int numberOfDice = 0; numberOfDice < weaponUsed.numberOfDiceRolled(); numberOfDice++){
                diceRoll = RandomGen.rand(1, weaponUsed.damageValue());
                tempDamage += diceRoll;
            }
		tempDamage += damageModifier() + weaponUsed.damageBonus();
		int damageAmount = tempDamage - otherEntity.armor();
		String action;
		
		if(toHitRoll < otherEntity.dodge()){ action = "dodge"; doDeflectAction(action, otherEntity); }
		else if(damageAmount < 1){ action = "deflect"; doDeflectAction(action, otherEntity); }
		else{ action = "attack"; doAttackAction(action, otherEntity, damageAmount); otherEntity.modifyHP(-damageAmount, "killed by a " + name()); }
	}

	public boolean checkIfAmmunitionAndRangedWeaponMatch(){
	    if(rangedWeapon != null && !rangedAmmunition.isEmpty()){
	        if(rangedWeapon.itemType().contains("bow") && rangedAmmunition.get(0).itemType().contains("bow")){
	            return true;
            }
            else{
	            return false;
            }
        }
        else if(rangedWeapon != null && rangedAmmunition.isEmpty()){
	        notify("You need something to shoot!");
	        return false;
        }
        else if(rangedWeapon == null && !rangedAmmunition.isEmpty()){
            if(rangedAmmunition.get(0).itemType().contains("thrown")){
                return true;
            }
            else{
                notify("You can't throw %ss!", rangedAmmunition.get(0).name());
                return false;
            }
        }
        else{
            return false;
        }
    }

    private void commonRangedAttack(BaseEntity otherEntity, Item weaponUsed, List <Item> ammunition){
        int toHitRoll = RandomGen.rand(1, 100);
        int diceRoll = 0, tempDamage = 0;
        if(weaponUsed != null) {
            toHitRoll += toHitBonus() + weaponUsed.getToHitBonus() + ammunition.get(0).getToHitBonus();
        }
        else{
            toHitRoll += toHitBonus() + ammunition.get(0).getToHitBonus();
        }
        for(int numberOfDice = 0; numberOfDice < ammunition.get(0).numberOfDiceRolled(); numberOfDice++){
            diceRoll = RandomGen.rand(1, ammunition.get(0).damageValue());
            tempDamage += diceRoll;
        }
        if(weaponUsed != null) {
            tempDamage += damageModifier() + weaponUsed.damageBonus() + ammunition.get(0).damageBonus();
        }
        else{
            tempDamage += damageModifier() + ammunition.get(0).damageBonus();
        }
        int damageAmount = tempDamage - otherEntity.armor();
        String action;

        consumeEquippedAmmunition(otherEntity.x, otherEntity.y);

        if(toHitRoll < otherEntity.dodge()){ action = "dodge"; doDeflectAction(action, otherEntity); }
        else if(damageAmount < 1){ action = "deflect"; doDeflectAction(action, otherEntity); }
        else{ action = "attack"; doAttackAction(action, otherEntity, damageAmount); otherEntity.modifyHP(-damageAmount, "killed by a " + name()); }
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
