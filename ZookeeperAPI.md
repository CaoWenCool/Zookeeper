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
ZookeeperAPI的核心部分是Zookeeper类。它提供了在其构造函数中连接Zookeeper集合的选项。并具有以下方法：
    （1） connect 连接到Zookeeper集合
    （2） create 创建znode
    （3） exists 检查znode是否存在其信息
    （4） getData 从特定的znode获取数据
    （5） setData 在特定的znode获取数据
    （6） getChildren 获取特定的znode中的所有子节点
    （7） delete 删除特定的znode及其所有子项
    （8） close 关闭连接
    
##连接到Zookeeper集合
Zookeeper 类通过其构造函数提供connect功能。构造函数的签名如下：
    Zookeeper（String connnectionString,int sessionTimeout,Watcher watcher）
   
    connectionString Zookeeper集合主机
    sessionTimeout 会话超时
    watcher 实现监视器界面的对象。Zookeeper集合通过监视器对象返回连接状态
    
创建一个新的帮助类ZookeeperConnection，并添加一个方法connect。connect方法创建一个Zookeeper对象，连接到Zookeeper集合，
返回对象。

这里CountDownLatch用于停止（等待）主进程。一旦客户端与Zookeeper集合连接，监视器回调就会被调用，并且监视器回调函数调用
CountDownLatch的countDown方法来释放锁，在主进程中await。

    public class ZookeeperConnection {

        private ZooKeeper zoo;
        final CountDownLatch connectedSignal = new CountDownLatch(1);
    
        public ZooKeeper connect(String host) throws IOException,InterruptedException{
    
            zoo = new ZooKeeper(host, 5000, new Watcher() {
    
                public void process(WatchedEvent we) {
                    if(we.getState() == Event.KeeperState.SyncConnected){
                        connectedSignal.countDown();
                    }
                }
            });
    
            connectedSignal.await();
            return zoo;
        }
    
        public void close()throws InterruptedException{
            zoo.close();
        }
    }
    
