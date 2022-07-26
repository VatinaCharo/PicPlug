# PicPlug

<p align="center">  
<img alt="GitHub top language" src="https://img.shields.io/github/languages/top/VatinaCharo/PicPlug?style=plastic">
<img alt="GitHub code size in bytes" src="https://img.shields.io/github/languages/code-size/VatinaCharo/PicPlug">
<img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/VatinaCharo/PicPlug">
<img alt="GitHub all releases" src="https://img.shields.io/github/downloads/VatinaCharo/PicPlug/total">
<img alt="GitHub Repo stars" src="https://img.shields.io/github/stars/VatinaCharo/PicPlug?style=social">
</p>

**目前已使用kotlin进行了重构，老版本请前往[java分支](https://github.com/VatinaCharo/PicPlug/tree/java)获取**

一个简单的机器人发图插件

使用kotlin重新精简了代码，并优化了旧版的使用体验，
日常的管理和配置都可以通过和机器人私聊完成

## 使用说明

**安装**

去`release`界面下载jar包，然后放置到`plugins`文件下即可

**说明**

出于避免打扰群友的考虑，插件默认不对任何群启用功能(~~除非你的群正好命中了配置文件里默认生成的示例群号~~),
启用插件需向`groupList`配置属性下添加对应的群号

**管理指令**

| 指令名              | 作用        |
|------------------|-----------|
| help             | 显示帮助信息    |
| check            | 显示当前的配置情况 |
| g+ \<group id\>  | 向群白名单中添加一个群     |
| g- \<group id\>  |  从群白名单中移除一个群         |
| m+ \<member id\> |  向群员白名单中添加一个群员         |
| m- \<member id\> |  从群员白名单中移除一个群员         |
| l+ \<api link\>  |  向API库中添加一个图片API链接         |
| l- \<api link\>  |  从API库中移除一个图片API链接         |

需要注意的是，管理员默认加入白名单，无法删除；
图片API无法删空，而且添加API链接时不会检查链接的可用性，需要自行确保正确，
并且api链接应该是直接返回图片而非返回包含图片地址等信息的json

**配置文件结构**

```yaml
# 默认的图片API，可依据需求自行更改，但务必保证返回的结果是一张图片，最好是jpg，其他不做可用性保证
imageAPIs:
  - 'https://imgapi.cn/cos.php?return=img'
whiteGroupList:
  - 1234567890
whiteQQList:
  - 123123123
  - 3067710367
adminQQ: 123123123
  触发发图指令
commands:
  - !!gkd
retryCount: 5
```