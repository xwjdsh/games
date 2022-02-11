# games

以前学习 Java Swing 时写的小项目，代码结构清晰，简单易读，注释完备。初学者能从中学习到数据结构，面向对象等基础知识。

## 截图
### 入口
![](https://raw.githubusercontent.com/xwjdsh/games/master/snapshot/entry.png)

### 俄罗斯方块
![](https://raw.githubusercontent.com/xwjdsh/games/master/snapshot/game1.png)
![](https://raw.githubusercontent.com/xwjdsh/games/master/snapshot/game1-setting.png)

### 贪吃蛇
![](https://raw.githubusercontent.com/xwjdsh/games/master/snapshot/game2.png)
![](https://raw.githubusercontent.com/xwjdsh/games/master/snapshot/game2-setting.png)

## 如何运行
### 通过 Maven 打包
```
cd $project_root
mvn -f pom.xml clean package
java -jar target/games-0.0.1-SNAPSHOT.jar
```
### 通过 Docker
https://github.com/xwjdsh/games/tree/master/docker

## 协议
[Apache License](https://github.com/xwjdsh/games/blob/master/LICENSE)
