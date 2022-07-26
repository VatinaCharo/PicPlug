package nju.eur3ka

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object Config : AutoSavePluginConfig("config") {
    val imageAPIs: MutableList<String> by value(mutableListOf(R.IMG_API_URL))
    val whiteGroupList: MutableList<Long> by value(mutableListOf(1234567890))
    val whiteQQList: MutableList<Long> by value(mutableListOf(987654321))
    val adminQQ: Long by value<Long>(123123123)
    val commands: List<String> by value(listOf(R.DEFAULT_COMMAND))

    // 当网络连接出现故障时，重试的次数
    val retryCount: Int by value(R.DEFAULT_RETRY_COUNT)
}