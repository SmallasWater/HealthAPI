package healthapi;

import cn.nukkit.plugin.PluginBase;
import healthapi.module.PlayerHealthModule;

import java.util.List;

/**
 * @author 若水
 */
public class HealthMainClass extends PluginBase {

    public static HealthMainClass MAIN_CLASS;

    private PlayerHealthModule module;

    private int defaultHealth;

    public List<String> worlds;
    @Override
    public void onEnable() {
        MAIN_CLASS = this;
        this.saveDefaultConfig();
        this.reloadConfig();
        if(getConfig().getBoolean("是否启用虚拟血量")) {
            worlds = getConfig().getStringList("真实血量世界");
            this.getLogger().info("正在启动虚拟血量模块");
            module = new PlayerHealthModule();
            module.moduleRegister();
            defaultHealth = getConfig().getInt("玩家血量锁定",40);
        }else{
            this.getLogger().info("未启动虚拟血量模块");
        }

    }

    public int getDefaultHealth() {
        return defaultHealth;
    }

    @Override
    public void onDisable() {
        if(module != null){
            module.moduleDisable();
        }
    }
}
