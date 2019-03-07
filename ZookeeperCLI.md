#Zookeeper CLI
Zookeeper命令行界面（CLI）用于于Zookeeper集合进行交互以进行开发。它有助于调试和解决不同的选项。要执行ZookeeperCLI操作。
首先打开Zookeeper服务器，然后打开Zookeeper客户端，一旦客户端启动，你可以执行一下操作：
 （1） 创建znode
 （2） 获取数据
 （3）监视znode的变化
 （4）设置数据
 （5）创建znode的子节点
 （6）列出znode的子节点
 （7）检查状态
 （8）移除/删除znode
 
 ##创建znodes
 用给定的路径创建一个znode。flag参数指定创建的znode是临时的，持久的还是顺序的。默认情况下，所有znode都是持久的。
 当会话过期或客户端断开连接时，临时节点（flag:-e）将自动删除。
 顺序节点保证znode路径是唯一的。
 Zookeeper集合将向znode路径填充10序列号。例如，znode路径 /myapp将转换为/myapp000000001,下一个序列号将为/myapp00000000002
 .如果没有指定flag，则znode被认为是持久的。
 
 语法：
    create /path/data
 示例：
    create /FirstZnode “MyFirstZookeeper-app”
 
 要创建顺序节点，请添加flag：-s
 语法：
    create -s /path/data
 示例
    create -s /FirstZnode second-data
 
 要创建临时节点
 语法：
    create -e /path/data
 示例：
  create -e /SecondZnode "Ephemeral-data"
 
 当客户端断开连接时，临时节点将被删除，你可以通过退出Zookeeper CLI，然后重新打开CLI来尝试
 
 获取数据
 它返回znode的关联数据和指定znode的元数据。你将获得信息，例如上次修改数据的时间，修改的位置以及数据的相关信息。此CLI还
 用于分配监视器以显示数据相关的通知。
 
 语法：
 get /path
 实例
 get /FirstZnode
 
 要访问顺序节点，必须输入znode的完整路径。
 实例 get /FirstZnode0000000023
 
 
 ##Watch 监视
 当指定的znode或znode的自数据更改时，监视器会显示通知。你只能在get命令中设置watch
 
 语法
    get /path [watch] 1
 实例
    get /FirstZnode 1
 输出
 
 输出类类似于普通的get命令，但它会等待后台等待znode更改。
 
 ###设置数据
 设置指定znode的数据，完整此设置操作后，你可以使用get CLI命令检查数据
 语法：
    set /path /data
 示例：
    set /SecondZnode Data-updated
 
 ###创建子项/子节点
 创建子节点类似于创建新的znode。唯一的区别是，子znode的路径也将具有父路径。
 
 语法：
    create /parent/path/subnode/path /data
 示例：
    create /FirstZnode/Child1 firstchildren
 
 ###列出子项
 此命令用于列出和显示znode的子项
 语法：
    ls /path
 示例
    ls /MyFirstZnode
 输出：
 
 ###检查状态
 状态描述指定znode的元数据。它包含时间戳，版本号，ACL，数据长度和子znode等细项
 语法：
    stat /path
 示例：
    stat /FirstZnode
 
 
 ###移除Znode
 移除指定的znode并递归其所有子节点。只有这样的znode可用的情况下才会发生。
 语法：
    rmr /path
 示例
    rmr /FirstZnode
    
 删除（delete/path）命令类似于remove命令，除了它只适用于没有子节点的znode.