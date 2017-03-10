package hk.curator.client;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.zookeeper.CreateMode;

import hk.curator.service.HKCuratorService;
import hk.curator.service.listener.ChildrenPathListener;
import hk.curator.service.listener.NodeListener;
import hk.curator.service.listener.TreeListener;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	//监听path
    	//输入构造参数 链接zk
    	HKCuratorService ser=new HKCuratorService("127.0.0.1:2181",60 * 1000,5 * 1000,5);
    	ser.curatorSU.listenerChildrenPath("/hk_config_test/test", new ChildrenPathListener() {

			@Override
			public void listenerChildrenPathEvent(String path, String value) {
				// TODO Auto-generated method stub
				 System.out.println("触发path监听   :"+path+"  value: "+value);
			}			
			
		});
//     	ser.curatorSU.ListenerTree("/hk_config_test/test",new TreeListener() {
//
//			@Override
//			public void listenerTreeEvent(String path, String value) {
//				// TODO Auto-generated method stub
//				 System.out.println("触发tree监听   :"+path+"  value: "+value);
//			}
//			
//		
//		});

    	//创建
    	if(ser.curatorSU.checkExists("/hk_config_test/test/1111"))
    	{
    		 boolean ok=ser.curatorSU.createNode("/hk_config_test/test/1111", "的所得税", CreateMode.PERSISTENT);
    	}
    	
//    	ser.curatorSU.ListenerNode("/hk_config_test/test",new NodeListener() {
//			
//			@Override
//			public void listenerNodeEvent() {
//				// TODO Auto-generated method stub
//				 System.out.println("触发节点监听   /hk_config_test/test/1111");
//			}
//		});
//    	
   
    	//删除
     	if(ser.curatorSU.checkExists("/hk_config_test/test/1111"))
    	{
    		 boolean ok=ser.curatorSU.deleteNode("/hk_config_test/test/1111");
    	}
     	
    	//修改
     	if(ser.curatorSU.checkExists("/hk_config_test/test/1111"))
    	{
    		 boolean ok=ser.curatorSU.UpdateNodeData("/hk_config_test/test/1111","修改");
    		 System.out.println("/hk_config_test/test/1111");
    	}
    	//查询
     	if(ser.curatorSU.checkExists("/hk_config_test/test/1111"))
    	{
    		 String value=ser.curatorSU.getData("/hk_config_test/test/1111");
    		 System.out.println("值:"+value);
    	}
    	while(true)
		{
			  try {
				Thread.sleep(100 * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    }

}