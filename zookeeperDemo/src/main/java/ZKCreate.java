import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author: admin
 * @create: 2019/3/8
 * @update: 9:31
 * @version: V1.0
 * @detail:
 **/
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
