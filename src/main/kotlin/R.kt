package nju.eur3ka

object R {
    const val DEFAULT_COMMAND = "!!gkd"
    const val IMG_API_URL = "https://imgapi.cn/cos.php?return=img"
    const val DEFAULT_RETRY_COUNT = 5
    const val DEFAULT_CD = 10_000

    //    const val HELP_CN = "图片小助手功能"
    const val HELP = "help"

    //    const val CHECK_CN = "检查"
    const val CHECK = "check"

    //    const val ADD_GROUP_CN = "群+"
    const val ADD_GROUP = "g+"

    //    const val REMOVE_GROUP_CN = "群-"
    const val REMOVE_GROUP = "g-"

    //    const val ADD_MEMBER_CN = "群员+"
    const val ADD_MEMBER = "m+"

    //    const val REMOVE_MEMBER_CN = "群员-"
    const val REMOVE_MEMBER = "m-"

    //    const val ADD_API_CN = "链接+"
    const val ADD_API = "l+"

    //    const val REMOVE_API_CN = "链接-"
    const val REMOVE_API = "l-"

    val HELP_INFO =
        """
            ---
            help            : 显示本帮助信息
            ---
            check           : 显示当前的配置情况
            ---
            g+ <group id>   : 向群白名单中添加一个群
            ---
            g- <group id>   : 从群白名单中移除一个群
            ---
            m+ <member id>  : 向群员白名单中添加一个群员
            ---
            m- <member id>  : 从群员白名单中移除一个群员
            ---
            l+ <api link>   : 向API库中添加一个图片API链接
            ---
            l- <api link>   : 从API库中移除一个图片API链接
            ---
            演示：
            A: g+ 123456789
            B: 成功向群白名单中添加了群123456789
            A: m+ 987654321
            B: 成功从群员白名单中移除了群员987654321
        """.trimIndent()
}