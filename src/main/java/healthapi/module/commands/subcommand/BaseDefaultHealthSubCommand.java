package healthapi.module.commands.subcommand;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.TextFormat;
import healthapi.PlayerHealth;
import healthapi.module.commands.base.BaseSubCommand;

/**
 * @author 若水
 */
public abstract class BaseDefaultHealthSubCommand extends BaseSubCommand {
    protected static final int HEALTH_SETTING_ADD = 0;
    protected static final int HEALTH_SETTING_SET = 1;
    protected static final int HEALTH_SETTING_REMOVE = 2;
    protected static final int LEVEL_HEALTH_SET = 3;
    protected static final int LEVEL_HEALTH_REMOVE = 4;

    protected BaseDefaultHealthSubCommand( String subCommandName) {
        super(subCommandName);
    }

    protected boolean setLevelDefaultHealth(String[] strings,CommandSender commandSender,int type){
        if(!commandSender.isOp()){
            return true;
        }
        if(strings.length > 3){
            String playerName = strings[1];
            int health = -1;
            try{
                health = Integer.parseInt(strings[3]);
            }catch (Exception ignore){}
            String levelName = strings[2];
            if(health != -1){
                Player player = Server.getInstance().getPlayer(playerName);
                if(player != null){
                    playerName = player.getName();
                }
                PlayerHealth health1 = PlayerHealth.getPlayerHealth(playerName);

                if(type == LEVEL_HEALTH_SET){
                    health1.setLevelHealth(levelName,health);
                }else{
                    health1.removeLevelHealth(levelName,health);
                }
                boolean b = true;
                if(strings.length > 4 && "true".equalsIgnoreCase(strings[4])) {
                    b = false;
                }
                if(b) {
                    if(type == LEVEL_HEALTH_SET){
                        commandSender.sendMessage("§a>>§b玩家 §a" + playerName + "在"+levelName+"§b的虚拟血量上限设置为: §a" + health);
                    }else{
                        commandSender.sendMessage("§a>>§b玩家 §a" + playerName + "在"+levelName+"§b的虚拟血量上限"+(health1.existsLevelHealth(levelName)?"减少: §c"+health+"点":"§c已移除"));
                    }
                }
                return true;
            }else{
                commandSender.sendMessage(TextFormat.RED+"请设置正确的血量参数");

            }
        }
        return false;
    }

    protected boolean setDefaultHealth(String[] strings, CommandSender commandSender, int type, boolean max){
        if(strings.length > 2){
            String playerName = strings[1];
            int health = -1;
            try{
                health = Integer.parseInt(strings[2]);
            }catch (Exception ignore){}
            if(health != -1){
                Player player = Server.getInstance().getPlayer(playerName);
                if(player != null){
                    playerName = player.getName();
                }
                PlayerHealth health1 = PlayerHealth.getPlayerHealth(playerName);

                double maxhealth = health1.getHealth();
                if(max){
                    maxhealth = health1.getMaxHealth();
                }
                if(type == HEALTH_SETTING_ADD){
                    if(max){
                        health1.setMaxHealth((int) (health + maxhealth));
                    }else {
                        health1.setHealth(health + maxhealth);
                    }
                }else if(type == HEALTH_SETTING_SET){
                    if(max){
                        health1.setMaxHealth(health);
                    }else {
                        health1.setHealth(health);
                    }
                }else if(type == HEALTH_SETTING_REMOVE){
                    if(!max) {
                        health1.setHealth(maxhealth - health);
                    }else{
                        if(health1.getDefaultMaxHealth() - health >= 1){
                            health1.setMaxHealth((int) (maxhealth - health));
                        }else{
                            health1.setMaxHealth(1);
                        }
                    }
                }

                boolean b = true;
                if(strings.length > 3 && "true".equalsIgnoreCase(strings[3])) {
                    b = false;
                }
                if(b) {
                    commandSender.sendMessage("§a>>§b玩家 §a" + playerName + "§b的虚拟血量"+(max?"上限":"")+": §e" + String.format("%.1f",maxhealth) + " §d->§a" + (max?health1.getMaxHealth():health1.getHealth()));
                }
                if(!max){
                    if(player != null) {
                        player.setHealth(health1.getPlayerHealth());
                    }
                }
            }else{
                commandSender.sendMessage(TextFormat.RED+"请设置正确的血量参数");
            }
            return true;
        }
        return false;
    }


    @Override
    public String[] getAliases() {
        return new String[]{};
    }


    @Override
    public CommandParameter[] getParameters() {
        return new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET,false),
                new CommandParameter("health", CommandParamType.INT,false)
        };
    }



}
