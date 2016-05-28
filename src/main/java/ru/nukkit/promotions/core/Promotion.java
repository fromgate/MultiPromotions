package ru.nukkit.promotions.core;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.utils.ConfigSection;
import ru.nukkit.multipass.Multipass;
import ru.nukkit.promotions.MultiPromotions;
import ru.nukkit.promotions.util.Message;

public abstract class Promotion {

    protected ConfigSection cfg;

    public Promotion(){
        cfg = this.getConfigSection();
        onConstruct();
    }

    public abstract void onConstruct();

    /**
     * This method will called periodically
     */
    public abstract void processOnTime(Player player);

    /**
     * This method will called on P
     * @param eventType
     */
    public void processEvent(PlayerEvent eventType) {
    }



    /**
     * Add player into provided group
     * @param player    Player object
     * @param group     Group id
     */
    public void promote (String player, String group){
        if (Multipass.isInGroup(player,group)) return;
        Multipass.addGroup(player, group);
        Message.LOG_PROMOTE.log(player,group,this.getClass().getSimpleName());
    }

    /**
     * Remove player from defined group
     *
     * @param player    Player object
     * @param group     Group id
     */
    public void demote (String player, String group){
        if (!Multipass.isInGroup(player,group)) return;
        Multipass.removeGroup(player, group);
        if (MultiPromotions.getCfg().logPromotions)
        Message.LOG_DEMOTE.log(player,group,this.getClass().getSimpleName());
    }

    /**
     * Check is player in group
     *
     * @param player    Player object
     * @param group     Group id
     * @return          true - if player is member of defined group
     */
    public boolean isInGroup (String player, String group){
        return Multipass.isInGroup(player, group);
    }


    protected void registerListener (Class<? extends PlayerEvent> eventType){
        Promotions.registerEvent(eventType, this);
    }

    public ConfigSection getConfigSection(){
        return MultiPromotions.getCfg().promoters.getSection(this.getClass().getSimpleName().toLowerCase());
    }


    public void log(String... msg){
        StringBuilder sb = new StringBuilder("[").append(this.getClass().getSimpleName()).append("]");
        for (String m : msg) sb.append(" ").append(m);
        MultiPromotions.getPlugin().getLogger().info(sb.toString());
    }

    public void informPromote (Player player, String groupName){
        Message.INFORM_PROMOTE.print(player, groupName);
    }

    public void informDemote(Player player, String groupName){
        Message.INFORM_DEMOTE.print(player, groupName);
        Message.LOG_DEMOTE.log(player.getName(),groupName,this.getClass().getSimpleName());
    }
}
