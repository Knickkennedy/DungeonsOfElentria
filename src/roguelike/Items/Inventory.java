package roguelike.Items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import roguelike.Mob.BaseEntity;

public class Inventory {
	private List <Item> inventory;
	private double currentWeight;
	private BaseEntity owner;
	
	public Inventory(BaseEntity owner){
		inventory = new ArrayList <> ();
		this.owner = owner;
	}
	
	public double CurrentWeight(){
		return currentWeight;
	}
	
	public List<Item> getInventory(){ return this.inventory; }
	
	public void add(Item item){
		if(owner.currentCarryWeight() + item.weight() <= owner.maxCarryWeight()){
			inventory.add(item);
			Collections.sort(inventory);
            currentWeight += item.weight();
		}
	}

	public void remove(Item item){
		if(inventory.contains(item)){
			inventory.remove(item);
		}
		else{
			owner.notify("Remove what?");
		}
		currentWeight -= item.weight();
	}
	
	public boolean contains(Item item){
		for(Item otherItem : inventory){
			if(item.equals(otherItem)){
				return true;
			}
		}
		return false;
	}
}
