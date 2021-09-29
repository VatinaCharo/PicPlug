package vcg;

import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import vcg.Commands.GetRandImage;
import vcg.Commands.Ping;
import vcg.Commands.CommandsManager;
import vcg.Config.Config;
import vcg.Utils.Resources;

import java.io.File;

public final class Plugin extends JavaPlugin {
    public CommandsManager commandsManager;
    public static final Plugin INSTANCE = new Plugin();

    private Plugin() {
        super(new JvmPluginDescriptionBuilder(Resources.ID, Resources.VERSION)
                .name(Resources.NAME)
                .info(Resources.DESCRIPTION)
                .author(Resources.AUTHOR)
                .build());
    }
    @Override
    public void onEnable() {
        getLogger().info("插件："+Resources.NAME_CN+"正在加载...");
        getLogger().info("Plugin: "+Resources.NAME+" loading...");

        //读取配置文件
        reloadPluginConfig(Config.INSTANCE);
        // 初始化图片储存地址
        File imageStorage = new File(Config.INSTANCE.getImageStorage());
        if (!imageStorage.exists()){
            if (!imageStorage.mkdirs()){
                getLogger().error("创建图片文件夹失败");
                getLogger().error("Failed to create image storage folder");
            }
        }
        // 创建自建指令管理系统
        commandsManager = new CommandsManager(getLogger(), Config.INSTANCE.getCommandPrefix());

        getLogger().info("插件："+Resources.NAME_CN+"注册指令中...");
        getLogger().info("Plugin: "+Resources.NAME+" registering commands...");

        // 注册自建指令
        commandsManager.registerCommand(new GetRandImage("gkd"));
        commandsManager.registerCommand(new Ping("ping"));
        // 注册Console命令
        // TODO: 2021/9/28
        // 注入自建的指令处理系统
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class,event -> {
            for (Long obj: Config.INSTANCE.getGroupList()) {
                if (obj ==event.getGroup().getId()){
                    commandsManager.handle(event);
                }
            }
        });

        getLogger().info("插件："+Resources.NAME_CN+"加载完毕！");
        getLogger().info("Plugin: "+Resources.NAME+" loaded!");
    }
}