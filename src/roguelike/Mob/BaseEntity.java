package roguelike.Mob;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import roguelike.AI.BaseAI;
import roguelike.Items.BaseItem;
import roguelike.Items.Inventory;
import roguelike.Level.Level;
import roguelike.levelBuilding.Tile;
import roguelike.modifiers.*;
import roguelike.utility.RandomGen;

public class BaseEntity implements EntityInterface{
	public Level level;
	private char glyph;
	private Color color;
	private String name, causeOfDeath;
	private BaseAI ai;
	private boolean isPlayer;
	public int x, y;
	private int maxHP, currentHP, currentMana, maxMana, healthRegen, manaRegen, healthRegenCooldown, manaRegenCooldown, attackDamage, range, rangedDamage, armor, dodge, visionRadius;
	private double maxCarryWeight;
	private Inventory inventory;
	private Inventory equipment;
	private List <Effect> effects;
	private List <String> offensiveEffects;
	
	public BaseEntity(Level level, char glyph, Color color){
		healthRegenCooldown = 1000;
		manaRegenCooldown = 1000;
		this.level = level;
		this.glyph = glyph;
		this.color = color;
		this.isPlayer = false;
		this.effects = new ArrayList <> ();
		offensiveEffects = new ArrayList<>();
	}

	public BaseAI getAi(){ return this.ai; }

	public Tile realTile(int x, int y){ return this.level().tile(x, y); }
	
	public List <Effect> effects(){ return this.effects; }

	public List <String> getOffensiveEffects(){ return this.offensiveEffects; }
	public void setOffensiveEffects(List <String> effects){ this.offensiveEffects = effects; }
	public void addOffensiveEffect(String offensiveEffect){ this.offensiveEffects.add(offensiveEffect); }

	public void setMaxCarryWeight(int carryWeight){ this.maxCarryWeight = carryWeight; }
	public double maxCarryWeight(){ return this.maxCarryWeight; }
	public double currentCarryWeight(){ 
		if(this.equipment() != null && this.inventory() != null){ return this.equipment().CurrentWeight() + this.inventory().CurrentWeight(); }
		else{ return this.inventory().CurrentWeight(); }
		}
	
	public Inventory inventory(){ return this.inventory; }
	public void setInventory(BaseEntity Entity){ this.inventory = new Inventory(Entity); }
	
	public Inventory equipment(){ return this.equipment; }
	public void setEquipment(BaseEntity Entity){ this.equipment = new Inventory(Entity); }
	
	public Level level(){ return this.level; }
	public void setLevel(Level level){ this.level = level; }

	public int getRange(){ return this.range; }
	public void setRange(int amount){ this.range = amount; }

	public int getRangedDamage(){ return this.rangedDamage; }
	public void setRangedDamage(int amount){ this.rangedDamage = amount; }

	public String name(){ return this.name; }
	public void setName(String name){ this.name = name; }
	
	public String causeOfDeath(){ return this.causeOfDeath; }
	
	public boolean isPlayer(){ return this.isPlayer; }
	public void setIsPlayer(boolean isPlayer){ this.isPlayer = isPlayer; }
	
	public Color color(){ return this.color; }
	public char glyph(){ return this.glyph; }
	
	public int maxHP(){ return this.maxHP; }
	public void setMaxHP(int amount){ this.maxHP = amount; this.currentHP = amount; }
	public void setHealthRegenRate(int regenRate){ this.healthRegen = regenRate; }

	public int getMaxMana(){ return this.maxMana; }
	public void setMaxMana(int amount){ this.maxMana = amount; this.currentMana = amount; }
	public void setManaRegenRate(int regenRate){ this.manaRegen = regenRate; }

	public int getCurrentMana(){ return this.currentMana; }
	public void setCurrentMana(int amount){
		if(this.currentMana + amount > this.maxMana){
			this.currentMana = this.maxMana;
		}
		else{
			this.currentMana += amount;
		}
	}

