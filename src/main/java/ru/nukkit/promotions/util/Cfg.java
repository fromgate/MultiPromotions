package ru.nukkit.promotions.util;

import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.SimpleConfig;
import ru.nukkit.promotions.MultiPromotions;

public class Cfg extends SimpleConfig {

    @Path("general.language")
    public String language = "default";

    @Path("general.language-save")
    public boolean saveLanguage = false;

    @Path("general.debug")
    public boolean debugMode = true;

    @Path("promotions.recheck-interval")
    String recheckTime = "10s";

    @Path ("promotions.log-promotions")
    public boolean logPromotions;

    @Path("promotions.modules")
    public ConfigSection promoters = new ConfigSection();


    public Cfg() {
        super(MultiPromotions.getPlugin());
    }

    public int getRecheckTimeTick(){
        return TimeUtil.timeToTicks(TimeUtil.parseTime(recheckTime)).intValue();
    }
}
