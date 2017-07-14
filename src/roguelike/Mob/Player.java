package roguelike.Mob;

import roguelike.Items.*;
import roguelike.World.Factory;
import roguelike.Level.Level;
import roguelike.utility.RandomGen;

import java.util.ArrayList;
import java.util.List;

public class Player extends BaseEntity {

    private int strength, constitution, dexterity, intelligence, wisdom, charisma, perception;
    private int bonusToHit, leftHandDamage, rightHandDamage, bonusToDamage, leftHandNumberOfDice, rightHandNumberOfDice, rangedDamage, bonusRangedDamage, bonusRangedToHit, rangedNumberOfDice;
    private Item leftHand, rightHand, rangedWeapon, boots, greaves, cuisses, chestpiece, helmet;
    private List<Item> rangedAmmunition;
    private Factory itemStore;

    public Player(Level level) {
        super(level);
        itemStore = new Factory(this.level);
        setInventory(this);
        setEquipment(this);
        setIsPlayer(true);
        setExperience(0);
        setExperienceLevel(1);
        rangedAmmunition = new ArrayList<>();
        setName("Hero");
    }

    public void setAttribute(String attribute, String value) {
        switch(attribute) {
            case "symbol":{
                setGlyph(value.charAt(0));
                break;
            }
            case "color":{
                setColor(Colors.getColor(value));
                break;
            }
            case "Equipment":{
                String equipmentArray[];
                String equipment[] = value.split(", ");
                int chances = 0;
                double odds = 0.0;
                for(String item : equipment){
                    equipmentArray = item.split(" - ");
                    if(equipmentArray.length > 1) {
                        chances = Integer.parseInt(equipmentArray[1].trim());
                        odds = Double.parseDouble(equipmentArray[2].trim());
                        for (int i = 0; i < chances; i++) {
                            double check = RandomGen.dRand(0.0, 100.0);
                            if (check < odds) {
                                Item newItem = itemStore.newItem(equipmentArray[0].trim());
                                equipItem(newItem);
                                equipment().add(newItem);
                            }
                        }
                    }
                    else{
                        Item newItem = itemStore.newItem(equipmentArray[0].trim());
                        equipItem(newItem);
                        equipment().add(newItem);
                    }
                }
                break;
            }case "Inventory":{
                String inventoryArray[];
                String inventory[] = value.split(", ");
                int chances = 0;
                double odds = 0.0;
                for(String drop : inventory){
                    inventoryArray = drop.split(" - ");
                    chances = Integer.parseInt(inventoryArray[1].trim());
                    odds = Double.parseDouble(inventoryArray[2].trim());
                    for(int i = 0; i < chances; i++){
                        double check = RandomGen.dRand(0.0, 100.0);
                        if(check < odds){
                            if(inventoryArray[0].trim().equals("dangerOneItems")){
                                int roll = RandomGen.rand(1, itemStore.dangerOneItems.size());
                                inventory().add(itemStore.newItem(itemStore.dangerOneItems.get(roll)));
                            }
                            else if(inventoryArray[0].trim().equals("dangerTwoItems")){
                                int roll = RandomGen.rand(1, itemStore.dangerTwoItems.size());
                                inventory().add(itemStore.newItem(itemStore.dangerTwoItems.get(roll)));
                            }
                            else if(inventoryArray[0].trim().equals("dangerThreeItems")){
                                int roll = RandomGen.rand(1, itemStore.dangerThreeItems.size());
                                inventory().add(itemStore.newItem(itemStore.dangerThreeItems.get(roll)));
                            } else if(inventoryArray[0].trim().contains(":")){
                                String[] temp = inventoryArray[0].trim().split(":");
                                String itemType = temp[0].trim();
                                int lowerEnd = Integer.parseInt(temp[1].trim());
                                int upperEnd = Integer.parseInt(temp[2].trim());
                                Item tempItem;
                                do {
                                    int roll = RandomGen.rand(0, itemStore.itemsByCategory.get(itemType).size() - 1);
                                    tempItem = itemStore.newItem(itemStore.itemsByCategory.get(itemType).get(roll));
                                }
                                while(tempItem.getDanger() < lowerEnd && tempItem.getDanger() > upperEnd);
                                inventory().add(tempItem);
                            }
                            else {
                                inventory().add(itemStore.newItem(inventoryArray[0].trim()));
                            }
                        }
                    }
                }
                break;
            }case "Strength":{
                setStrength(Integer.parseInt(value));
                setMaxCarryWeight(this.strength * 15);
                setBaseBonusToDamage();
                setBaseBonusToHit();
                break;
            }case "Constitution":{
                setConstitution(Integer.parseInt(value));
                setPlayerHP();
                break;
            }case "Dexterity": {
                setDexterity(Integer.parseInt(value));
                setBaseBonusToDamage();
                setBaseBonusToHit();
                setDodgeChance();
                break;
            }case "Intelligence": {
                setIntelligence(Integer.parseInt(value));
                setMaxMana((int) (intelligence * 2.5));
                break;
            }case "Wisdom": {
                setWisdom(Integer.parseInt(value));
                break;
            }case "Charisma": {
                setCharisma(Integer.parseInt(value));
                break;
            }case "Perception": {
                setPerception(Integer.parseInt(value));
                setVisionRadius(perception / 2 + 1);
                break;
            }case "HP Regen": {
                setHealthRegenRate(Integer.parseInt(value));
                break;
            }case "Mana Regen": {
                setManaRegenRate(Integer.parseInt(value));
                break;
            }
        }
    }

