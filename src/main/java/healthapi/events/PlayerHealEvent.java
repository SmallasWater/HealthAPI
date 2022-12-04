package healthapi.events;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

/**
 * @author SmallasWater
 * @create 2020/9/16 22:01
 */
public class PlayerHealEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    private double amount;

    public static HandlerList getHandlers() {
        return handlers;
    }

    public PlayerHealEvent(Player player,double heal){
        this.player = player;
        this.amount = heal;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