	public int currentHP(){ return this.currentHP; }
	public void setCurrentHP(int amount){ 
		if(this.currentHP + amount > this.maxHP){
			this.currentHP = this.maxHP;
		}
		else{
			this.currentHP += amount;
		}
		}
	
	public int attackDamage(){ return this.attackDamage; }
	public void setAttack(int damage){ this.attackDamage = damage; }
	
	public int armor(){ return this.armor; }
	public void setArmor(int armor){ this.armor = armor; }
	public void updateArmor(int armor){ this.armor += armor; }
	
	public int dodge(){ return this.dodge; }
	public void setDodge(int dodge){ this.dodge = dodge; }
	
	public int visionRadius(){ return this.visionRadius; }
	public void setVisionRadius(int visionRadius){ this.visionRadius = visionRadius; }
	
	public void setMobAi(BaseAI ai){ this.ai = ai; }
	
	public void notify(String message, Object...params){
		ai.onNotify(String.format(message, params));
	}
	
	public void pickupItem(){
		BaseItem itemToPickUp = this.level().checkItems(this.x, this.y);
		if(itemToPickUp != null){
			this.notify("You pick up the %s.", itemToPickUp.name());
			inventory().add(itemToPickUp);
			this.level().removeItem(itemToPickUp);
		}
		else{
			this.notify("There is nothing to pick up here.");
		}
	}
	
	public void dropItem(BaseItem itemToDrop){
		this.level().addAtSpecificLocation(itemToDrop, this.x, this.y);
		this.inventory().remove(itemToDrop);
		this.notify("You drop %s", itemToDrop.name());
	}
	
	public boolean canSee(int x, int y) { return this.ai.canSee(x, y); }
	public boolean canEnter(int x, int y){ return this.level.tile(x, y).canEnter(); }
	
	public void move(int x, int y){
		if(x == 0 && y == 0){ return; }
		if(this.level.isWall(this.x + x, this.y + y)){ notify("You bump into the %s.", level.tile(this.x + x, this.y + y).details()); }
		BaseEntity otherEntity = this.level.checkForMob(this.x + x, this.y + y);
		if(otherEntity == null){
			if(this.level.hasItemAlready(this.x + x, this.y + y)){
				notify("You see %s here.", this.level.checkItems(this.x + x, this.y + y).name());
			}
			ai.onEnter(this.x + x, this.y + y, this.level);
		}
		else if(!isPlayer() && !otherEntity.isPlayer()){
			return;
		}
		else{
			meleeAttack(otherEntity);
		}
	}
	
	public void meleeAttack(BaseEntity otherEntity){
		commonAttack(otherEntity, this.attackDamage(), otherEntity.name());
	}
	
	public void rangedAttack(BaseEntity otherEntity) {
		commonAttack(otherEntity, this.getRangedDamage(), otherEntity.name());
	}
	
	private void commonAttack(BaseEntity otherEntity, int attackDamage, Object...params){
		int toHitRoll = RandomGen.rand(1, 100);
		int diceRoll = RandomGen.rand(1, attackDamage);
		int damageAmount = diceRoll - otherEntity.armor();
		String action;
		
		if(toHitRoll < otherEntity.dodge()){
			action = "dodge";
			doDeflectAction(action, otherEntity);
		}
		else if(damageAmount < 1){
			action = "deflect";
			doDeflectAction(action, otherEntity);
		}
		else{
			action = "attack";
			doAttackAction(action, otherEntity, damageAmount);
			otherEntity.modifyHP(-damageAmount, "killed by a " + this.name());
			specialAttack(otherEntity);
		}
	}

	public boolean isNextTo(BaseEntity otherEntity){
		int xCheck = Math.abs(this.x - otherEntity.x);
		int yCheck = Math.abs(this.y - otherEntity.y);

		if(xCheck > 1 || yCheck > 1){ return false; }
		else{ return true; }
	}

