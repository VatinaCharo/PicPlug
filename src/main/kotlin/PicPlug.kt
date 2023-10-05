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
import net.mamoe.mirai.utils.error
import java.net.URL

object PicPlug : KotlinPlugin(
    JvmPluginDescription(
        id = "nju.eur3ka.picplug",
        name = "PicPlug",
        version = "2.1.2",
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
        if (imageFolder.exists()) {
            logger.info("ImgFolder: ${imageFolder.path}")
        } else {
            logger.info("Can't find img folder")
            imageFolder.mkdirs()
            logger.info("Creat ImgFolder: ${imageFolder.path}")
        }
        // 载入配置文件
        reloadPluginConfig(Config)
        // 实现获取图片指令
        globalEventChannel().subscribeAlways<GroupMessageEvent> {
            // 只有白名单群里的非黑名单成员可以触发指令
            if (group.id !in Config.whiteGroupList || sender.id in Config.banQQList) return@subscribeAlways
            Config.commands.forEachIndexed { index, cmd ->
                // 非触发指令直接跳过当前index的处理
                if (!message.content.startsWith(cmd)) return@forEachIndexed
                val mcb = MessageChainBuilder().append(QuoteReply(source)).append(At(sender))
                val newTime = System.currentTimeMillis()
                val timeInt = newTime - timeFlag
                // 发图冷却，直接跳出事件处理
                if (timeInt < Config.cd) {
                    group.sendMessage(
                        mcb.append("\n${Config.commands.first()}太频繁了，年轻人要节制哦，请冷静一会儿吧\n")
                            .append("Left: ${Config.cd - timeInt} ms")
                            .asMessageChain()
                    )
                    return@subscribeAlways
                }
                // 更新执行指令的时间
                timeFlag = newTime
                // 启动一个下载图片，转发图片的协程
                scope.launch {
                    var timeCost = System.currentTimeMillis()
                    // 指令列表的第一个指令默认为随机调用API
                    // 指令列表的非第一个指令依照顺序依次调用对应的API
                    val url = if (index == 0) {
                        URL(Config.imageAPIs.shuffled().first())
                    } else {
                        URL(Config.imageAPIs[(index - 1) % Config.imageAPIs.size])
                    }
                    // 获取图片
                    val result = downloadImg(url, imageFolder.absolutePath, Config.retryCount)
                    val type = result.first
                    val fileName = result.second
                    timeCost = System.currentTimeMillis() - timeCost
                    // 检查是否正确获取了图片，并发送对应的消息
                    if (fileName == "err") {
                        logger.error("未能正确下载图片，尝试次数${Config.retryCount}，Time: $timeCost ms")
                        group.sendMessage(mcb.append("图片获取失败 >_<").asMessageChain())
                        return@launch
                    }
                    logger.info("获取到图片 $fileName，Time: $timeCost ms")
                    if (type == PicType.UNKNOWN) {
                        logger.error { "暂未支持的图片类型" }
                        return@launch
                    }
                    // 发送正确获取的图片
                    val img = imageFolder.resolve(fileName).uploadAsImage(group, type.ext)
                    group.sendMessage(mcb.append(img).asMessageChain())
                }
            }
        }
        // 实现机器人的私戳管理功能
        globalEventChannel().subscribeAlways<FriendMessageEvent> {
            // 只有机器人管理员可以配置机器人
            if (sender.id != Config.adminQQ) return@subscribeAlways
            scope.launch {
                // 机器人Info指令
                when (message.content) {
                    // 显示帮助信息
                    R.HELP -> sender.sendMessage(R.HELP_INFO)
                    // 显示当前配置信息
                    R.CHECK -> {
                        val imageAPIsInfo =
                            Config.imageAPIs.fold("imageAPIs: \n") { info, api -> "$info\t$api\n" }
                        val whiteGroupListInfo = bot.groups
                            .filter { it.id in Config.whiteGroupList }
                            .fold("whiteGroupList: \n") { info, g ->
                                "$info\t${g.id} ${g.name}\n"
                            }
                        val banQQListInfo = bot.groups
                            .flatMap { it.members }
                            .filter { it.id in Config.banQQList }
                            .distinctBy { it.id }
                            .fold("banQQList: \n") { info, q ->
                                "$info\t${q.id} ${q.nick}\n"
                            }
                        sender.sendMessage(imageAPIsInfo + whiteGroupListInfo + banQQListInfo)
                    }
                }
                val cmds = message.content.split(" ")
                if (cmds.isEmpty()) return@launch
                // 机器人管理指令
                when (cmds[0]) {
                    // 添加白名单群
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
                    // 移除白名单群
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
                    // 添加黑名单群员
                    R.ADD_MEMBER -> when (cmds.size) {
                        1 -> sender.sendMessage("指令缺少参数")
                        2 -> try {
                            val qq = cmds[1].toLong()
                            Config.banQQList.add(qq)
                            sender.sendMessage("成功向群员黑名单中添加了群员$qq")
                        } catch (e: Exception) {
                            sender.sendMessage("错误：${e.message}")
                        }

                        else -> sender.sendMessage("指令存在多余的部分：${cmds.drop(2)}")
                    }
                    // 移除黑名单群员
                    R.REMOVE_MEMBER -> when (cmds.size) {
                        1 -> sender.sendMessage("指令缺少参数")
                        2 -> try {
                            val qq = cmds[1].toLong()
                            when (Config.banQQList.remove(qq)) {
                                true -> sender.sendMessage("成功从群员白名单中移除了群员$qq")
                                false -> sender.sendMessage("错误：群员白名单中不存在群员$qq")
                            }
                        } catch (e: Exception) {
                            sender.sendMessage("错误：${e.message}")
                        }

                        else -> sender.sendMessage("指令存在多余的部分：${cmds.drop(2)}")
                    }
                    // 添加API
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
                    // 移除API
                    R.REMOVE_API -> when (cmds.size) {
                        1 -> sender.sendMessage("指令缺少参数")
                        2 -> try {
                            if (Config.imageAPIs.size == 1) {
                                sender.sendMessage("错误：不能移除最后的图片API链接")
                                return@launch
                            }
                            val api = cmds[1]
                            when (Config.imageAPIs.remove(api)) {
                                true -> sender.sendMessage("成功从图片API库中移除了API:$api")
                                false -> sender.sendMessage("错误：图片API库中不存在API:$api")
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