package nju.eur3ka

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.plugin.jvm.reloadPluginConfig
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.QuoteReply
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.utils.ExternalResource.Companion.uploadAsImage
import java.net.URL

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
    private val scope = CoroutineScope(this.coroutineContext)
    private var timeFlag = System.currentTimeMillis()

    override fun onEnable() {
        // 创建图片存储区
        val imageFolder = dataFolder.resolve("img")
        when {
            imageFolder.exists() -> logger.info("ImgFolder: ${imageFolder.path}")
            else -> {
                logger.info("Can't find img folder")
                imageFolder.mkdirs()
                logger.info("Creat ImgFolder: ${imageFolder.path}")
            }
        }
        // 载入配置文件
        reloadPluginConfig(Config)
        // 若管理员不是白名单成员，则自动添加为白名单成员
        if (!Config.whiteQQList.contains(Config.adminQQ)) {
            Config.whiteQQList.add(Config.adminQQ)
        }
        // 实现获取图片指令
        globalEventChannel().subscribeAlways<GroupMessageEvent> {
            // 只有白名单中的群里的白名单成员可以触发指令
            // 管理员默认是白名单成员
            if (group.id in Config.whiteGroupList && sender.id in Config.whiteQQList) {
                // 检测是否为触发指令
                Config.commands.forEach {
                    if (message.content.startsWith(it)) {
                        val mcb = MessageChainBuilder()
                            .append(QuoteReply(source))
                            .append(At(sender))
                        val newTime = System.currentTimeMillis()
                        val timeInt = newTime - timeFlag
                        // 更新执行指令的时间
                        timeFlag = newTime
                        if (timeInt > Config.cd) {
                            // 启动一个下载图片，转发图片的协程
                            scope.launch {
                                var timeCost = System.currentTimeMillis()
                                // 获取图片
                                val fileName = downloadImg(
                                    URL(Config.imageAPIs.shuffled().first()),
                                    imageFolder.absolutePath,
                                    Config.retryCount
                                )
                                timeCost = System.currentTimeMillis() - timeCost
                                // 检查是否正确获取了图片，并发送对应的消息
                                when (fileName) {
                                    "err" -> {
                                        logger.error("未能正确下载图片，尝试次数${Config.retryCount}，Time: $timeCost ms")
                                        group.sendMessage(mcb.append("图片获取失败 >_<").asMessageChain())
                                    }
                                    else -> {
                                        logger.info("获取到图片 $fileName，Time: $timeCost ms")
                                        val img = imageFolder.resolve(fileName).uploadAsImage(group, "jpg")
                                        group.sendMessage(mcb.append(img).asMessageChain())
                                    }
                                }
                            }
                        } else {
                            group.sendMessage(
                                mcb.append("\n${Config.commands.first()}太频繁了，年轻人要节制哦，请冷静一会儿吧\n")
                                    .append("Left: ${Config.cd - timeInt} ms")
                                    .asMessageChain()
                            )
                        }
                    }
                }
            }
        }
        // 实现管理员私戳的管理功能
        globalEventChannel().subscribeAlways<FriendMessageEvent> {
            scope.launch {
                when (message.content) {
                    R.HELP -> sender.sendMessage(R.HELP_INFO)
                    R.CHECK -> {
                        val imageAPIsInfo =
                            Config.imageAPIs.fold("imageAPIs: \n") { info, api -> "$info\t$api\n" }
                        val whiteGroupListInfo =
                            Config.whiteGroupList.fold("whiteGroupList: \n") { info, g -> "$info\t$g\n" }
                        val whiteQQList =
                            Config.whiteQQList.fold("whiteQQList: \n") { info, q -> "$info\t$q\n" }
                        sender.sendMessage(imageAPIsInfo + whiteGroupListInfo + whiteQQList)
                    }
                    else -> {
                        val cmds = message.content.split(" ")
                        if (cmds.isNotEmpty()) {
                            when (cmds[0]) {
                                R.ADD_GROUP -> when (cmds.size) {
                                    1 -> sender.sendMessage("指令缺少参数")
                                    2 -> try {
                                        val groupId = cmds[1].toLong()
                                        Config.whiteGroupList.add(groupId)
                                        sender.sendMessage("成功向群白名单中添加了群$groupId")
                                    } catch (e: Exception) {
                                        sender.sendMessage("错误：${e.message}")
                                    }
                                    else -> sender.sendMessage("指令存在多余的部分：${cmds.drop(2)}")
                                }
                                R.REMOVE_GROUP -> when (cmds.size) {
                                    1 -> sender.sendMessage("指令缺少参数")
                                    2 -> try {
                                        val groupId = cmds[1].toLong()
                                        when (Config.whiteGroupList.remove(groupId)) {
                                            true -> sender.sendMessage("成功从群白名单中移除了群$groupId")
                                            false -> sender.sendMessage("错误：群白名单中不存在群$groupId")
                                        }
                                    } catch (e: Exception) {
                                        sender.sendMessage("错误：${e.message}")
                                    }
                                    else -> sender.sendMessage("指令存在多余的部分：${cmds.drop(2)}")
                                }
                                R.ADD_MEMBER -> when (cmds.size) {
                                    1 -> sender.sendMessage("指令缺少参数")
                                    2 -> try {
                                        val qq = cmds[1].toLong()
                                        Config.whiteQQList.add(qq)
                                        sender.sendMessage("成功向群员白名单中添加了群员$qq")
                                    } catch (e: Exception) {
                                        sender.sendMessage("错误：${e.message}")
                                    }
                                    else -> sender.sendMessage("指令存在多余的部分：${cmds.drop(2)}")
                                }
                                R.REMOVE_MEMBER -> when (cmds.size) {
                                    1 -> sender.sendMessage("指令缺少参数")
                                    2 -> try {
                                        val qq = cmds[1].toLong()
                                        if (qq == Config.adminQQ) {
                                            sender.sendMessage("错误：不能从群员白名单中移除机器人管理员")
                                        } else {
                                            when (Config.whiteQQList.remove(qq)) {
                                                true -> sender.sendMessage("成功从群员白名单中移除了群员$qq")
                                                false -> sender.sendMessage("错误：群员白名单中不存在群员$qq")
                                            }
                                        }
                                    } catch (e: Exception) {
                                        sender.sendMessage("错误：${e.message}")
                                    }
                                    else -> sender.sendMessage("指令存在多余的部分：${cmds.drop(2)}")
                                }
                                R.ADD_API -> when (cmds.size) {
                                    1 -> sender.sendMessage("指令缺少参数")
                                    2 -> try {
                                        val api = cmds[1]
                                        Config.imageAPIs.add(api)
                                        sender.sendMessage("成功向图片API库中添加了API:$api")
                                    } catch (e: Exception) {
                                        sender.sendMessage("错误：${e.message}")
                                    }
                                    else -> sender.sendMessage("指令存在多余的部分：${cmds.drop(2)}")
                                }
                                R.REMOVE_API -> when (cmds.size) {
                                    1 -> sender.sendMessage("指令缺少参数")
                                    2 -> try {
                                        if (Config.imageAPIs.size == 1) {
                                            sender.sendMessage("错误：不能移除最后的图片API链接")
                                        } else {
                                            val api = cmds[1]
                                            when (Config.imageAPIs.remove(api)) {
                                                true -> sender.sendMessage("成功从图片API库中移除了API:$api")
                                                false -> sender.sendMessage("错误：图片API库中不存在API:$api")
                                            }
                                        }
                                    } catch (e: Exception) {
                                        sender.sendMessage("错误：${e.message}")
                                    }
                                    else -> sender.sendMessage("指令存在多余的部分：${cmds.drop(2)}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}