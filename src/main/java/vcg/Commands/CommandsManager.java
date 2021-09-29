package vcg.Commands;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.MiraiLogger;

import java.util.ArrayList;

public class CommandsManager {
    private final MiraiLogger logger;
    private static final ArrayList<SimpleCommand> SIMPLE_COMMAND_LIST = new ArrayList<>();
    private static final ArrayList<PureCommand> PURE_COMMAND_LIST = new ArrayList<>();
    public static final ArrayList<Command> COMMAND_LIST = new ArrayList<>();
    private final String COMMAND_PREFIX;

    public CommandsManager(MiraiLogger logger, String command_prefix) {
        this.logger = logger;
        COMMAND_PREFIX = command_prefix;
    }

    public void registerCommand(SimpleCommand sc) {
        SIMPLE_COMMAND_LIST.add(sc);
        COMMAND_LIST.add(sc);
        logger.info("注册简单指令：" + sc.getName());
        logger.info("register simple command：" + sc.getName());
    }
    public void registerCommand(PureCommand pc) {
        PURE_COMMAND_LIST.add(pc);
        COMMAND_LIST.add(pc);
        logger.info("注册纯指令：" + pc.getName());
        logger.info("register pure command：" + pc.getName());
    }

    public void handle(GroupMessageEvent event) {
        MessageChain source = event.getMessage();
        String[] rawText = source.contentToString().split(" ");
        if (rawText.length == 1 && rawText[0].startsWith(COMMAND_PREFIX)){
            String name = rawText[0].substring(2);
            //分派解析后的指令到对应的command处理
            for (PureCommand pc : PURE_COMMAND_LIST) {
                if (name.equals(pc.getName())) {
                    pc.onCommand(event);
                }
            }
        }
        if (rawText.length == 2 && rawText[0].startsWith(COMMAND_PREFIX)) {
            String name = rawText[0].substring(2);
            String text = rawText[1];
            //分派解析后的指令到对应的command处理
            for (SimpleCommand sc : SIMPLE_COMMAND_LIST) {
                if (name.equals(sc.getName())) {
                    sc.setContext(text);
                    sc.onCommand(event);
                }
            }
        }
    }
}
