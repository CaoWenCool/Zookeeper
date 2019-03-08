import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import sun.security.krb5.internal.KdcErrException;

/**
 * @author: admin
 * @create: 2019/3/8
 * @update: 10:10
 * @version: V1.0
 * @detail:
 **/
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
