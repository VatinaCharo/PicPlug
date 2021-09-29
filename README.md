# PicPlug

一个自用的机器人发图插件

出于实际使用考虑，mirai console内建的指令系统不太适合聊天环境使用， 所以也顺便构建了一套比较简单易用的指令处理系统。

## 使用说明

**安装**

去`release`界面下载jar包，然后放置到`plugins`文件下即可

**使用说明**

出于避免打扰群友的考虑，插件默认不对任何群启用功能(~~除非你的群正好命中了配置文件里默认生成的示例群号~~), 启用插件需向`groupList`配置属性下添加对应的群号

**配置文件结构**
```yaml
# 默认的图片API，可依据需求自行更改，但务必保证返回的结果是一张图片，最好是jpg，其他不做可用性保证
imageAPI: 'https://imgapi.cn/cos.php?return=img'
# 插件生效的群列表
groupList: 
  - 1234567890
  - 9876543210
# 插件的指令前缀，仅此前缀开头的聊天信息会被识别为命令
# 示例：
#     Alice： !!ping
#      Bot:  @Alice !!pong
commandPrefix: '!!'
# 插件下载的图片储存位置
imageStorage: './data/image/PicPlug/'
```
**当前支持的功能**

* `!!help` 机器人会回复帮助信息并@你
* `!!ping` 机器人会回复`pong`并@你
* `!!gkd` 机器人会从指定的图片api处获取一张图片，然后发到群里并@你

`!!help`指令演示:
![help command demo](https://github.com/VatinaCharo/PicgoPicAssets/blob/09c4ad04de8bb732af59b66e5130ca1d83a194ff/pic/picplug_help_command.png)

## 项目结构

```text
Main Class : vcg.Plugin

main/
├── java/
│   └── vcg/
│       ├── Commands/
│       │   ├── Command.java
│       │   ├── CommandsManager.java
│       │   ├── GetRandImage.java
│       │   ├── Help.java
│       │   ├── Ping.java
│       │   ├── PureCommand.java
│       │   └── SimpleCommand.java
│       ├── Config/
│       │   └── Config.kt
│       ├── Plugin.java
│       └── Utils/
│           ├── ImgDownloader.java
│           └── Resources.java
└── resources/
    └── META-INF/
        └── services/
            └── net.mamoe.mirai.console.plugin.jvm.JvmPlugin
```
