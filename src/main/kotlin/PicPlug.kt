package nju.eur3ka

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info

object PicPlug : KotlinPlugin(
    JvmPluginDescription(
        id = "nju.eur3ka.picplug",
        name = "PicPlug",
        version = "1.2.0",
    ) {
        author("Eur3ka")
        info("""发图小助手""")
    }
) {
    override fun onEnable() {
        logger.info { "Plugin loaded" }
    }
}