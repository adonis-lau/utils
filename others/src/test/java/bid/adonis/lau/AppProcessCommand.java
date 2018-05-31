package bid.adonis.lau;

import org.junit.jupiter.api.Test;

/**
 * User: zhaofanju
 * Time: 2015/9/14 0010 13:06
 * E-mail: zhaofanju@join-cn.com
 */
public enum AppProcessCommand {
    NORMAL("正常调度"),
    KILL("杀死"),
    DEBUG("调试"),
    ONCE("运行一次"),
    SUSPEND("暂停"),
    RESTART("重跑"),
    CONTINUE("继续运行"),
    NODEONCE("节点运行"),
    WAIT("等待");
    private String text;

    AppProcessCommand(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public static AppProcessCommand fromInteger(int x) {
        AppProcessCommand[] statuses = AppProcessCommand.values();
        for (AppProcessCommand s : statuses) {
            if (s.ordinal() == x) {
                return s;
            }
        }
        return AppProcessCommand.KILL;
    }

    public static boolean isAuthorizedAction(AppProcessCommand command) {
        switch (command) {
            case NORMAL:
            case DEBUG:
            case ONCE:
            case CONTINUE:
            case RESTART:
            case NODEONCE:
            case WAIT:
                return true;
            default:
                return false;
        }
    }

    public static boolean isNormalAction(AppProcessCommand command) {
        switch (command) {
            case NORMAL:
            case ONCE:
            case DEBUG:

                return true;
            default:
                return false;
        }
    }

    public static boolean isSuspendAction(AppProcessCommand command){
        switch (command){
            case SUSPEND:
                return true;
            default:
                return false;
        }
    }

    public static boolean isNodeOnceAction(AppProcessCommand command){
        switch (command){
            case NODEONCE:
                return true;
            default:
                return false;
        }
    }

}