## 创建Znode
Zookeeper类提供了Zookeeper集合创建一个新的znode的create方法。如下：
    create(String path,byte[] data,List<ACL> acl,CreateMode createMode)

   (1) path znode路径。例如，/myapp1，/myapp2 /myapp1/mydata1
  （2）data  要存储在指定znode路径中的数据
  （3）cl-要创建的节点的访问控制列表。ZookeeperAPI提供了一个静态接口ZooDefs.Ids来获取一些基本的acl列表。例如，
  ZooDefs.Ids.OPEN_ACL_UNSAFE返回打开znode的acl列表。
  （4）createMode -- 节点类型，即临时，顺序或者两者。这是一个枚举
  
  我们可以创建一个新的java应用程序来检查ZookeeperAPI的create功能。创建文件ZKCreate.java。在main方法中，创建一个类型为
  ZookeeperConnection的对象，并且调用connect方法连接到Zookeeper集合。connect方法将返回Zookeeper对象zk。现在，使用自定义
  path和data调用zk对象的create方法。创建znode的完整程序代码如下：
  
    public class ZKCreate {
  
      private static ZooKeeper zk;
  
      private static ZookeeperConnection conn;
  
      public static void create(String path,byte[] data)throws KeeperException,InterruptedException{
          zk.create(path,data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
      }
  
      public static void main(String[] args) {
          String path = "/MyFirstZnode";
  
          byte[] data = "My first zookeeper app".getBytes();
  
          try{
              conn = new ZookeeperConnection();
              zk = conn.connect("127.0.0.1");
              create(path,data);
              conn.close();
          }catch (Exception e){
              System.out.println(e.getMessage());
          }
      }
    }

可以使用ZooKeeper CLI zkCli.sh 进行检查

###Exists 检查Znode的存在
Zookeeper类型提供了exists 方法来检查znode的存在。如果指定的znode存在，则返回一个znode的元数据。如下：
    
    exists(String path,boolean watcher)

我们可以创建一个新的ZookeeperAPI的exists功能。如下：

    public class ZKExisits {

        private static ZooKeeper zk;
        private static ZookeeperConnection conn;
    
        public static Stat znode_exists(String path)throws KeeperException,InterruptedException{
            return zk.exists(path,true);
        }
    
        public static void main(String[] args)throws InterruptedException,KeeperException {
            String path = "/MyFirstZnode";
            try{
                conn = new ZookeeperConnection();
                zk = conn.connect("127.0.0.1");
                Stat stat = znode_exists(path);
    
                if(stat != null){
                    System.out.println("Node exists and the node version is"+stat.getVersion());
                }else{
                    System.out.println("Node does not exists");
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
    
###getData方法
ZooKeeper类提供了getData方法来获取附加在指定znode中的数据及其状态。

    getData(String path,Watcher watcher,Stat stat)
  （1） path znode 路径
  （2） watcher 监视器类型的回调函数。当指定的znode的数据改变时.Zookeeper集合将通过监视器回调进行通知。这是一次性通知
  （3） stat 返回znode的元数据
  
    public class ZKGetData {
  
      private static ZooKeeper zk;
      private static ZookeeperConnection conn;
      public static Stat znode_exists(String path)throws KeeperException,InterruptedException{
          return zk.exists(path,true);
      }
  
      public static void main(String[] args)throws InterruptedException,KeeperException {
          String path = "/MyFirstZnode";
          final CountDownLatch connectedSignal = new CountDownLatch(1);
  
          try{
              conn = new ZookeeperConnection();
              zk = conn.connect("127.0.0.1");
              Stat stat = znode_exists(path);
  
              if(stat != null){
                  byte[] b = zk.getData(path, new Watcher() {
                      public void process(WatchedEvent we) {
                         if(we.getType() == Event.EventType.None){
                             switch (we.getState()){
                                 case Expired: connectedSignal.countDown();
                                 break;
                             }
                         }else{
                             String path = "/MyFirstZnode";
                             try{
                                 byte[] bn = zk.getData(path,false,null);
                                 String data = new String(bn,"UTF-8");
                                 System.out.println(data);
                                 connectedSignal.countDown();
                             }catch (Exception e){
                                 System.out.println(e.getMessage());
                             }
                         }
                      }
                  },null);
                  String data = new String(b,"UTF-8");
                  System.out.println(data);
                  connectedSignal.await();
              }else{
                  System.out.println("Node does not exists");
              }
          }catch (Exception e){
              System.out.println(e.getMessage());
          }
      }
    }
    
###setData 方法
ZooKeeper类提供setData方法来修改znode中附加的数据。如下：

    setData(String path,byte[] data,int version)
   
   （1） path znode 路径
   （2） data -要存储在指定的znode路径中的数据
   （3） version -znode的当前的版本。每当数据更改时，Zookeeper会更新znode的版本号
   
    public class ZKSetData {
   
       private static ZooKeeper zk;
       private static ZookeeperConnection conn;
   
       public static void update(String path,byte[] data)throws KeeperException,InterruptedException{
           zk.setData(path,data,zk.exists(path,true).getVersion());
       }
   
       public static void main(String[] args) throws InterruptedException,KeeperException{
           String path = "/MyFirstZnode";
           byte[] data = "Success".getBytes();
   
           try{
               conn = new ZookeeperConnection();
               zk = conn.connect("127.0.0.1");
               update(path,data);
           }catch (Exception e){
               System.out.println(e.getMessage());
           }
       }
    }
    
###getChildren
ZooKeeper类提供getChildren方法类获取特定znode的所有子节点。

    getChilder(String path,Watcher watcher)
   
   （1） path Znode路径
   （2） watcher 监视器类型的回调函数，当指定的znode被删除或znode下的子节点被创建/删除时，Zookeeper集合将进行通知。
   
    public class ZKGetChilder {
   
       private static ZooKeeper zk;
       private static ZookeeperConnection conn;
   
       public static Stat znode_exists(String path)throws KeeperException,InterruptedException{
           return zk.exists(path,true);
       }
   
       public static void main(String[] args)throws InterruptedException,KeeperException {
           String path = "/MyFirstZnode";
           try{
               conn = new ZookeeperConnection();
               zk = conn.connect("127.0.0.1");
               Stat stat = znode_exists(path);
   
               if(stat != null){
                   List<String> childern = zk.getChildren(path,false);
                   for(int i= 0;i<childern.size();i++){
                       System.out.println(childern.get(i));
                   }
               }else{
                   System.out.println("Nodes does not exists");
               }
           }catch (Exception e){
               System.out.println(e.getMessage());
           }
       }
    }
    
### 删除Znode
ZooKeeper 类提供了delete方法来删除指定的znode。
     
     delete(String path,int version)
   
   （1） path znode路径
   （2） version znode当前的版本
   
    public class ZKDelete {
   
       private static ZooKeeper zk;
       private static ZookeeperConnection conn;
   
       public static void delete(String path)throws KeeperException,InterruptedException{
           zk.delete(path,zk.exists(path,true).getVersion());
       }
   
       public static void main(String[] args)throws InterruptedException,KeeperException {
           String path = "/MyFirstZnode";
   
           try{
               conn = new ZookeeperConnection();
               zk = conn.connect("127.0.0.1");
   
               delete(path);
           }catch (Exception e){
               System.out.println(e.getMessage());
           }
       }
    }