    public Item getLeftHand() {
        return this.leftHand;
    }

    public Item getRightHand() {
        return this.rightHand;
    }

    public Item getRangedWeapon() {
        return this.rangedWeapon;
    }

    public Item getBoots() {
        return this.boots;
    }

    public Item getGreaves() {
        return this.greaves;
    }

    public Item getCuisses() {
        return this.cuisses;
    }

    public Item getChestpiece() {
        return this.chestpiece;
    }

    public Item getHelmet() {
        return this.helmet;
    }

    public List<Item> getRangedAmmunition() {
        return this.rangedAmmunition;
    }

    public String helmetString() {
        return this.helmet == null ? "" : this.helmet.name();
    }

    public String chestpieceString() {
        return this.chestpiece == null ? "" : this.chestpiece.name();
    }

    public String cuissesString() {
        return this.cuisses == null ? "" : this.cuisses.name();
    }

    public String greavesString() {
        return this.greaves == null ? "" : this.greaves.name();
    }

    public String bootsString() {
        return this.boots == null ? "" : this.boots.name();
    }

    public String leftHandString() {
        return this.leftHand == null ? "" : this.leftHand.name();
    }

    public String rightHandString() {
        return this.rightHand == null ? "" : this.rightHand.name();
    }

    public String rangedWeaponString() {
        return this.rangedWeapon == null ? "" : this.rangedWeapon.name();
    }

    public String rangedAmmunitionString() {
        if (rangedAmmunition.isEmpty()) {
            return "";
        } else {
            return rangedAmmunition.get(0).name() + " x " + rangedAmmunition.size();
        }
    }

