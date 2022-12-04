package healthapi.module.commands;


import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Level;
import healthapi.module.commands.base.BaseCommand;
import healthapi.module.commands.subcommand.*;



import java.util.ArrayList;



/**
 * @author 若水
 */
public class HealthCommand extends BaseCommand {


    public HealthCommand(String command, String description) {
        super(command, description);
        this.setUsage("/vh help");
        this.setPermission("healthapi.vh");
        this.setDescription(description);

        this.addSubCommand(new BaseDefaultHealthSubCommand("ah") {

            @Override
            public boolean execute( CommandSender commandSender,  String s,  String[] strings) {
                return setDefaultHealth(strings,commandSender,HEALTH_SETTING_ADD,false);
            }

        });
        this.addSubCommand(new BaseDefaultHealthSubCommand("add") {
            @Override
            public boolean execute(CommandSender commandSender,String s, String[] strings) {
                return setDefaultHealth(strings,commandSender,HEALTH_SETTING_ADD,true);
            }


        });
        this.addSubCommand(new BaseDefaultHealthSubCommand("sh") {
            @Override
            public boolean execute(CommandSender commandSender,String s, String[] strings) {
                return setDefaultHealth(strings,commandSender,HEALTH_SETTING_SET,false);
            }

        });
        this.addSubCommand(new BaseDefaultHealthSubCommand("set") {
            @Override
            public boolean execute( CommandSender commandSender,  String s,  String[] strings) {
                return setDefaultHealth(strings,commandSender,HEALTH_SETTING_SET,true);
            }


        });
        this.addSubCommand(new BaseDefaultHealthSubCommand("rh") {
            @Override
            public boolean execute( CommandSender commandSender,  String s,  String[] strings) {
                return setDefaultHealth(strings,commandSender,HEALTH_SETTING_REMOVE,false);
            }


        });
        this.addSubCommand(new BaseDefaultHealthSubCommand("remove") {
            @Override
            public boolean execute(CommandSender commandSender,String s, String[] strings) {
                return setDefaultHealth(strings,commandSender,HEALTH_SETTING_REMOVE,true);

            }

        });
        this.addSubCommand(new BaseDefaultHealthSubCommand("setlevel") {
            @Override
            public boolean execute(CommandSender commandSender, String s, String[] strings) {
                return setLevelDefaultHealth(strings,commandSender,LEVEL_HEALTH_SET);
            }

            @Override
            public CommandParameter[] getParameters() {
                ArrayList<String> levelName = new ArrayList<>();
                for(Level level:Server.getInstance().getLevels().values()){
                    levelName.add(level.getFolderName());
                }
                return new CommandParameter[]{
                        new CommandParameter("player", CommandParamType.TARGET,false),
                        new CommandParameter("levelName", levelName.toArray(new String[0])),
                        new CommandParameter("health", CommandParamType.INT,false)
                };
            }
        });
        this.addSubCommand(new BaseDefaultHealthSubCommand("removelevel") {
            @Override
            public boolean execute(CommandSender commandSender, String s, String[] strings) {
                return setLevelDefaultHealth(strings,commandSender,LEVEL_HEALTH_REMOVE);
            }
            @Override
            public CommandParameter[] getParameters() {
                ArrayList<String> levelName = new ArrayList<>();
                for(Level level:Server.getInstance().getLevels().values()){
                    levelName.add(level.getFolderName());
                }
                return new CommandParameter[]{
                        new CommandParameter("player", CommandParamType.TARGET,false),
                        new CommandParameter("levelName", levelName.toArray(new String[0])),
                        new CommandParameter("health", CommandParamType.INT,false),
                };
            }

        });
        loadCommandBase();
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("healthapi.vh");
    }

    @Override
    public void sendHelp(CommandSender sender) {
        sender.sendMessage("§e>>§a-------------------§bHelps of HealthAPI§a-------------------§e<<");
        sender.sendMessage("§e/vh §7help §a查看插件帮助");
        sender.sendMessage("§e/vh §7add <Player> <health> <(可选)> §a增加玩家血量上限");
        sender.sendMessage("§e/vh §7remove <Player> <health> <(可选)> §a减少玩家血量上限");
        sender.sendMessage("§e/vh §7set <Player> <health> <(可选)> §a设置玩家血量上限");
        sender.sendMessage("§e/vh §7ah <Player> <health> <(可选)> §a增加玩家血量");
        sender.sendMessage("§e/vh §7rh <Player> <health> <(可选)> §a减少玩家血量");
        sender.sendMessage("§e/vh §7sh <Player> <health> <(可选)> §a设置玩家血量");
        sender.sendMessage("§e/vh §7setlevel <Player> <LevelName> <health> <(可选)> §a设置玩家世界虚拟血量");
        sender.sendMessage("§e/vh §7removelevel <Player> <LevelName> <health> <(可选)> §a减少/移除玩家世界虚拟血量");
        sender.sendMessage("§e>>§a-------------------§bHelps of HealthAPI§a-------------------§e<<");
    }

    @Override
    public boolean execute( CommandSender sender,  String label,  String[] args) {
        return super.execute(sender,label,args);
    }



}
