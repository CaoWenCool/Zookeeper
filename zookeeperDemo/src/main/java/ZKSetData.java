import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

/**
 * @author: admin
 * @create: 2019/3/8
 * @update: 10:33
 * @version: V1.0
 * @detail:
 **/
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
