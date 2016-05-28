package ru.nukkit.promotions.core;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.scheduler.TaskHandler;
import ru.nukkit.promotions.MultiPromotions;
import ru.nukkit.promotions.modules.dbpromotion.DbPromotion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Promotions {

    private static List<Promotion> promoters;
    private static TaskHandler taskHandler;

    private static Map<Class<? extends PlayerEvent>, Set<Promotion>> listeners;


    public static void addPromotion(Promotion promotion){
        if (promotion != null) promoters.add(promotion);
    }

    public static void init(){
        promoters = new ArrayList<>();
        listeners = new HashMap<>();
        addPromotion(new DbPromotion());


        taskHandler = Server.getInstance().getScheduler().scheduleRepeatingTask(new Runnable() {
            @Override
            public void run() {
                processAllPlayers();
            }
        }, MultiPromotions.getCfg().getRecheckTimeTick());
    }

    public static void processPlayer (Player player){
        promoters.forEach(p -> p.processOnTime(player));
    }

    public static void processAllPlayers(){
        Server.getInstance().getOnlinePlayers().values().forEach(player-> processPlayer(player));
    }

    public static void onDisable(){
        taskHandler.cancel();
        taskHandler = null;
    }

    public static void processEvent (PlayerEvent event){
        if (!listeners.containsKey(event.getClass())) return;
        listeners.get(event.getClass()).forEach(promotion -> promotion.processEvent(event));
    }

    public static void registerEvent (Class<? extends PlayerEvent> eventType, Promotion promotion){
        Set<Promotion> promSet = (listeners.containsKey(eventType)) ? listeners.get(eventType) : new HashSet<>();
        promSet.add (promotion);
        listeners.put(eventType, promSet);
    }

}
