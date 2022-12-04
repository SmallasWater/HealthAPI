package healthapi.module;


import cn.nukkit.utils.Config;
import healthapi.HealthMainClass;
import healthapi.PlayerHealth;


import healthapi.module.commands.HealthCommand;


import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author 若水
 */
public class PlayerHealthModule{

    private static PlayerHealthModule module;
    private Config config;

    private static LinkedHashMap<String,PlayerHealth> health = new LinkedHashMap<>();


    public void moduleDisable() {
        for(PlayerHealth health: health.values()){
            health.save();
        }
    }


    public void moduleRegister() {
        module = this;
        HealthMainClass.MAIN_CLASS.getLogger().info("虚拟血量模块已启动..");
        config = new Config(getModuleInfo().getDataFolder()+"/healthConfig.yml",Config.YAML);
//        init();
        this.registerCommand();
        this.registerListener();
    }

//    private void init(){
//        Map<String,Object> map = config.getAll();
//        for(String name: map.keySet()){
//            health.put(name,PlayerHealth.formMap(name,(Map) map.get(name)));
//        }
//    }

    public static PlayerHealth getPlayerHealth(String playerName){
        if(!health.containsKey(playerName)){
            if(getModule().config.exists(playerName)){
                health.put(playerName,PlayerHealth.formMap(playerName,((Map) module.config.get(playerName))));
            }else{
                health.put(playerName,new PlayerHealth(playerName));
            }
        }
        return health.get(playerName);
    }

    private static HealthMainClass getModuleInfo(){
        return HealthMainClass.MAIN_CLASS;
    }


    private void registerCommand(){
        getModuleInfo().getServer().getCommandMap().register("healthCommand",new HealthCommand("vh","虚拟血量命令"));
    }

    private void registerListener(){
        HealthMainClass.MAIN_CLASS.getServer().getPluginManager().registerEvents(new HealthListener(),HealthMainClass.MAIN_CLASS);
    }


    public Config getConfig() {
        return config;
    }

    public static PlayerHealthModule getModule() {
        return module;
    }
}
