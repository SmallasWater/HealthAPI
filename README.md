# HealthAPI
血量核心

API类:
```java
//获取玩家虚拟血量类
PlayerHealth healt = PlayerHealth.getPlayerHealth(玩家名);

//PlayerHealth参数
/**
    * 获取玩家虚拟血量类
    * @param player 玩家类
    * @return PlayerHealth类
    * */
  getPlayerHealth(Player player);

    /**
     * 获取玩家虚拟血量类
     * @param playerName 玩家名称
     * @return PlayerHealth类
     * */
  getPlayerHealth(String playerName);
    
    /**
     * 获取虚拟血量上限
     * @return 血量上限
     * */
    getDefaultMaxHealth();

    /**
     * 获取玩家名
     * @return 玩家名
     * */
    getPlayerName();

    /**
     * 获取玩家虚拟血量
     *
     * @return 玩家血量
     * */
    getHealth() ;

    /**
     * 设置玩家虚拟血量 如果玩家在线 同步玩家血量
     * @param health 虚拟血量值
     * */
    setHealth(double health) ;
    /**
     * 获取玩家最大血量
     * @return 最大虚拟血量
     * */
    getMaxHealth();

    /**
     * 插件api 累计增加虚拟血量上限
     * @param owner 识别名
     * @param health 血量上限
     *
     * */
    addMaxHealth(String owner,int health);

    /**
     * 插件api 设置虚拟血量上限
     * @param owner 识别名
     * @param health 血量上限
     *
     * */
    setMaxHealth(String owner,int health);

    /**
     * 插件api 减少虚拟血量上限
     * @param owner 识别名
     * @param health 血量上限
     * */
    removeMaxHealth(String owner,int health);

    /**
     * 插件api 获取识别名增加的虚拟血量
     * @param owner 识别名
     * @return 增加的虚拟血量上限
     * */
    getOwnerMaxHealth(String owner);


    /**
     * 设置虚拟血量上限
     * @param maxHealth 虚拟血量上限
     * */
     setMaxHealth(int maxHealth) ;
    

    /**
     * 获取计算后玩家的真实血量
     * @return 玩家的真实血量
     * */
    getPlayerHealth();


    /**
     * 获取玩家受到攻击后的真实血量
     * @return 玩家的真实血量
     * */
    getDamageHealth(float damage);

    /**
     * 获取玩家血量百分比
     * @return 百分比血量
     * */
    getHealthPercentage();
    /**
     * 设置玩家重生血量
     * */
    setSpawnHealth();
   /**
     * 设置玩家重生血量
     * */
    setSpawnHealth();
    /**
     * 获取玩家世界独立血量
     * @param levelName 世界名称
     * @return 最大血量
     * */
    getLevelHealth(String levelName)；
    /**
     * 移除玩家世界独立血量
     * @param levelName 世界名称
     * */
    removeLevelHealth(String levelName)；
    /**
     * 设置玩家世界独立血量
     * @param levelName 世界名称
     * @param maxHealth 最大血量
     * */
    setLevelHealth(String levelName,int maxHealth)；
```
