package roguelike.Items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import roguelike.Mob.BaseEntity;

public class Inventory {
	private List <BaseItem> inventory;
	private double currentWeight;
	private BaseEntity owner;
	
	public Inventory(BaseEntity owner){
		inventory = new ArrayList <BaseItem> ();
		this.owner = owner;
	}
	
	public double CurrentWeight(){
		return currentWeight;
	}
	
	public List<BaseItem> getInventory(){ return this.inventory; }
	
	public void add(BaseItem item){
		if(owner.currentCarryWeight() + item.weight() <= owner.maxCarryWeight()){
			inventory.add(item);
			Collections.sort(inventory);
		}
		else{
			owner.notify("You are carrying too much to pick up the %s.", item.details());
		}
		currentWeight += item.weight();
	}
	
	public void remove(BaseItem item){
		if(inventory.contains(item)){
			inventory.remove(item);
		}
		else{
			owner.notify("Remove what?");
		}
		currentWeight -= item.weight();
	}
	
	public boolean contains(BaseItem item){
		for(BaseItem otherItem : inventory){
			if(item.equals(otherItem)){
				return true;
			}
		}
		return false;
	}
}
