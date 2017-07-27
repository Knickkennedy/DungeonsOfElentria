package roguelike.mob;

import lombok.Data;
import roguelike.items.*;
import roguelike.world.Factory;
import roguelike.modifiers.Effect;
import roguelike.utility.RandomGen;

import java.util.ArrayList;
import java.util.List;

public @Data class Player extends BaseEntity {

    private int strength, constitution, dexterity, intelligence, wisdom, charisma, perception;
    private int bonusToHit, leftHandDamage, rightHandDamage, bonusToDamage, leftHandNumberOfDice, rightHandNumberOfDice, rangedDamage, bonusRangedDamage, bonusRangedToHit, rangedNumberOfDice;
    private Item leftHand, rightHand, rangedWeapon, boots, greaves, cuisses, chestpiece, helmet;
    private List<Item> rangedAmmunition;
    private Factory itemStore;

    public Player() {
        super();
        itemStore = new Factory();
        setInventory(this);
        setEquipment(this);
        setPlayer(true);
        setExperience(0);
        setExperienceLevel(1);
        rangedAmmunition = new ArrayList<>();
        setName("Hero");
    }

    @Override
    public char glyph(){
        if(isInvisible()) return '_';
        else return this.glyph;
    }

    public void read(Item itemToRead){
        if(itemToRead.getItemType().equalsIgnoreCase("book")){
            learnNewSpell(itemToRead.getSpell());
            getInventory().remove(itemToRead);
        }
        else{
            for(Effect effect : itemToRead.effects()){
                addEffect(effect);
            }
        }
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
                                getEquipment().add(newItem);
                            }
                        }
                    }
                    else{
                        Item newItem = itemStore.newItem(equipmentArray[0].trim());
                        equipItem(newItem);
                        getEquipment().add(newItem);
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
                                getInventory().add(itemStore.newItem(itemStore.dangerOneItems.get(roll)));
                            }
                            else if(inventoryArray[0].trim().equals("dangerTwoItems")){
                                int roll = RandomGen.rand(1, itemStore.dangerTwoItems.size());
                                getInventory().add(itemStore.newItem(itemStore.dangerTwoItems.get(roll)));
                            }
                            else if(inventoryArray[0].trim().equals("dangerThreeItems")){
                                int roll = RandomGen.rand(1, itemStore.dangerThreeItems.size());
                                getInventory().add(itemStore.newItem(itemStore.dangerThreeItems.get(roll)));
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
                                getInventory().add(tempItem);
                            }
                            else {
                                getInventory().add(itemStore.newItem(inventoryArray[0].trim()));
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
                setCurrentMana(getMaxMana());
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
                setHealthRegen(Integer.parseInt(value));
                break;
            }case "Mana Regen": {
                setManaRegen(Integer.parseInt(value));
                break;
            }
        }
    }

    public String helmetString() {
        return this.helmet == null ? "" : this.helmet.getName();
    }

    public String chestpieceString() {
        return this.chestpiece == null ? "" : this.chestpiece.getName();
    }

    public String cuissesString() {
        return this.cuisses == null ? "" : this.cuisses.getName();
    }

    public String greavesString() {
        return this.greaves == null ? "" : this.greaves.getName();
    }

    public String bootsString() {
        return this.boots == null ? "" : this.boots.getName();
    }

    public String leftHandString() {
        return this.leftHand == null ? "" : this.leftHand.getName();
    }

    public String rightHandString() {
        return this.rightHand == null ? "" : this.rightHand.getName();
    }

    public String rangedWeaponString() {
        return this.rangedWeapon == null ? "" : this.rangedWeapon.getName();
    }

    public String rangedAmmunitionString() {
        if (rangedAmmunition.isEmpty()) {
            return "";
        } else {
            return rangedAmmunition.get(0).getName() + " x " + rangedAmmunition.size();
        }
    }

    public void setBaseBonusToDamage() {
        this.bonusToDamage = this.strength / 2 - 5;
        bonusRangedDamage = this.strength / 2 - 5 + this.dexterity / 2 - 5;
    }

    public void updateBonusToDamage(int damageBonus) {
        this.bonusToDamage += damageBonus;
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

        if (random <= 10) maxHealth -= 5;
        else if (random <= 20) maxHealth -= 4;
        else if (random <= 30) maxHealth -= 3;
        else if (random <= 40) maxHealth -= 2;
        else if (random <= 50) maxHealth -= 1;
        else if (random <= 60) maxHealth += 1;
        else if (random <= 70) maxHealth += 2;
        else if (random <= 80) maxHealth += 3;
        else if (random <= 90) maxHealth += 4;
        else maxHealth += 5;
        setMaxHP(maxHealth);
        setCurrentHP(getMaxHP());
    }

    public void unequipLeftHand() {
        getEquipment().remove(leftHand);
        getInventory().add(leftHand);
        leftHandDamage = 3;
        bonusToDamage -= leftHand.damageBonus();
        bonusToHit -= leftHand.getToHitBonus();
        leftHandNumberOfDice = 1;
        updateArmor(-leftHand.armorValue());
        updateDodge(-leftHand.dodgeValue());
        this.leftHand = null;
    }

    private void equipLeftHand(Item weapon) {
        if (getInventory().contains(weapon)) {
            getInventory().remove(weapon);
        }
        this.leftHand = weapon;
        leftHandDamage = leftHand.damageValue();
        bonusToDamage += leftHand.damageBonus();
        bonusToHit += leftHand.getToHitBonus();
        updateArmor(leftHand.armorValue());
        updateDodge(leftHand.dodgeValue());
        leftHandNumberOfDice = leftHand.numberOfDiceRolled();

        getEquipment().add(weapon);
    }

    public void unequipRightHand() {
        getEquipment().remove(rightHand);
        getInventory().add(rightHand);
        rightHandDamage = 3;
        bonusToDamage -= rightHand.damageBonus();
        bonusToHit -= rightHand.getToHitBonus();
        rightHandNumberOfDice = 1;
        updateArmor(-rightHand.armorValue());
        updateDodge(-rightHand.dodgeValue());
        this.rightHand = null;
    }

    private void equipRightHand(Item weapon) {
        if (getInventory().contains(weapon)) {
            getInventory().remove(weapon);
        }
        this.rightHand = weapon;
        rightHandDamage = rightHand.damageValue();
        bonusToDamage += rightHand.damageBonus();
        bonusToHit += rightHand.getToHitBonus();
        updateArmor(rightHand.armorValue());
        updateDodge(rightHand.dodgeValue());
        rightHandNumberOfDice = rightHand.numberOfDiceRolled();

        getEquipment().add(weapon);
    }

    public void unequipRangedWeapon() {
        getEquipment().remove(rangedWeapon);
        getInventory().add(rangedWeapon);
        bonusRangedToHit -= rangedWeapon.getToHitBonus();
        bonusRangedDamage -= rangedWeapon.damageBonus();
        updateArmor(-rangedWeapon.armorValue());
        updateDodge(-rangedWeapon.dodgeValue());
        this.rangedWeapon = null;
        setRange(strength / 5 + 1);
    }

    private void equipRangedWeapon(Item weapon) {
        if (getInventory().contains(weapon)) {
            getInventory().remove(weapon);
        }
        this.rangedWeapon = weapon;
        bonusRangedToHit += rangedWeapon.getToHitBonus();
        bonusRangedDamage += rangedWeapon.damageBonus();
        updateArmor(rangedWeapon.armorValue());
        updateDodge(rangedWeapon.dodgeValue());
        getEquipment().add(weapon);
        setRange(weapon.getRange());
    }

    public void unequipHelmet() {
        getEquipment().remove(helmet);
        getInventory().add(helmet);
        updateArmor(-helmet.armorValue());
        updateToHitBonus(-helmet.getToHitBonus());
        updateBonusToDamage(-helmet.damageBonus());
        updateDodge(-helmet.dodgeValue());
        this.helmet = null;
    }

    public void equipHelmet(Item helmet) {
        if (getInventory().contains(helmet)) {
            getInventory().remove(helmet);
        }
        this.helmet = helmet;
        updateArmor(helmet.armorValue());
        updateToHitBonus(helmet.getToHitBonus());
        updateBonusToDamage(helmet.damageBonus());
        updateDodge(helmet.dodgeValue());
        getEquipment().add(helmet);
    }

    public void unequipChestpiece() {
        getEquipment().remove(chestpiece);
        getInventory().add(chestpiece);
        updateArmor(-chestpiece.armorValue());
        updateToHitBonus(-chestpiece.getToHitBonus());
        updateBonusToDamage(-chestpiece.damageBonus());
        updateDodge(-chestpiece.dodgeValue());
        this.chestpiece = null;
    }

    public void equipChestpiece(Item chestpiece) {
        if (getInventory().contains(chestpiece)) {
            getInventory().remove(chestpiece);
        }
        this.chestpiece = chestpiece;
        updateArmor(chestpiece.armorValue());
        updateToHitBonus(chestpiece.getToHitBonus());
        updateBonusToDamage(chestpiece.damageBonus());
        updateDodge(chestpiece.dodgeValue());
        getEquipment().add(chestpiece);
    }

    public void unequipCuisses() {
        getEquipment().remove(cuisses);
        getInventory().add(cuisses);
        updateArmor(-cuisses.armorValue());
        updateToHitBonus(-cuisses.getToHitBonus());
        updateBonusToDamage(-cuisses.damageBonus());
        updateDodge(-cuisses.dodgeValue());
        this.cuisses = null;
    }

    public void equipCuisses(Item cuisses) {
        if (getInventory().contains(cuisses)) {
            getInventory().remove(cuisses);
        }
        this.cuisses = cuisses;
        updateArmor(cuisses.armorValue());
        updateToHitBonus(cuisses.getToHitBonus());
        updateBonusToDamage(cuisses.damageBonus());
        updateDodge(cuisses.dodgeValue());
        getEquipment().add(cuisses);
    }

    public void unequipBoots() {
        getEquipment().remove(boots);
        getInventory().add(boots);
        updateArmor(-boots.armorValue());
        updateToHitBonus(-boots.getToHitBonus());
        updateBonusToDamage(-boots.damageBonus());
        updateDodge(-boots.dodgeValue());
        this.boots = null;
    }

    public void equipBoots(Item boots) {
        if (getInventory().contains(boots)) {
            getInventory().remove(boots);
        }
        this.boots = boots;
        updateArmor(boots.armorValue());
        updateToHitBonus(boots.getToHitBonus());
        updateBonusToDamage(boots.damageBonus());
        updateDodge(boots.dodgeValue());
        getEquipment().add(boots);
    }

    public void unequipGreaves() {
        getEquipment().remove(greaves);
        getInventory().add(greaves);
        updateArmor(-greaves.armorValue());
        updateToHitBonus(-greaves.getToHitBonus());
        updateBonusToDamage(-greaves.damageBonus());
        updateDodge(-greaves.dodgeValue());
        this.greaves = null;
    }

    public void equipGreaves(Item greaves) {
        if (getInventory().contains(greaves)) {
            getInventory().remove(greaves);
        }
        this.greaves = greaves;
        updateArmor(greaves.armorValue());
        updateToHitBonus(greaves.getToHitBonus());
        updateBonusToDamage(greaves.damageBonus());
        updateDodge(greaves.dodgeValue());
        getEquipment().add(greaves);
    }

    public void equipRangedAmmunition(Item ammunition) {
        List<Item> tempList = new ArrayList<>();
        for (Item item : getInventory().getItems()) {
            if (item.equals(ammunition)) {
                tempList.add(item);
            }
        }
        if (!tempList.isEmpty()) {
            for (Item item : tempList) {
                getEquipment().add(item);
                rangedAmmunition.add(item);
                getInventory().remove(item);
            }
        } else {
            getEquipment().add(ammunition);
            rangedAmmunition.add(ammunition);
        }
        bonusRangedDamage += rangedAmmunition.get(0).damageBonus();
        bonusRangedToHit += rangedAmmunition.get(0).getToHitBonus();
        rangedNumberOfDice = rangedAmmunition.get(0).numberOfDiceRolled();
        rangedDamage = rangedAmmunition.get(0).damageValue();
    }

    public void unequipRangedAmmunition() {
        for (Item arrow : rangedAmmunition) {
            getEquipment().remove(arrow);
            getInventory().add(arrow);
        }
        bonusRangedDamage -= rangedAmmunition.get(0).damageBonus();
        bonusRangedToHit -= rangedAmmunition.get(0).getToHitBonus();
        rangedNumberOfDice = 0;
        rangedDamage = 0;
        rangedAmmunition.clear();
    }

    public void equipItem(Item itemToEquip) {
        if (itemToEquip.getItemType().contains("helmet")) {
            equipHelmet(itemToEquip);
        } else if (itemToEquip.getItemType().contains("chestpiece")) {
            equipChestpiece(itemToEquip);
        } else if (itemToEquip.getItemType().contains("melee weapon") && rightHand == null) {
            equipRightHand(itemToEquip);
        } else if (itemToEquip.getItemType().contains("melee weapon") && rightHand != null) {
            equipLeftHand(itemToEquip);
        } else if (itemToEquip.getItemType().contains("cuisses")) {
            equipCuisses(itemToEquip);
        } else if (itemToEquip.getItemType().contains("greaves")) {
            equipGreaves(itemToEquip);
        } else if (itemToEquip.getItemType().contains("boots")) {
            equipBoots(itemToEquip);
        } else if (itemToEquip.getItemType().contains("ranged weapon")) {
            equipRangedWeapon(itemToEquip);
        } else if (itemToEquip.getItemType().contains("ammunition")) {
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

    public void move(int xx, int yy) {
        if (xx == 0 && yy == 0) {
            return;
        }
        BaseEntity otherEntity = level.checkForMob(x + xx, y + yy);
        if (otherEntity == null) {
            if (level.hasItemAlready(x + xx, y + yy)) {
                if(level.checkItems(x + xx, y + yy).getName().toLowerCase().matches("^[aeiou].*")){
                    doAction("see", level.checkItems(x + xx, y + yy));
                }else doAction("see", level.checkItems(x + xx, y + yy));
            }
            if (!level.isGround(x + xx, y + yy)) {
                doAction(String.format("bump into the %s", level.tile(x + xx, y + yy).details()));
            }
            getAi().onEnter(x + xx, y + yy, level);
        } else {
            if (rightHand != null && leftHand != null) dualWieldAttack(otherEntity, leftHand, rightHand);
            else if (rightHand == null && leftHand != null) meleeAttack(otherEntity, leftHand);
            else if (rightHand != null) meleeAttack(otherEntity, rightHand);
        }
    }

    public void consumeEquippedAmmunition(int x, int y) {
        int roll = RandomGen.rand(1, 100);
        if (roll > 25) {
            getLevel().addAtSpecificLocation(rangedAmmunition.get(0), x, y);
            getEquipment().remove(rangedAmmunition.get(0));
            rangedAmmunition.remove(0);
        } else {
            getEquipment().remove(rangedAmmunition.get(0));
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
        toHitRoll += getBonusToHit();

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
        tempDamage += getBonusToDamage();
        int damageAmount = tempDamage - otherEntity.getArmor();

        if (toHitRoll < 25) {
            doAttackAction("miss", otherEntity);
        } else if (toHitRoll > 25 && toHitRoll < 25 + otherEntity.getDodge()) {
            doAttackAction("dodge", otherEntity);
        } else if (damageAmount < 1) {
            doAttackAction("block", otherEntity);
        } else {
            doAttackAction("hit", otherEntity);
            otherEntity.modifyHP(-damageAmount, "killed by a ");
        }
    }

    public void commonDualWieldAttack(BaseEntity otherEntity, Item leftWeapon, Item rightWeapon) {
        int leftToHit = RandomGen.rand(1, 100);
        int rightToHit = RandomGen.rand(1, 100);
        int diceRoll;
        if (leftWeapon.getWeight() > 5.0) {
            leftToHit -= 5;
        }
        if (rightWeapon.getWeight() > 5.0) {
            rightToHit -= 5;
        }
        leftToHit += getBonusRangedToHit();
        rightToHit += getBonusRangedToHit();

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
        leftDamage -= otherEntity.getArmor();
        rightDamage -= otherEntity.getArmor();

        if (rightToHit < 25) {
            doAttackAction("miss", otherEntity);
        } else if (rightToHit > 25 && rightToHit < 25 + otherEntity.getDodge()) {
            doAttackAction("dodge", otherEntity);
        } else {
            if (rightDamage < 1) {
                doAttackAction("block", otherEntity);
            } else {
                doAttackAction("hit", otherEntity);
                otherEntity.modifyHP(-rightDamage, "killed by a ");
            }
        }
        if(otherEntity.getCurrentHP() < 1){
            return;
        }
        if (leftToHit < 25) {
            doAttackAction("miss", otherEntity);
        } else if (leftToHit > 25 && leftToHit < 25 + otherEntity.getDodge()) {
            doAttackAction("dodge", otherEntity);
        } else {
            if (leftDamage < 1) {
                doAttackAction("block", otherEntity);
            } else {
                doAttackAction("hit", otherEntity);
                otherEntity.modifyHP(-leftDamage, "killed by a ");
            }
        }
    }

    public boolean checkIfAmmunitionAndRangedWeaponMatch() {
        if (rangedWeapon != null && !rangedAmmunition.isEmpty()) {
            return rangedWeapon.getItemType().contains("Bow") && rangedAmmunition.get(0).getItemType().contains("bow");
        } else if (rangedWeapon != null && rangedAmmunition.isEmpty()) {
            notify("You need something to shoot!");
            return false;
        } else if (rangedWeapon == null && !rangedAmmunition.isEmpty()) {
            if (rangedAmmunition.get(0).getItemType().contains("thrown")) {
                return true;
            } else {
                notify("You can't throw %ss!", rangedAmmunition.get(0).getName());
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
        int damageAmount = tempDamage - otherEntity.getArmor();

        consumeEquippedAmmunition(otherEntity.x, otherEntity.y);

        if (toHitRoll < otherEntity.getDodge()) {
            doAttackAction("dodge", otherEntity);
        } else if (damageAmount < 1) {
            doAttackAction("barely scratch", otherEntity);
        } else {
            doAttackAction("hit", otherEntity);
            otherEntity.modifyHP(-damageAmount, "killed by a ");
        }
    }


}