    public int strength() {
        return this.strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int constitution() {
        return this.constitution;
    }

    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    public int dexterity() {
        return this.dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int intelligence() {
        return this.intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int wisdom() {
        return this.wisdom;
    }

    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    public int charisma() {
        return this.charisma;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    public int perception() {
        return this.perception;
    }

    public void setPerception(int perception) {
        this.perception = perception;
    }

    public int getLeftHandDamage(){
        return this.leftHandDamage;
    }

    public int getLeftHandNumberOfDice(){
        return this.leftHandNumberOfDice;
    }

    public int getRightHandDamage(){
        return this.rightHandDamage;
    }

    public int getRightHandNumberOfDice(){
        return this.rightHandNumberOfDice;
    }

    public int bonusToDamage() {
        return this.bonusToDamage;
    }

    public void setBaseBonusToDamage() {
        this.bonusToDamage = this.strength / 2 - 5;
        bonusRangedDamage = this.strength / 2 - 5 + this.dexterity / 2 - 5;
    }

    public void updateBonusToDamage(int damageBonus) {
        this.bonusToDamage += damageBonus;
    }

    public int toHitBonus() {
        return this.bonusToHit;
    }

    public void setBaseBonusToHit() {
        this.bonusToHit = this.strength / 2 - 5 + this.dexterity / 2 - 5;
        bonusRangedToHit = this.strength / 2 - 5 + this.dexterity / 2 - 5;
    }

    public void updateToHitBonus(int update) {
        this.bonusToHit += update;
    }

    public void setDodgeChance() {
        setDodge(this.dexterity / 2 - 5);
    }

    public void setPlayerHP() {
        int maxHealth = this.constitution * 2;
        int random = RandomGen.rand(1, 100);

        if (random <= 10) {
            maxHealth -= 5;
        } else if (random > 10 && random <= 20) {
            maxHealth -= 4;
        } else if (random > 20 && random <= 30) {
            maxHealth -= 3;
        } else if (random > 30 && random <= 40) {
            maxHealth -= 2;
        } else if (random > 40 && random <= 50) {
            maxHealth -= 1;
        } else if (random > 50 && random <= 60) {
            maxHealth += 1;
        } else if (random > 60 && random <= 70) {
            maxHealth += 2;
        } else if (random > 70 && random <= 80) {
            maxHealth += 3;
        } else if (random > 80 && random <= 90) {
            maxHealth += 4;
        } else if (random > 90) {
            maxHealth += 5;
        }
        this.setMaxHP(maxHealth);
    }

    public void unequipLeftHand() {
        equipment().remove(leftHand);
        inventory().add(leftHand);
        leftHandDamage = 3;
        bonusToDamage -= leftHand.damageBonus();
        bonusToHit -= leftHand.getToHitBonus();
        leftHandNumberOfDice = 1;
        updateArmor(-leftHand.armorValue());
        updateDodge(-leftHand.dodgeValue());
        this.leftHand = null;
    }

    private void equipLeftHand(Item weapon) {
        if (inventory().contains(weapon)) {
            inventory().remove(weapon);
        }
        this.leftHand = weapon;
        leftHandDamage = leftHand.damageValue();
        bonusToDamage += leftHand.damageBonus();
        bonusToHit += leftHand.getToHitBonus();
        updateArmor(leftHand.armorValue());
        updateDodge(leftHand.dodgeValue());
        leftHandNumberOfDice = leftHand.numberOfDiceRolled();

        equipment().add(weapon);
    }

    public void unequipRightHand() {
        equipment().remove(rightHand);
        inventory().add(rightHand);
        rightHandDamage = 3;
        bonusToDamage -= rightHand.damageBonus();
        bonusToHit -= rightHand.getToHitBonus();
        rightHandNumberOfDice = 1;
        updateArmor(-rightHand.armorValue());
        updateDodge(-rightHand.dodgeValue());
        this.rightHand = null;
    }

    private void equipRightHand(Item weapon) {
        if (inventory().contains(weapon)) {
            inventory().remove(weapon);
        }
        this.rightHand = weapon;
        rightHandDamage = rightHand.damageValue();
        bonusToDamage += rightHand.damageBonus();
        bonusToHit += rightHand.getToHitBonus();
        updateArmor(rightHand.armorValue());
        updateDodge(rightHand.dodgeValue());
        rightHandNumberOfDice = rightHand.numberOfDiceRolled();

        equipment().add(weapon);
    }

    public void unequipRangedWeapon() {
        equipment().remove(rangedWeapon);
        inventory().add(rangedWeapon);
        bonusRangedToHit -= rangedWeapon.getToHitBonus();
        bonusRangedDamage -= rangedWeapon.damageBonus();
        updateArmor(-rangedWeapon.armorValue());
        updateDodge(-rangedWeapon.dodgeValue());
        this.rangedWeapon = null;
        setRange(strength / 5 + 1);
    }

    private void equipRangedWeapon(Item weapon) {
        if (inventory().contains(weapon)) {
            inventory().remove(weapon);
        }
        this.rangedWeapon = weapon;
        bonusRangedToHit += rangedWeapon.getToHitBonus();
        bonusRangedDamage += rangedWeapon.damageBonus();
        updateArmor(rangedWeapon.armorValue());
        updateDodge(rangedWeapon.dodgeValue());
        equipment().add(weapon);
        setRange(weapon.getRange());
    }

    public void unequipHelmet() {
        equipment().remove(helmet);
        inventory().add(helmet);
        updateArmor(-helmet.armorValue());
        updateToHitBonus(-helmet.getToHitBonus());
        updateBonusToDamage(-helmet.damageBonus());
        updateDodge(-helmet.dodgeValue());
        this.helmet = null;
    }

    public void equipHelmet(Item helmet) {
        if (inventory().contains(helmet)) {
            inventory().remove(helmet);
        }
        this.helmet = helmet;
        updateArmor(helmet.armorValue());
        updateToHitBonus(helmet.getToHitBonus());
        updateBonusToDamage(helmet.damageBonus());
        updateDodge(helmet.dodgeValue());
        equipment().add(helmet);
    }

    public void unequipChestpiece() {
        equipment().remove(chestpiece);
        inventory().add(chestpiece);
        updateArmor(-chestpiece.armorValue());
        updateToHitBonus(-chestpiece.getToHitBonus());
        updateBonusToDamage(-chestpiece.damageBonus());
        updateDodge(-chestpiece.dodgeValue());
        this.chestpiece = null;
    }

    public void equipChestpiece(Item chestpiece) {
        if (inventory().contains(chestpiece)) {
            inventory().remove(chestpiece);
        }
        this.chestpiece = chestpiece;
        updateArmor(chestpiece.armorValue());
        updateToHitBonus(chestpiece.getToHitBonus());
        updateBonusToDamage(chestpiece.damageBonus());
        updateDodge(chestpiece.dodgeValue());
        equipment().add(chestpiece);
    }

    public void unequipCuisses() {
        equipment().remove(cuisses);
        inventory().add(cuisses);
        updateArmor(-cuisses.armorValue());
        updateToHitBonus(-cuisses.getToHitBonus());
        updateBonusToDamage(-cuisses.damageBonus());
        updateDodge(-cuisses.dodgeValue());
        this.cuisses = null;
    }

    public void equipCuisses(Item cuisses) {
        if (inventory().contains(cuisses)) {
            inventory().remove(cuisses);
        }
        this.cuisses = cuisses;
        updateArmor(cuisses.armorValue());
        updateToHitBonus(cuisses.getToHitBonus());
        updateBonusToDamage(cuisses.damageBonus());
        updateDodge(cuisses.dodgeValue());
        equipment().add(cuisses);
    }

    public void unequipBoots() {
        equipment().remove(boots);
        inventory().add(boots);
        updateArmor(-boots.armorValue());
        updateToHitBonus(-boots.getToHitBonus());
        updateBonusToDamage(-boots.damageBonus());
        updateDodge(-boots.dodgeValue());
        this.boots = null;
    }

    public void equipBoots(Item boots) {
        if (inventory().contains(boots)) {
            inventory().remove(boots);
        }
        this.boots = boots;
        updateArmor(boots.armorValue());
        updateToHitBonus(boots.getToHitBonus());
        updateBonusToDamage(boots.damageBonus());
        updateDodge(boots.dodgeValue());
        equipment().add(boots);
    }

    public void unequipGreaves() {
        equipment().remove(greaves);
        inventory().add(greaves);
        updateArmor(-greaves.armorValue());
        updateToHitBonus(-greaves.getToHitBonus());
        updateBonusToDamage(-greaves.damageBonus());
        updateDodge(-greaves.dodgeValue());
        this.greaves = null;
    }

    public void equipGreaves(Item greaves) {
        if (inventory().contains(greaves)) {
            inventory().remove(greaves);
        }
        this.greaves = greaves;
        updateArmor(greaves.armorValue());
        updateToHitBonus(greaves.getToHitBonus());
        updateBonusToDamage(greaves.damageBonus());
        updateDodge(greaves.dodgeValue());
        equipment().add(greaves);
    }

    public void equipRangedAmmunition(Item ammunition) {
        List<Item> tempList = new ArrayList<>();
        for (Item item : inventory().getInventory()) {
            if (item.name().equalsIgnoreCase(ammunition.name())) {
                tempList.add(item);
            }
        }
        if (!tempList.isEmpty()) {
            for (Item item : tempList) {
                equipment().add(item);
                rangedAmmunition.add(item);
                inventory().remove(item);
            }
        } else {
            equipment().add(ammunition);
            rangedAmmunition.add(ammunition);
        }
        bonusRangedDamage += rangedAmmunition.get(0).damageBonus();
        bonusRangedToHit += rangedAmmunition.get(0).getToHitBonus();
        rangedNumberOfDice = rangedAmmunition.get(0).numberOfDiceRolled();
        rangedDamage = rangedAmmunition.get(0).damageValue();
    }

    public void unequipRangedAmmunition() {
        for (Item arrow : rangedAmmunition) {
            equipment().remove(arrow);
            inventory().add(arrow);
        }
        bonusRangedDamage -= rangedAmmunition.get(0).damageBonus();
        bonusRangedToHit -= rangedAmmunition.get(0).getToHitBonus();
        rangedNumberOfDice = 0;
        rangedDamage = 0;
        rangedAmmunition.clear();
    }

    public void equipItem(Item itemToEquip) {
        if (itemToEquip.itemType().contains("helmet")) {
            equipHelmet(itemToEquip);
        } else if (itemToEquip.itemType().contains("chestpiece")) {
            equipChestpiece(itemToEquip);
        } else if (itemToEquip.itemType().contains("melee weapon") && rightHand == null) {
            equipRightHand(itemToEquip);
        } else if (itemToEquip.itemType().contains("melee weapon") && rightHand != null) {
            equipLeftHand(itemToEquip);
        } else if (itemToEquip.itemType().contains("cuisses")) {
            equipCuisses(itemToEquip);
        } else if (itemToEquip.itemType().contains("greaves")) {
            equipGreaves(itemToEquip);
        } else if (itemToEquip.itemType().contains("boots")) {
            equipBoots(itemToEquip);
        } else if (itemToEquip.itemType().contains("ranged weapon")) {
            equipRangedWeapon(itemToEquip);
        } else if (itemToEquip.itemType().contains("ammunition")) {
            equipRangedAmmunition(itemToEquip);
        }
    }

    public void equipItem(Item itemToEquip, char input) {
        if (input == 'A') {
            equipHelmet(itemToEquip);
        } else if (input == 'B') {
            equipChestpiece(itemToEquip);
        } else if (input == 'C') {
            equipRightHand(itemToEquip);
        } else if (input == 'D') {
            equipLeftHand(itemToEquip);
        } else if (input == 'E') {
            equipCuisses(itemToEquip);
        } else if (input == 'F') {
            equipGreaves(itemToEquip);
        } else if (input == 'G') {
            equipBoots(itemToEquip);
        } else if (input == 'H') {
            equipRangedWeapon(itemToEquip);
        } else if (input == 'I') {
            equipRangedAmmunition(itemToEquip);
        }
    }

    public void move(int x, int y) {
        if (x == 0 && y == 0) {
            return;
        }
        if (this.level.isWall(this.x + x, this.y + y)) {
            notify("You bump into the %s.", level.tile(this.x + x, this.y + y).details());
        }
        BaseEntity otherEntity = this.level.checkForMob(this.x + x, this.y + y);
        if (otherEntity == null) {
            if (this.level.hasItemAlready(this.x + x, this.y + y)) {
                notify("You see a %s here.", this.level.checkItems(this.x + x, this.y + y).name());
            }
            getAi().onEnter(this.x + x, this.y + y, this.level);
        } else {
            if (rightHand != null && leftHand != null) {
                dualWieldAttack(otherEntity, leftHand, rightHand);
            } else if (rightHand == null && leftHand != null) {
                meleeAttack(otherEntity, leftHand);
            } else if (rightHand != null) {
                meleeAttack(otherEntity, rightHand);
            }
        }
    }

    public void consumeEquippedAmmunition(int x, int y) {
        int roll = RandomGen.rand(1, 100);
        if (roll > 25) {
            level().addAtSpecificLocation(rangedAmmunition.get(0), x, y);
            equipment().remove(rangedAmmunition.get(0));
            rangedAmmunition.remove(0);
        } else {
            equipment().remove(rangedAmmunition.get(0));
            rangedAmmunition.remove(0);
        }
    }

    public void dualWieldAttack(BaseEntity otherEntity, Item leftWeapon, Item rightWeapon) {
        commonDualWieldAttack(otherEntity, leftWeapon, rightWeapon);
    }

    public void meleeAttack(BaseEntity otherEntity, Item weaponUsed) {
        commonAttack(otherEntity, weaponUsed);
    }

    public void rangedAttack(BaseEntity otherEntity) {
        commonRangedAttack(otherEntity);
    }

    private void commonAttack(BaseEntity otherEntity, Item weaponUsed) {
        int toHitRoll = RandomGen.rand(1, 100);
        int diceRoll, tempDamage = 0;
        toHitRoll += toHitBonus();

        if (weaponUsed.equals(leftHand)) {
            for (int numberOfDice = 0; numberOfDice < leftHandNumberOfDice; numberOfDice++) {
                diceRoll = RandomGen.rand(1, leftHandDamage);
                tempDamage += diceRoll;
            }
        } else {
            for (int numberOfDice = 0; numberOfDice < rightHandNumberOfDice; numberOfDice++) {
                diceRoll = RandomGen.rand(1, rightHandDamage);
                tempDamage += diceRoll;
            }
        }
        tempDamage += bonusToDamage();
        int damageAmount = tempDamage - otherEntity.armor();

        if (toHitRoll < 25) {
            doMissAction("miss", otherEntity);
        } else if (toHitRoll > 25 && toHitRoll < 25 + otherEntity.dodge()) {
            doDeflectAction("dodge", otherEntity);
        } else if (damageAmount < 1) {
            doDeflectAction("deflect", otherEntity);
        } else {
            doAttackAction("attack", otherEntity, damageAmount);
        }
    }

    public void commonDualWieldAttack(BaseEntity otherEntity, Item leftWeapon, Item rightWeapon) {
        int leftToHit = RandomGen.rand(1, 100);
        int rightToHit = RandomGen.rand(1, 100);
        int diceRoll;
        if (leftWeapon.weight() > 5.0) {
            leftToHit -= 5;
        }
        if (rightWeapon.weight() > 5.0) {
            rightToHit -= 5;
        }
        leftToHit += toHitBonus();
        rightToHit += toHitBonus();

        int leftDamage = 0;
        int rightDamage = 0;

        for (int i = 0; i < leftHandNumberOfDice; i++) {
            diceRoll = RandomGen.rand(1, leftHandDamage);
            leftDamage += diceRoll;
        }
        for (int i = 0; i < rightHandNumberOfDice; i++) {
            diceRoll = RandomGen.rand(1, rightHandDamage);
            rightDamage += diceRoll;
        }
        leftDamage -= otherEntity.armor();
        rightDamage -= otherEntity.armor();

        if (rightToHit < 25) {
            notify("You miss the %s.", otherEntity.name());
        } else if (rightToHit > 25 && rightToHit < 25 + otherEntity.dodge()) {
            doDeflectAction("dodge", otherEntity);
        } else {
            if (rightDamage < 1) {
                doDeflectAction("deflect", otherEntity);
            } else {
                doAttackAction("attack", otherEntity, rightDamage);
            }
        }
        if(otherEntity.currentHP() < 1){
            return;
        }
        if (leftToHit < 25) {
            notify("You miss the %s.", otherEntity.name());
        } else if (leftToHit > 25 && leftToHit < 25 + otherEntity.dodge()) {
            doDeflectAction("dodge", otherEntity);
        } else {
            if (leftDamage < 1) {
                doDeflectAction("deflect", otherEntity);
            } else {
                doAttackAction("attack", otherEntity, leftDamage);
            }
        }
    }

    public boolean checkIfAmmunitionAndRangedWeaponMatch() {
        if (rangedWeapon != null && !rangedAmmunition.isEmpty()) {
            return rangedWeapon.itemType().contains("Bow") && rangedAmmunition.get(0).itemType().contains("bow");
        } else if (rangedWeapon != null && rangedAmmunition.isEmpty()) {
            notify("You need something to shoot!");
            return false;
        } else if (rangedWeapon == null && !rangedAmmunition.isEmpty()) {
            if (rangedAmmunition.get(0).itemType().contains("thrown")) {
                return true;
            } else {
                notify("You can't throw %ss!", rangedAmmunition.get(0).name());
                return false;
            }
        } else {
            return false;
        }
    }

    private void commonRangedAttack(BaseEntity otherEntity) {
        int toHitRoll = RandomGen.rand(1, 100);
        int diceRoll, tempDamage = 0;
        toHitRoll += bonusRangedToHit;
        for (int numberOfDice = 0; numberOfDice < rangedNumberOfDice; numberOfDice++) {
            diceRoll = RandomGen.rand(1, rangedDamage);
            tempDamage += diceRoll;
        }
        tempDamage += bonusRangedDamage;
        int damageAmount = tempDamage - otherEntity.armor();
        String action;

        consumeEquippedAmmunition(otherEntity.x, otherEntity.y);

        if (toHitRoll < otherEntity.dodge()) {
            action = "dodge";
            doDeflectAction(action, otherEntity);
        } else if (damageAmount < 1) {
            action = "deflect";
            doDeflectAction(action, otherEntity);
        } else {
            action = "attack";
            doAttackAction(action, otherEntity, damageAmount);
            otherEntity.modifyHP(-damageAmount, "killed by a " + name());
        }
    }


}
