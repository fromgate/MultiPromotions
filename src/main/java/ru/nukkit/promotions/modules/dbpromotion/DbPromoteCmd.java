package ru.nukkit.promotions.modules.dbpromotion;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import ru.nukkit.promotions.command.Cmd;
import ru.nukkit.promotions.command.CmdDefine;
import ru.nukkit.promotions.util.Message;
import ru.nukkit.promotions.util.TimeUtil;


@CmdDefine(command = "dbpromote", allowConsole = true, subCommands = {"\\S+", "\\S+"}, permission = "dbpromote.test", description = Message.DBPROMOTE_DESC)
public class DbPromoteCmd extends Cmd {
    @Override
    public boolean execute(CommandSender sender, Player player, String[] args) {
        String promotePlayer = args[0];
        String groupStr = args[1];
        long endTime = TimeUtil.parseTime(args[2])+System.currentTimeMillis();
        long startTime = System.currentTimeMillis();
        if (args.length>3) {
            startTime =TimeUtil.parseTime(args[2])+System.currentTimeMillis();
            endTime = startTime + TimeUtil.parseTime(args[3]);
        }
        DbPromotion.get().addPromotion (promotePlayer, groupStr, startTime, endTime);
        return Message.DBPROMOTE_ADDED.print(sender, promotePlayer);
    }
}
