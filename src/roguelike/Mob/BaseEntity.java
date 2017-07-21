package roguelike.Mob;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import roguelike.AI.BaseAI;
import roguelike.Items.Item;
import roguelike.Items.Inventory;
import roguelike.Level.Level;
import roguelike.levelBuilding.Tile;
import roguelike.modifiers.*;
import roguelike.utility.Point;
import roguelike.utility.RandomGen;

public class BaseEntity{
    public Level level;
    public char glyph;
    private Color color;
    private String name, causeOfDeath;
    private BaseAI ai;
    private boolean isPlayer;

    private boolean isInvisible;
    public int x, y;
    private Point location;
    private int maxHP, currentHP, currentMana, maxMana, healthRegen, manaRegen, healthRegenCooldown, manaRegenCooldown, attackDamage, range, rangedDamage, armor, dodge, visionRadius;
    private int experienceLevel, experience;
    private double maxCarryWeight;
    private Inventory inventory;
    private Inventory equipment;
    private List<Effect> effects;
    private List<Effect> offensiveEffects;
    private List <Spell> knownSpells;

    public BaseEntity() {
        healthRegenCooldown = 1000;
        manaRegenCooldown = 1000;
        this.isPlayer = false;
        this.effects = new ArrayList<>();
        offensiveEffects = new ArrayList<>();
        knownSpells = new ArrayList<>();
        this.experienceLevel = 1;
    }

    public boolean isInvisible() {
        return isInvisible;
    }

    public void setInvisible(boolean invisible) {
        isInvisible = invisible;
    }

    public void learnNewSpell(Spell spell){
        knownSpells.add(spell);
    }

    public List <Spell> getKnownSpells(){
        return this.knownSpells;
    }

    public void setLocation(Point here){
        this.location = here;
    }

    public Point getLocation(){ return this.location; }

    public void setExperience(int value){ this.experience = value; }
    public int getExperience(){ return this.experience; }

    public void setExperienceLevel(int level){ this.experienceLevel = level; }
    public int getExperienceLevel(){ return this.experienceLevel; }

    public BaseAI getAi() {
        return this.ai;
    }

