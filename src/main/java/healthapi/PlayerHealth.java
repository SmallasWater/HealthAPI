package healthapi;


import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.Config;
import healthapi.events.PlayerHealEvent;
import healthapi.module.PlayerHealthModule;


import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author 若水
 * 玩家虚拟血量类
 * 注意 使用本插件千万不要将EntityDamageEvent的监听优先级设置为 EventPriority.MONITOR
 */
public class PlayerHealth  {


    private double heal = 0.5;

    private final String playerName;

    private double health;

    private int maxHealth;

    private LinkedHashMap<String,Integer> addHeaths;

    private LinkedHashMap<String,Integer> levelHealth;

    private boolean isDeath = false;

    private LinkedHashMap<String,Boolean> healthAdd = new LinkedHashMap<>();



   /**
    * 构造函数
    * */
   private PlayerHealth(String playerName, double health, int maxHealth, LinkedHashMap<String, Integer> addHeaths, LinkedHashMap<String, Integer> levelhealth){
       this.playerName = playerName;
       this.health = health;
       this.maxHealth = maxHealth;
       this.addHeaths = addHeaths;
       this.levelHealth = levelhealth;
   }


    public void setDeath(boolean death) {
        isDeath = death;
    }


    public PlayerHealth(String playerName){
       this(playerName,HealthMainClass.MAIN_CLASS.getDefaultHealth(),HealthMainClass.MAIN_CLASS.getDefaultHealth(),new LinkedHashMap<>(),new LinkedHashMap<>());
   }

   /**
    * 判断虚拟血量上限是否受血量加成影响
    * */
    public boolean isHealthAdd(String worldName) {
        if(worldName != null) {
            if (healthAdd.containsKey(worldName)) {
                return healthAdd.get(worldName);
            }
            return true;
        }
        return true;
    }
    /**
     * 判断虚拟血量上限是否受血量加成影响
     * */
    public boolean isHealthAdd() {
        return isHealthAdd(null);
    }
    /**
     * 设置虚拟血量上限是否受血量加成影响
     * */
    public void setHealthAdd(String worldName,boolean healthAdd) {
        this.healthAdd.put(worldName,healthAdd);
    }

    public void setHeal(double heal) {
        this.heal = heal;
    }

    public double getHeal() {
        return heal;
    }



    /**
    * 获取玩家虚拟血量类
    * @param player 玩家类
    * @return {@link PlayerHealth}
    * */
   public static PlayerHealth getPlayerHealth(Player player){
       return getPlayerHealth(player.getName());
   }

    /**
     * 获取玩家虚拟血量类
     * @param playerName 玩家名称
     * @return {@link PlayerHealth}
     * */
    public static PlayerHealth getPlayerHealth(String playerName){
        return PlayerHealthModule.getPlayerHealth(playerName);
    }

    /**
     * 获取虚拟血量上限
     * @return 血量上限
     * */
    public int getDefaultMaxHealth(){
       return maxHealth;
    }

    /**
     * 获取玩家名
     * @return 玩家名
     * */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * 获取玩家虚拟血量
     *
     * @return 玩家血量
     * */
    public double getHealth() {
        Player player = Server.getInstance().getPlayer(playerName);
        if(player != null){
            if(HealthMainClass.MAIN_CLASS.worlds.contains(player.getLevel().getFolderName())){
                return player.getHealth();
            }
        }
        return health;
    }

    public boolean isDeath(){
        return isDeath;
    }

    /**
     * 增加血量
     * @param health 血量值
     * */
    public void heal(double health){
        Player player = Server.getInstance().getPlayer(playerName);
        if(!isDeath){
            if(player != null) {
                PlayerHealEvent event = new PlayerHealEvent(player,heal);
                Server.getInstance().getPluginManager().callEvent(event);
                if(event.isCancelled()){
                    return;
                }
                health = event.getAmount();
            }
            if(this.health + health > getMaxHealth()){
                this.health = getMaxHealth();
            }else{
                this.health += health;
            }
        }
        if(player != null){
            player.setHealth(getPlayerHealth());
        }
    }

