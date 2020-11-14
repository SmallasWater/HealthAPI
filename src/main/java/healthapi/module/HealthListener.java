package healthapi.module;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityRegainHealthEvent;
import cn.nukkit.event.player.PlayerDeathEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import healthapi.HealTask;
import healthapi.HealthMainClass;
import healthapi.PlayerHealth;

/**
 * @author SmallasWater
 */
public class HealthListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player =  event.getPlayer();
        if(!HealthMainClass.MAIN_CLASS.worlds.contains(player.getLevel().getFolderName())){
            PlayerHealth health = PlayerHealth.getPlayerHealth(player.getName());
            if(health.isDeath()){
                health.setDeath(false);
            }
            player.setHealth(health.getPlayerHealth());
            if(player.getMaxHealth() != HealthMainClass.MAIN_CLASS.getDefaultHealth()) {
                player.setMaxHealth(HealthMainClass.MAIN_CLASS.getDefaultHealth());
            }
        }
        if(HealthMainClass.MAIN_CLASS.getConfig().getBoolean("生命恢复.是否开启",true)) {
            Server.getInstance().getScheduler().scheduleRepeatingTask(new HealTask(player, HealthMainClass.MAIN_CLASS)
                    , HealthMainClass.MAIN_CLASS.getConfig().getInt("生命恢复.间隔(刻)", 60));

        }

    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onDamage(EntityDamageEvent event){
        if(!event.isCancelled()){
            Entity entity = event.getEntity();
            if(entity instanceof Player){
                if(!HealthMainClass.MAIN_CLASS.worlds.contains(entity.getLevel().getFolderName())) {
                    PlayerHealth health = PlayerHealth.getPlayerHealth((Player) entity);
                    /*
                    * 获取伤害数值
                    * */
                    float damage = event.getFinalDamage();
                    /*
                    * 伤害小于0强制设置为1
                    * */
                    if(damage < 0){
                        damage = 1;
                    }
                    /*
                    *判断是否"死亡"
                    * 防止死亡后进一步受到伤害
                    * */
                    if(!health.isDeath()) {
                        /*
                        *设置被攻击后的虚拟血量
                        * */
                        health.setDamageHealth(damage);
                        /*
                        * 防止血量出现迷之鬼畜
                        * */
                        entity.setHealth(health.getPlayerHealth());
                        /*
                        * 判断死亡条件
                        * */
                        if( health.isDeath()) {
                            /*
                            * 强行击杀
                            * */
                            event.setCancelled();
                            health.addDeath();
                            return;
                        }
                        /*
                        * 计算伤害对于的百分比
                        * */
                        double remove = (double) damage/ (double) health.getMaxHealth();
                        /*
                        * 调整伤害数值
                        * */
                        double damages =  remove * damage;
                        if(entity.getHealth() == 4 && health.getHealth() > 4){
                            damages = 0;
                        }
                        /*
                        * 当伤害过低
                        * */
                        if(damages <= 1){
                            /*
                            * 如果原生血量为保底血量
                            * */
                            if(damages == 0){
                                /*
                                * 维持原生血量状态
                                * */
                                entity.setHealth(health.getPlayerHealth());
                            }
                            /*
                            * 再次强制修正为1
                            * 用处: 解决触碰仙人掌 飞速扣血bug
                            * */
                            damages = 1;
                        }
                        /*
                        * 重设伤害数值
                        * */
                        event.setDamage((float) damages);
                    }else{
                       /*
                       * 强制击杀
                       * */
                        if(entity.isAlive()){
                            event.setCancelled();
                            entity.setHealth(0);

                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event){
        Player entity = event.getEntity();
        PlayerHealth health = PlayerHealth.getPlayerHealth(entity);
        if(health.isDeath()) {
            health.setSpawnHealth();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(player != null) {
            PlayerHealth health = PlayerHealth.getPlayerHealth(player);
            if(health.isDeath()){
                health.reset();
            }
            health.save();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerRespawnEvent event){
        Player entity = event.getPlayer();
        PlayerHealth health = PlayerHealth.getPlayerHealth(entity);
        if(health.isDeath()) {
            health.reset();
        }
    }



    @EventHandler
    public void onAddHealth(EntityRegainHealthEvent event){
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            if(!HealthMainClass.MAIN_CLASS.worlds.contains(entity.getLevel().getFolderName())) {
                event.setCancelled();
                if(HealthMainClass.MAIN_CLASS.getConfig().getBoolean("是否关闭饱食度回血",false)){
                    if(event.getRegainReason() == EntityRegainHealthEvent.CAUSE_EATING){
                        return;
                    }
                }
                PlayerHealth health = PlayerHealth.getPlayerHealth((Player) entity);
                health.heal(event.getAmount());
            }
        }
    }
}
