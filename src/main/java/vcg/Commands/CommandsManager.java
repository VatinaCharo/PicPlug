package vcg.Commands;

import net.mamoe.mirai.event.events.GroupMessageEvent;
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

    /**
     * 受配置文件控制的指令注册方法
     *
     * @param sc     简单指令
     * @param enable 配置文件中对应的指令开关属性
     */
    public void registerCommand(SimpleCommand sc, Boolean enable) {
        if (enable) {
            registerCommand(sc);
        } else {
            logger.info("取消注册简单指令：" + sc.getName());
            logger.info("cancel registering simple command：" + sc.getName());
        }
    }

    public void registerCommand(PureCommand pc) {
        PURE_COMMAND_LIST.add(pc);
        COMMAND_LIST.add(pc);
        logger.info("注册纯指令：" + pc.getName());
        logger.info("register pure command：" + pc.getName());
    }

    /**
     * 受配置文件控制的指令注册方法
     *
     * @param pc     纯指令
     * @param enable 配置文件中对应的指令开关属性
     */
    public void registerCommand(PureCommand pc, Boolean enable) {
        if (enable) {
            registerCommand(pc);
        } else {
            logger.info("取消注册纯指令：" + pc.getName());
            logger.info("cancel registering pure command：" + pc.getName());
        }
    }

    /**
     * 解析群消息，提取合法的指令消息，并分配给对应的指令去处理事件
     *
     * @param event 机器人传入的群消息事件
     */
    public void handle(GroupMessageEvent event) {
        String[] rawText = event.getMessage().contentToString().split(" ");
        if (rawText.length == 1 && rawText[0].startsWith(COMMAND_PREFIX)) {
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