    /**
     * 设置玩家虚拟血量 如果玩家在线 同步玩家血量
     * @param health 虚拟血量值
     * */
    public void setHealth(double health) {
        Player player = Server.getInstance().getPlayer(playerName);
        this.health = health;
        if(player != null){
            /*
            * 判断死亡条件
            * 设置为 death
            * */
            if(this.health < 1){
                setDeath(true);
                return;
            }
            /*
            防止原生血量过低导致被击杀
            * */
            if(!HealthMainClass.MAIN_CLASS.worlds.contains(player.getLevel().getFolderName())) {
                if (this.health > 1 && getPlayerHealth() < 2) {
                    player.setHealth(4);
                }
            }
        }
    }


    /**
     * 获取玩家世界独立血量
     * @param levelName 世界名称
     * @return 最大血量
     * */
    public int getLevelHealth(String levelName){
        if(levelHealth.containsKey(levelName)){
            return levelHealth.get(levelName);
        }
        return maxHealth;
    }

    /**
     * 移除玩家世界独立血量
     * @param levelName 世界名称
     * */
    public void removeLevelHealth(String levelName){
        levelHealth.remove(levelName);
    }

    /**
     * 移除玩家世界独立血量
     * @param levelName 世界名称
     * @param maxHealth 血量上限
     * */
    public void removeLevelHealth(String levelName,int maxHealth){
        if(levelHealth.containsKey(levelName)){
            int health = levelHealth.get(levelName);
            if(health - maxHealth > 0){
                levelHealth.put(levelName,health - maxHealth);
            }else{
                levelHealth.remove(levelName);
            }
        }
    }

    public boolean existsLevelHealth(String levelName){
        return levelHealth.containsKey(levelName);
    }

    /**
     * 设置玩家世界独立血量
     * @param levelName 世界名称
     * @param maxHealth 最大血量
     * */
    public void setLevelHealth(String levelName,int maxHealth){
        levelHealth.put(levelName,maxHealth);
    }
    /**
     * 获取玩家最大血量
     * @return 最大虚拟血量
     * */
    public int getMaxHealth() {
        Player player = Server.getInstance().getPlayer(playerName);
        String levelName = null;
        int health = this.maxHealth;
        if(player != null){
            levelName = player.getLevel().getFolderName();
            if(HealthMainClass.MAIN_CLASS.worlds.contains(levelName)){
                return player.getMaxHealth();
            }
            if(levelHealth.containsKey(levelName)){
                health = getLevelHealth(levelName);
            }
        }
        if(isHealthAdd(levelName)) {
            for (int value : addHeaths.values()) {
                health += value;
            }
        }
        return health;

    }

    /**
     * 插件api 累计增加虚拟血量上限
     * @param owner 识别名
     * @param health 血量上限
     *
     * */
    public void addMaxHealth(String owner,int health){
       if(!addHeaths.containsKey(owner)){
           addHeaths.put(owner,health);
       }else{
           addHeaths.put(owner,addHeaths.get(owner) + health);
       }

    }

    /**
     * 插件api 设置虚拟血量上限
     * @param owner 识别名
     * @param health 血量上限
     *
     * */
    public void setMaxHealth(String owner,int health) {
        addHeaths.put(owner,health);

    }

    /**
    * 初始化
     * */
    public void reset(){
        Player player = Server.getInstance().getPlayer(playerName);
        if(player != null){
            player.setHealth(getPlayerHealth());
            player.setMaxHealth(HealthMainClass.MAIN_CLASS.getDefaultHealth());
        }
        if(isDeath){
            isDeath = false;
        }
        this.health = getMaxHealth();
    }

