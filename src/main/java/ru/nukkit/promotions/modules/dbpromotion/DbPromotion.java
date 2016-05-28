package ru.nukkit.promotions.modules.dbpromotion;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import ru.nukkit.dblib.DbLib;
import ru.nukkit.promotions.command.Commander;
import ru.nukkit.promotions.core.Promotion;
import ru.nukkit.promotions.util.Message;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class DbPromotion extends Promotion {

    private static DbPromotion instance;
    public static DbPromotion get(){
        return instance;
    }

    private boolean enabled;

    private ConnectionSource connection;
    private Dao<DbTable,String> dao;




    @Override
    public void onConstruct() {
        instance = this;

        enabled = false;
        if (cfg.getBoolean("custom-MySQL")) {
            StringBuilder url = new StringBuilder("jdbc:mysql://");
            url.append(cfg.getString("MySQL.host","localhost"));
            url.append(":").append(cfg.getString("MySQL.port","3306"));
            url.append("/").append(cfg.get("MySQL.db","db"));
            connection = DbLib.getConnectionSource(url.toString(),cfg.getString("MySQL.username","nukkit"), cfg.getString("MySQL.password","tikkun"));
        } else connection = DbLib.getConnectionSource();

        if (connection == null) return;

        try {
            dao = DaoManager.createDao(connection, DbTable.class);
            TableUtils.createTableIfNotExists(connection,DbTable.class);
        } catch (Exception e) {
            Message.debugException(e);
            return;
        }

        enabled = true;
        this.registerListener(PlayerJoinEvent.class);
        this.registerListener(PlayerQuitEvent.class);
        Commander.addNewCommand(new DbPromoteCmd());
    }

    @Override
    public void processOnTime(Player player) {
        if (player == null||!player.isOnline()) return;
        List<DbTable> promotions = getPromotions(player.getName());
        if (promotions==null||promotions.isEmpty()) return;
        Iterator<DbTable> iterator = promotions.iterator();
        while (iterator.hasNext()){
            DbTable db = iterator.next();
            if (db.timeEnd<=System.currentTimeMillis()) {
                Message.debugMessage("timeEnd / current:",db.timeEnd, System.currentTimeMillis());
                this.demote(db.getPlayer(),db.getGroup());
                informDemote(player, db.getGroup());
                try {
                    dao.delete(db);
                } catch (SQLException e) {
                    Message.debugException("Failed to delete database record", e);
                }
                iterator.remove();
            } else if (db.timeStart>0&&db.timeStart<=System.currentTimeMillis()){
                this.promote(db.getPlayer(),db.getGroup());
                informPromote(player, db.getGroup());
            }
        }
    }

    @Override
    public void processEvent(PlayerEvent eventType) {
        processOnTime(eventType.getPlayer());
    }


    public boolean addPromotion(String promotePlayer, String groupStr, long startTime, long endTime) {
        DbTable table = new DbTable(groupStr, promotePlayer, startTime, endTime);
        try {
            dao.create(table);
        } catch (SQLException e) {
            Message.debugException(e);
            return false;
        }
        return true;
    }

    public List<DbTable> getPromotions (String player){
        List<DbTable> result = null;
        try {
            result = dao.queryBuilder().where().like("player",player).query();
        } catch (SQLException e) {
            Message.debugException(e);
        }
        return result;
    }




}
