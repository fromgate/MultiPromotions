package ru.nukkit.promotions;

import cn.nukkit.plugin.PluginBase;
import ru.nukkit.promotions.command.Commander;
import ru.nukkit.promotions.core.Promotions;
import ru.nukkit.promotions.util.Cfg;
import ru.nukkit.promotions.util.Message;

public class MultiPromotions extends PluginBase{

    private static MultiPromotions instance;
    private Cfg cfg;

    public static MultiPromotions getPlugin(){
        return instance;
    }
    public static Cfg getCfg(){
        return instance.cfg;
    }


    @Override
    public void onEnable(){
        instance = this;
        cfg = new Cfg();
        cfg.load();
        cfg.save();
        Message.init(this);
        Commander.init(this);
        Promotions.init();
    }

    @Override
    public void onDisable(){
        Promotions.onDisable();
    }


}
