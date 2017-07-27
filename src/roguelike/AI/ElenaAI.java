package roguelike.AI;

import roguelike.mob.BaseEntity;
import roguelike.mob.Player;

public class ElenaAI extends BaseAI{
    Player player;
    boolean hasSeenPlayer;

    public ElenaAI(BaseEntity mob) {
        super(mob);
    }

    public void onUpdate(){
        this.player = mob.getLevel().player;
        if(canSee(player.x, player.y) && !hasSeenPlayer){
            player.notify("You hear a booming voice echo through your head!");
            player.notify("\"You approach doom, mortal!\"");
            player.notify("Elena surges forward!");
        }
        if(this.canSee(player.x, player.y) || hasSeenPlayer){
            hunt(player);
            hasSeenPlayer = true;
        }
        else{
            skipTurn();
        }
    }
}
