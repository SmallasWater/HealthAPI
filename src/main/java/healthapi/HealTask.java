package healthapi;

import cn.nukkit.Player;
import cn.nukkit.event.entity.EntityRegainHealthEvent;
import cn.nukkit.scheduler.PluginTask;

/**
 * @author SmallasWater
 * @create 2020/9/16 21:08
 */
public class HealTask extends PluginTask<HealthMainClass> {
    private final Player player;
    public HealTask(Player player,HealthMainClass owner) {
        super(owner);
        this.player = player;
    }

    @Override
    public void onRun(int i) {
        if(player.isOnline()){
            PlayerHealth health = PlayerHealth.getPlayerHealth(player);
            health.heal(health.getHeal());
        }else{
            this.cancel();
        }
    }
}
