package ru.nukkit.promotions.modules.dbpromotion;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Igor on 28.05.2016.
 */
public class DbTable {
    //Игрок, группа, время "выдачи", время окончания группы

    public DbTable(){
    }

    @DatabaseField (generatedId = true)
    int id;

    @DatabaseField (canBeNull = false)
    String group;

    @DatabaseField (canBeNull = false)
    String player;

    @DatabaseField (canBeNull = false)
    long timeStart;

    @DatabaseField
    long timeEnd;

    /*
    @DatabaseField (defaultValue = "0")
    int state; // 0 - добавлено, 1 - выдано, 2 - закрыто
    */

    public int getId() {
        return id;
    }

    public DbTable (String group, String player, long timeStart, long timeEnd){
        this.group = group;
        this.player = player;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
      //  this.state = 0;
    }

    public DbTable (String group, String player, long timeEnd){
        this (group, player, System.currentTimeMillis(), timeEnd);
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }



}
