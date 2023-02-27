package nju.eur3ka

import nju.eur3ka.ext.toHexString
import java.net.URL
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.nio.channels.FileChannel
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

fun downloadImg(url: URL, imagePath: String, tryCount: Int): Pair<PicType, String> {
    return if (tryCount > 0) try {
        var fileName = "${System.currentTimeMillis()}"
        val path = Paths.get("$imagePath/$fileName")
        // 基于NIO来下载网络上的图片
        FileChannel.open(
            path,
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE
        ).use {
            it.transferFrom(Channels.newChannel(url.openStream()), 0, Long.MAX_VALUE)
        }
        // 读取图片文件前4位字节 检测文件类型
        val byteBuffer = ByteBuffer.allocate(4)
        FileChannel.open(path, StandardOpenOption.READ).use { it.read(byteBuffer) }
        val header = byteBuffer.array().toHexString().uppercase()
//        PicPlug.logger.error { "header: $header" }
        val type = when {
            header.contains(PicType.JPEG.header) -> PicType.JPEG
            header.contains(PicType.PNG.header) -> PicType.PNG
            header.contains(PicType.GIF.header) -> PicType.GIF
            header.contains(PicType.BMP.header) -> PicType.BMP
            else -> PicType.UNKNOWN
        }
        fileName = fileName.plus(".").plus(type.ext)
        // 重命名图片文件
        path.toFile().renameTo(Paths.get("$imagePath/$fileName").toFile())
        type to fileName
    } catch (e: Exception) {
        PicPlug.logger.error("${e.javaClass.name} : $e.message")
        // 若发生网络异常，则进行有限次的重试
        downloadImg(url, imagePath, tryCount - 1)
    } else PicType.UNKNOWN to "err"
}