#ZookeeperAPI 
使用ZookeeperAPI，一个用程序可以连接，交互，操作数据，协调，最后断开与Zookeeper集合的连接。
ZookeeperAPI具有丰富的功能，以简单和安全的方式获得Zookeeper集合的所有功能。ZookeeperAPI提供同步和异步方法

##ZookeeperAPI的基础知识
与Zookeeper集合进行交互的应用程序称为Zookeeper客户端或简称客户端
Znode是Zookeeper集合的核心组件，ZookeeperAPI提供了一小组方法使用Zookeeper集合来操纵znode的所有细节。
客户端应该遵循一下步骤，与zookeeper集合进行清晰和干净的交互
（1） 连接Zookeeper集合。Zookeeper集合为客户端分配会话ID
（2） 定期向服务器发送心跳。否则，Zookeeper集合将过期会话ID，客户端需要重新连接。
（3） 只要会话ID处于活动状态，就可以获取/设置znode
（4）所有任务完成后，断开与Zookeeper集合的连接。如果客户端长时间不活动，则Zookeeper集合将自动断开客户端。

##JAVA绑定
ZookeeperAPI的核心部分是Zookeeper类。它提供了在其