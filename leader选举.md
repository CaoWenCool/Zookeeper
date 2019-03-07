#leader选举
让我们分析如何子啊Zookeeper集合中选举leader节点。考虑一个集群中有N个节点。leader选举的过程如下：
   （1） 所有节点创建具有相同路径 /app/leader_election/guid_的顺序、临时节点
   （2） ZooKeeper集合将附加10位序列号到路径，创建的znode将是/app/leader_election/guid_0000000001,
   app/leader_election/guid_0000000002等；
   （3）对于follower节点监视下一个具有最小数字的节点成为leader，而所有其他节点都是follower。
   （4）每个follower节点监视下一个具有最小数字的znode。例如：创建
   znode/app/leader_election/guid_0000000008的节点将监视znode/app/leader_election/guid_0000000007
   （5）如果leader关闭，则相应的znode/app/leader_electionN会被删除
   （6）下一个在线follower节点将通过监视器获得关于leader移除的通知。
   （7）下一个在线follower节点检查是否存在其他具有最小数字的znode。如果没有，那么它将承担leader的角色。否则，它找到的
   创建具有最小数字的znode的节点将作为leader。
   （8）类似的，所有其他follower节点选举创建具有最小数字的znode的节点作为leader。
   