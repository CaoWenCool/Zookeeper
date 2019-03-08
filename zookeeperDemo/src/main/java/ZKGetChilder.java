import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.List;

/**
 * @author: admin
 * @create: 2019/3/8
 * @update: 10:46
 * @version: V1.0
 * @detail:
 **/
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