    public void setGlyph(char glyph) {
        this.glyph = glyph;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Tile realTile(int x, int y) {
        return this.level().tile(x, y);
    }

    public List<Effect> effects() {
        return this.effects;
    }

    public List<Effect> getOffensiveEffects() {
        return this.offensiveEffects;
    }

    public void setOffensiveEffects(List<Effect> effects) {
        this.offensiveEffects = effects;
    }

    public void addOffensiveEffect(Effect offensiveEffect) {
        this.offensiveEffects.add(offensiveEffect);
    }

    public void setMaxCarryWeight(int carryWeight) {
        this.maxCarryWeight = carryWeight;
    }

    public double maxCarryWeight() {
        return this.maxCarryWeight;
    }

    public double currentCarryWeight() {
        if (this.equipment() != null && this.inventory() != null) {
            return this.equipment().CurrentWeight() + this.inventory().CurrentWeight();
        } else {
            return this.inventory().CurrentWeight();
        }
    }

    public Inventory inventory() {
        return this.inventory;
    }

    public void setInventory(BaseEntity Entity) {
        this.inventory = new Inventory(Entity);
    }

    public Inventory equipment() {
        return this.equipment;
    }

    public void setEquipment(BaseEntity Entity) {
        this.equipment = new Inventory(Entity);
    }

    public Level level() {
        return this.level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getRange() {
        return this.range;
    }

    public void setRange(int amount) {
        this.range = amount;
    }

    public int getRangedDamage() {
        return this.rangedDamage;
    }

    public void setRangedDamage(int amount) {
        this.rangedDamage = amount;
    }

    public void updateRangedDamage(int update) {
        this.rangedDamage += update;
    }

    public String name() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String causeOfDeath() {
        return this.causeOfDeath;
    }

    public boolean isPlayer() {
        return this.isPlayer;
    }

    public void setIsPlayer(boolean isPlayer) {
        this.isPlayer = isPlayer;
    }

    public Color color() {
        if(isInvisible){
            return level.baseColor(this.x, this.y);
        }else return this.color;
    }

    public char glyph() {
        if(isInvisible){
            return level.baseGlyph(this.x, this.y);
        }else return this.glyph;
    }

    public int maxHP() {
        return this.maxHP;
    }

    public void setMaxHP(int amount) {
        this.maxHP = amount;
        this.currentHP = amount;
    }

    public void setHealthRegenRate(int regenRate) {
        this.healthRegen = regenRate;
    }

    public int getMaxMana() {
        return this.maxMana;
    }

    public void setMaxMana(int amount) {
        this.maxMana = amount;
        this.currentMana = amount;
    }

    public void setManaRegenRate(int regenRate) {
        this.manaRegen = regenRate;
    }

    public int getCurrentMana() {
        return this.currentMana;
    }

    public void setCurrentMana(int amount) {
        if (this.currentMana + amount > this.maxMana) {
            this.currentMana = this.maxMana;
        } else {
            this.currentMana += amount;
        }
    }

    public int currentHP() {
        return this.currentHP;
    }

    public void setCurrentHP(int amount) {
        if (this.currentHP + amount > this.maxHP) {
            this.currentHP = this.maxHP;
        } else {
            this.currentHP += amount;
        }
    }

    public int attackDamage() {
        return this.attackDamage;
    }

    public void setAttack(int damage) {
        this.attackDamage = damage;
    }

    public int armor() {
        return this.armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public void updateArmor(int armor) {
        this.armor += armor;
    }

    public int dodge() {
        return this.dodge;
    }

    public void setDodge(int dodge) {
        this.dodge = dodge;
    }

    public void updateDodge(int update) {
        this.dodge += update;
    }

    public int visionRadius() {
        return this.visionRadius;
    }

    public void setVisionRadius(int visionRadius) {
        this.visionRadius = visionRadius;
    }

    public void setMobAi(BaseAI ai) {
        this.ai = ai;
    }

    public void notify(String message, Object... params) {
        ai.onNotify(String.format(message, params));
    }

    public void pickupItem() {
        Item itemToPickUp = this.level().checkItems(this.x, this.y);
        if (itemToPickUp != null) {
            if (currentCarryWeight() + itemToPickUp.weight() < maxCarryWeight()) {
                doAction("pick up", itemToPickUp);
                inventory().add(itemToPickUp);
                this.level().removeItem(itemToPickUp);
            } else {
                this.notify("You are carrying too much to pick up the %s.", itemToPickUp.name());
            }
        } else {
            this.notify("There is nothing to pick up here.");
        }
    }

    public void dropItem(Item itemToDrop) {
        this.level().addAtSpecificLocation(itemToDrop, this.x, this.y);
        this.inventory().remove(itemToDrop);
        if(isPlayer) doAction("drop", itemToDrop);
    }

    public boolean canSee(int x, int y) {
        return this.ai.canSee(x, y);
    }

    public boolean canEnter(int x, int y) {
        return this.level.tile(x, y).canEnter();
    }

    public void move(int x, int y) {
        if (x == 0 && y == 0) {
            return;
        }
        BaseEntity otherEntity = this.level.checkForMob(this.x + x, this.y + y);
        if (otherEntity == null) {
            if (this.level.hasItemAlready(this.x + x, this.y + y)) {
                doAction("see", this.level.checkItems(this.x + x, this.y + y));
            }
            ai.onEnter(this.x + x, this.y + y, this.level);
        } else if (!isPlayer() && !otherEntity.isPlayer()) {
            ai.initializePathFinding();
            return;
        } else {
            meleeAttack(otherEntity);
        }
    }

    public void meleeAttack(BaseEntity otherEntity) {
        commonAttack(otherEntity, this.attackDamage());
    }

    public void rangedAttack(BaseEntity otherEntity) {
        commonAttack(otherEntity, this.getRangedDamage());
    }

    private void commonAttack(BaseEntity otherEntity, int attackDamage) {
        int toHitRoll = RandomGen.rand(1, 100);
        int diceRoll = RandomGen.rand(1, attackDamage);
        int damageAmount = diceRoll - otherEntity.armor();

        if (toHitRoll < 25) {
            doAttackAction("miss", otherEntity);
        } else if (toHitRoll > 25 && toHitRoll < 25 + otherEntity.dodge) {
            doAttackAction("dodge", otherEntity);
        } else if (damageAmount < 1) {
            doAttackAction("barely scratch", otherEntity);
        } else {
            doAttackAction("hit", otherEntity);
            otherEntity.modifyHP(-damageAmount, "killed by a ");
            specialAttack(otherEntity);
        }
    }

    public boolean isNextTo(BaseEntity otherEntity) {
        int xCheck = Math.abs(this.x - otherEntity.x);
        int yCheck = Math.abs(this.y - otherEntity.y);

        if (xCheck > 1 || yCheck > 1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isWithinRange(BaseEntity otherEntity) {
        int xCheck = Math.abs(this.x - otherEntity.x);
        int yCheck = Math.abs(this.y - otherEntity.y);

        if (xCheck > getRange() || yCheck > getRange()) {
            return false;
        } else {
            return true;
        }
    }

    public void specialAttack(BaseEntity otherEntity) {
        for (Effect effect : offensiveEffects) {
            double specialAttackRoll = RandomGen.dRand(1.0, 100.0);
            if (specialAttackRoll < effect.getEffectChance()) {
                effect.start(otherEntity);
                otherEntity.effects().add(effect);
            }
        }
    }

    public void gainExperience(BaseEntity otherEntity){
        this.experience += otherEntity.experience;
    }

    public void modifyHP(int amount, String causeOfDeath) {
        setCurrentHP(amount);
        if (currentHP() < 1) {
            death();
        }
        this.causeOfDeath = causeOfDeath;
    }

    public void dropAllItems() {
        List<Item> tempList = new ArrayList<>();
        for (Item item : inventory().getInventory()) {
            tempList.add(item);
        }

        for (Item item : tempList) {
            dropItem(item);
        }

    }

    public void death() {
        if(isPlayer()){
            doAction("die");
            level.remove(this);
        }
        else {
            doAction("die");
            dropAllItems();
            level.remove(this);
        }
    }

    public void modifyMana(int amount) {
        setCurrentMana(amount);
    }

    public void doAction(String message){
        for(BaseEntity entity : level.mobs){
            if(entity == this){
                entity.notify("You %s.", message);
            }
            else{
                entity.notify("The %s %s.", name, makeSecondPerson(message));
            }
        }
    }

    public void doAction(String message, Item itemToInteractWith){
        for(BaseEntity entity : level.mobs) {
            if(entity == this) {
                if(itemToInteractWith.name().endsWith("s")){
                    if (beginsWithVowel(itemToInteractWith.name())) {
                        notify("You %s %s.", message, itemToInteractWith.name());
                    } else {
                        notify("You %s %s.", message, itemToInteractWith.name());
                    }
                }else {
                    if (beginsWithVowel(itemToInteractWith.name())) {
                        notify("You %s an %s.", message, itemToInteractWith.name());
                    } else {
                        notify("You %s a %s.", message, itemToInteractWith.name());
                    }
                }
            }
            else{
                if(itemToInteractWith.name().endsWith("s")){
                    if (beginsWithVowel(itemToInteractWith.name())) {
                        entity.notify("The %s %s %s.", name, makeSecondPerson(message), itemToInteractWith.name());
                    } else {
                        entity.notify("The %s %s %s.", name, makeSecondPerson(message), itemToInteractWith.name());
                    }
                }
                else {
                    if (beginsWithVowel(itemToInteractWith.name())) {
                        entity.notify("The %s %s an %s.", name, makeSecondPerson(message), itemToInteractWith.name());
                    } else {
                        entity.notify("The %s %s a %s.", name, makeSecondPerson(message), itemToInteractWith.name());
                    }
                }
            }
        }
    }

    private boolean beginsWithVowel(String word){
        if(word.toLowerCase().matches("^[aeiou].*")){
            return true;
        }
        else return false;
    }

    public void doAction(String message, Object... params) {
        for (BaseEntity entity : this.level.mobs) {
            if (entity == this) entity.notify("You " + message, params);
            else entity.notify(String.format("The %s %s.", name(), makeSecondPerson(message)), params);
        }
    }

    public void doAttackAction(String action, BaseEntity otherEntity) {
        for(BaseEntity entity : level.mobs){
            if(entity == this){
                entity.notify("You %s the %s.", action, otherEntity.name());
            }
            else{
                entity.notify("The %s %s you.", name, makeSecondPerson(action));
            }
        }
    }

    private String makeSecondPerson(String message) {
        String[] words = message.split(" ");
        if(isAnAdverb(words[0])){
            if (shouldEndWithES(words[1])) {
                words[1] = words[1] + "es";
            } else {
                words[1] = words[1] + "s";
            }
        }
        else {
            if (shouldEndWithES(words[0])) {
                words[0] = words[0] + "es";
            } else {
                words[0] = words[0] + "s";
            }
        }
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append(" ");
            builder.append(word);
        }

        return builder.toString().trim();
    }

    private boolean isAnAdverb(String word){
        if(word.toLowerCase().matches(".*(ly)")){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean shouldEndWithES(String word){
        if(word.toLowerCase().matches(".*(s|sh|x|ch|z)")){
            return true;
        }
        else{
            return false;
        }
    }

    private void updateEffects() {
        List<Effect> done = new ArrayList<Effect>();

        for (Effect effect : effects) {
            if (effect.isDone()) {
                effect.end(this);
                done.add(effect);
            }
            else{
                effect.update(this);
            }
        }

        effects.removeAll(done);
    }

    public BaseEntity entityAt(Point location){
        return level.checkForMob(location.x, location.y);
    }

    public void castSpell(Spell spell, Point direction){
        Point current = new Point(this.x, this.y);
        Point dir = new Point(direction.x, direction.y);
        if(spell.getManaCost() > currentMana){
            notify("Nope!");
        }
        else{
            if(spell.getCastType().equals("LINE")){
                castLineSpell(spell, current, dir);
                modifyMana(-spell.getManaCost());
            }
            else if(spell.getCastType().equals("AOE")){
                castAOESpell(spell, current);
                modifyMana(-spell.getManaCost());
            }
        }
    }

    public void castAOESpell(Spell spell, Point current){
        for(int x = -spell.getRange(); x <= spell.getRange(); x++){
            for(int y = -spell.getRange(); y <= spell.getRange(); y++){
                if(x == 0 && y == 0) {
                    continue;
                }
                if(this.x + x < 0 || this.x + x >= level.width) {
                    continue;
                }
                if(this.y + y < 0 || this.y + y >= level.height) {
                    continue;
                }
                BaseEntity target = level.checkForMob(this.x + x, this.y + y);
                if(target != null) target.addEffect(spell.getEffect());
            }
        }
    }

    public void castLineSpell(Spell spell, Point current, Point dir){
        for (int i = 0; i < spell.getRange(); i++) {
            current.add(dir);
            BaseEntity target = level.checkForMob(current.x, current.y);
            if (target != null) {
                target.addEffect(spell.getEffect());
            }
            if (level.tile(current.x + dir.x, current.y).glyph() == '#') {
                if (spell.isReflective()) {
                    dir.flipHorizontally();
                    i++;
                } else {
                    break;
                }
            }
            if (level.tile(current.x, current.y + dir.y).glyph() == '#') {
                if (spell.isReflective()) {
                    dir.flipVertically();
                    i++;
                } else {
                    break;
                }
            }
        }
    }

    public void drink(Item item) {
        doAction("drink a " + item.name());
        consume(item);
    }

    public void consume(Item item) {
        for (Effect effect : item.effects()) {
            addEffect(effect);
        }
        inventory().remove(item);
    }

    public void addEffect(Effect effect) {
        if (effect == null) {
            return;
        }
        effect.start(this);
        effects.add(effect);
    }

    public void regenerateHealth() {
        healthRegenCooldown -= healthRegen;
        if (healthRegenCooldown < 1) {
            modifyHP(1, "");
            healthRegenCooldown += 1000;
        }
    }

    public void regenerateMana() {
        manaRegenCooldown -= manaRegen;
        if (manaRegenCooldown < 1) {
            modifyMana(1);
            manaRegenCooldown += 1000;
        }
    }

    public void update() {
        regenerateHealth();
        regenerateMana();
        ai.onUpdate();
        updateEffects();
    }


}