    /**
     * 插件api 减少虚拟血量上限
     * @param owner 识别名
     * @param health 血量上限
     * */
    public void removeMaxHealth(String owner,int health) {
        if(addHeaths.containsKey(owner)){
            if(addHeaths.get(owner) > health){
                addHeaths.put(owner,addHeaths.get(owner) - health);
            }else{
                addHeaths.remove(owner);
            }
        }
    }

    /**
     * 插件api 获取识别名增加的虚拟血量
     * @param owner 识别名
     * @return 增加的虚拟血量上限
     * */
    public int getOwnerMaxHealth(String owner){
       if(addHeaths.containsKey(owner)){
           return addHeaths.get(owner);
       }
       return 0;
    }


    /**
     * 设置虚拟血量上限
     * @param maxHealth 虚拟血量上限
     * */
    public void setMaxHealth(int maxHealth) {
        if(maxHealth <= 0){
            this.maxHealth = 1;
        }else{
            this.maxHealth = maxHealth;
        }

    }

    /**
     * 获取计算后玩家的真实血量
     * @return 玩家的真实血量
     * */
    public float getPlayerHealth(){
        float playerHealth = (float) ((float)HealthMainClass.MAIN_CLASS.getDefaultHealth() * (getHealthPercentage() / 100));
        if(health > 1 && playerHealth < 2){
            playerHealth = 4.0F;
        }
        if(health < 1){
            return 0;
        }
        /*
         * 兼容原生回血效果
         * */
        if(HealthMainClass.MAIN_CLASS.getDefaultHealth() - playerHealth < 2){
            if(health < getMaxHealth()){
                playerHealth = HealthMainClass.MAIN_CLASS.getDefaultHealth() - 4;
            }
        }
        return playerHealth;
    }

    public void addDeath(){
        isDeath = true;
        Player player = Server.getInstance().getPlayer(playerName);
        if(player != null){
            player.setHealth(0);
        }
    }


    /**
     * 设置玩家受到攻击后的真实血量
     * */
    public void setDamageHealth(float damage){
       setHealth(health - damage);
    }

    /**
     * 获取玩家血量百分比
     * @return 百分比血量
     * */
    public double getHealthPercentage(){
       return (health / (double) getMaxHealth()) * 100;
    }



    private LinkedHashMap<String,Object> getMap(){
       LinkedHashMap<String,Object> maps = new LinkedHashMap<>();
       maps.put("health", health);
       maps.put("maxHealth", maxHealth);
       maps.put("addHeaths", addHeaths);
       maps.put("levelHealth", levelHealth);
       maps.put("heal",heal);

       return maps;
    }

    public void save(){
        Config config = PlayerHealthModule.getModule().getConfig();
        config.set(playerName,getMap());
        config.save();
    }

    public static PlayerHealth formMap(String name,Map map){
        double health = (double) map.get("health");
        int maxHealth = (int) map.get("maxHealth");
        LinkedHashMap<String,Integer> addHealth = new LinkedHashMap<>();
        Map m = (Map) map.get("addHeaths");
        for(Object s:m.keySet()){
            addHealth.put(s.toString(),Integer.parseInt(m.get(s).toString()));
        }
        LinkedHashMap<String,Integer> levelHealth = new LinkedHashMap<>();
        m = (Map) map.get("levelHealth");
        for(Object s:m.keySet()){
            levelHealth.put(s.toString(),Integer.parseInt(m.get(s).toString()));
        }
        PlayerHealth health1 =  new PlayerHealth(name,health,maxHealth,addHealth,levelHealth);
        if(map.containsKey("heal")) {
            health1.setHeal((double) map.get("heal"));
        }else{
            health1.setHeal(HealthMainClass.MAIN_CLASS.getConfig().getDouble("生命恢复.数值",0.5));
        }
        return health1;

    }
    /**
     * 要留着的
     * */
    @Deprecated
    public void setSpawnHealth(){
        reset();
    }



}
