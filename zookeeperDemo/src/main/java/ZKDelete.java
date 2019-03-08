import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author: admin
 * @create: 2019/3/8
 * @update: 10:53
 * @version: V1.0
 * @detail:
 **/
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
