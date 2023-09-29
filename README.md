# PicPlug

[![Language](https://img.shields.io/github/languages/top/VatinaCharo/PicPlug?style=plastic)](https://github.com/VatinaCharo/PicPlug)

[![Code Size](https://img.shields.io/github/languages/code-size/VatinaCharo/PicPlug)](https://github.com/VatinaCharo/PicPlug)
[![Release](https://img.shields.io/github/v/release/VatinaCharo/PicPlug)](https://github.com/VatinaCharo/PicPlug/releases/latest)
[![Download](https://img.shields.io/github/downloads/VatinaCharo/PicPlug/total)](https://github.com/VatinaCharo/PicPlug/releases)
[![Star](https://img.shields.io/github/stars/VatinaCharo/PicPlug?style=social)](https://github.com/VatinaCharo/PicPlug)

**目前已使用kotlin进行了重构，老版本请前往[java分支](https://github.com/VatinaCharo/PicPlug/tree/java)获取**

一个简单的机器人发图插件

使用kotlin重新精简了代码，并优化了旧版的使用体验，
日常的管理和配置都可以通过和机器人私聊完成

## 使用说明

### 安装

去`release`界面下载jar包，然后放置到`plugins`文件下即可

### 说明

出于避免打扰群友的考虑，插件默认不对任何群启用功能(~~除非你的群正好命中了配置文件里默认生成的示例群号~~),
控制插件的启用可以通过机器人的私聊窗口进行白名单的管理，也可以手动修改配置文件

群员的管理机制采用黑名单机制（添加白名单确实太麻烦）

**使用前请配置好管理员qq**

### 配置管理指令

**特别提醒：由于mirai的配置文件自动保存机制，
通过机器人指令完成的修改需要等待一段时间才会保存至配置文件中，
如果控制台还没自动保存就关闭了mirai-console，会导致指令修改无效。
一般配置后等待几分钟即可完成保存**

| 指令名              | 作用                |
|------------------|-------------------|
| help             | 显示帮助信息            |
| check            | 显示当前的配置情况         |
| g+ \<group id\>  | 向群白名单中添加一个群       |
| g- \<group id\>  | 从群白名单中移除一个群       |
| m+ \<member id\> | 向群员黑名单中添加群员       |
| m- \<member id\> | 从群员黑名单中移除群员       |
| l+ \<api link\>  | 向API库中添加一个图片API链接 |
| l- \<api link\>  | 从API库中移除一个图片API链接 |

图片API无法删空，而且添加API链接时不会检查链接的可用性，需要自行确保正确，
并且api链接应该是直接返回图片而非返回包含图片地址等信息的json

### 配置文件结构

```yaml
# 默认的图片API，可依据需求自行更改，但务必保证返回的结果是一张图片，最好是jpg，其他不做可用性保证
imageAPIs:
  - 'https://imgapi.cn/cos.php?return=img'
whiteGroupList:
  - 1234567890
banQQList:
  - 123123123
adminQQ: 123123123
# 触发发图指令
# 第1个指令为随机触发设置的API
# 如果有，第2个指令固定触发第1个API
# 如果有，第3个指令固定触发第2个API
# 依次类推
# ！！！如果设定的指令数大于(API数+1)，则自动取模从头开始计算！！！
# 例如： 3个API 5个指令
#       则第5个指令触发第一个API
commands:
  - !!gkd
# 图片下载失败之后的重试次数
retryCount: 5
# 发图冷却时间
cd: 1000
```