	public boolean isWithinRange(BaseEntity otherEntity){
		int xCheck = Math.abs(this.x - otherEntity.x);
		int yCheck = Math.abs(this.y - otherEntity.y);

		if(xCheck > getRange() || yCheck > getRange()){ return false; }
		else{ return true; }
	}

	public void specialAttack(BaseEntity otherEntity){
		int poisonRoll = RandomGen.rand(1, 100);
		for(String effect : offensiveEffects){
			if (effect.equals("weak poison") && poisonRoll >= 75) {
				int totalLength = 0;
				for (int i = 0; i < 4; i++) {
					int roll = RandomGen.rand(1, 3);
					totalLength += roll;
				}
				Poison newPoison = new Poison(1, totalLength);
				newPoison.start(otherEntity);
				otherEntity.effects().add(newPoison);
			} else if (effect.equals("strong poison") && poisonRoll >= 60) {
				int totalLength = 0;
				for (int i = 0; i < 4; i++) {
					int roll = RandomGen.rand(1, 3);
					totalLength += roll;
				}
				Poison newPoison = new Poison(2, totalLength);
				newPoison.start(otherEntity);
				otherEntity.effects().add(newPoison);
			}
		}
	}
	
	public void doAttackAction(String action, BaseEntity otherEntity, int damage){
		if(isPlayer()){	this.notify("You %s the %s for %d damage.", action, otherEntity.name(), damage); }
		else{ otherEntity.notify("The %s %ss you for %d damage.", this.name(), action, damage); }
	}
	
	public void doDeflectAction(String action, BaseEntity otherEntity){
		if(isPlayer()){ this.notify("The %s %ss your attack.", otherEntity.name(), action); }
		else{ otherEntity.notify("You %s the %s's attack.", action, this.name()); }
	}
	
	public void modifyHP(int amount, String causeOfDeath){
		setCurrentHP(amount);
		this.causeOfDeath = causeOfDeath;
		if(this.currentHP() < 1){
			doAction("die");
			this.level.remove(this);
		}
	}

	public void modifyMana(int amount){
		setCurrentMana(amount);
	}
	
	public void doAction(String message, Object...params){
		for(BaseEntity otherEntity : this.level.mobs){
			if(otherEntity == this){ otherEntity.notify("You " + message, params); }
			else{ otherEntity.notify(String.format("The %s %s.", name(), makeSecondPerson(message)), params); }
		}
	}
	
	private String makeSecondPerson(String message){
		String[] words = message.split(" ");
		words[0] = words[0] + "s";
		
		StringBuilder builder = new StringBuilder();
		for (String word : words){
			builder.append(" ");
			builder.append(word);
		}
		
		return builder.toString().trim();
	}
	
	private void updateEffects(){
		List <Effect> done = new ArrayList <Effect> ();
		
		for(Effect effect : effects){
		    System.out.println(effect.duration());
			effect.update(this);
			if(effect.isDone()){
				effect.end(this);
				done.add(effect);
			}
		}

		effects.removeAll(done);
	}
	
	public void drink(BaseItem item){
		doAction("drink a " + item.name());
		consume(item);
	}
	
	public void consume(BaseItem item){
		addEffect(item.effect());
		inventory().remove(item);
	}
	
	public void addEffect(Effect effect){
		if(effect == null){ return; }
		effect.start(this);
		effects.add(effect);
	}

	public void regenerateHealth(){
		healthRegenCooldown -= healthRegen;
		if(healthRegenCooldown < 1){
			modifyHP(1, "");
			healthRegenCooldown += 1000;
		}
	}

	public void regenerateMana(){
		manaRegenCooldown -= manaRegen;
		if(manaRegenCooldown < 1){
			modifyMana(1);
			manaRegenCooldown += 1000;
		}
	}

	public void update(){
		regenerateHealth();
		regenerateMana();
		ai.onUpdate();
		updateEffects();
		}
	
	
	
	
	
	
	
	
	
}
