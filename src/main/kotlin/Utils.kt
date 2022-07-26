package nju.eur3ka

import java.net.URL
import java.nio.channels.Channels
import java.nio.channels.FileChannel
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

fun downloadImg(url: URL, imagePath: String, tryCount: Int): String {
    return if (tryCount > 0) try {
        val fileName = "${System.currentTimeMillis()}.jpg"
        // 基于NIO来下载网络上的图片
        FileChannel.open(
            Paths.get("$imagePath/$fileName"),
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE
        ).use {
            it.transferFrom(Channels.newChannel(url.openStream()), 0, Long.MAX_VALUE)
        }
        fileName
    } catch (e: Exception) {
        PicPlug.logger.error("${e.javaClass.name} : $e.message")
        // 若发生网络异常，则进行有限次的重试
        downloadImg(url, imagePath, tryCount - 1)
    } else "err"
}