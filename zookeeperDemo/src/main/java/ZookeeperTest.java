import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author: admin
 * @create: 2019/3/7
 * @update: 10:10
 * @version: V1.0
 * @detail:
 **/
public class ZookeeperTest {

    //根节点
    public static final String ROOT = "/root-zero";
    public static void main(String[] args) {
        //创建一个与服务器的连接
        try {
            ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 30000, new Watcher() {
                //监控所有被出发的事件
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("状态"+watchedEvent.getState()+":"+watchedEvent.getType()+":"+
                            watchedEvent.getWrapper()+watchedEvent.getPath());
                }
            });
            //创建一个总的目录，并不控制权限，这些需要用到持久化节点，下面的节点创建容易出错
            try {
                zk.create(ROOT,"root-zero".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);

                //然后增加一个节点，PERSISTENT_SEQUENTIAL 类型会自动加上 0000000000  自增的后缀
                zk.create(ROOT+"/one","one".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT_SEQUENTIAL);

                //再增加一个节点，EPHEMERAL session过期了就会自动删除
                zk.create(ROOT+"two","two".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL);

                //我们也可以 来看看一共监视了多少点节点
                List<String> roots = zk.getChildren(ROOT,true);
                System.out.println(Arrays.toString(roots.toArray()));

                for(String node:roots){
                    //删除节点
                    zk.delete(ROOT+"/"+node,-1);
                }
                //根目录得最后撒谎呢粗话
                zk.delete(ROOT,-1);
                zk.close();
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
