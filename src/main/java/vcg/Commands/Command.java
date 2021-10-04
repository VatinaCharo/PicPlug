package vcg.Commands;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import vcg.Config.Config;

public interface Command {
    String getName();

    /**
     * 重载方法以添加额外的帮助信息
     *
     * 默认情况示例：
     * ```text
     * !!help
     * >>>  Help Info Manuel
     *      --------------------
     *      !!ping
     * ```
     *
     * @return help info
     */
    default String info() {
        return Config.INSTANCE.getCommandPrefix()+getName();
    }

    void onCommand(GroupMessageEvent event);
}
