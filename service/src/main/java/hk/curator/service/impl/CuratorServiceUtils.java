package hk.curator.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;

import org.apache.curator.RetryPolicy;  
import org.apache.curator.framework.CuratorFramework;  
import org.apache.curator.framework.CuratorFrameworkFactory;  
import org.apache.curator.framework.api.ACLProvider;  
import org.apache.curator.framework.api.CuratorEvent;  
import org.apache.curator.framework.api.CuratorListener;  
import org.apache.curator.framework.recipes.cache.ChildData;  
import org.apache.curator.framework.recipes.cache.NodeCache;  
import org.apache.curator.framework.recipes.cache.NodeCacheListener;  
import org.apache.curator.framework.recipes.cache.PathChildrenCache;  
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;  
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;  
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;  
import org.apache.curator.framework.recipes.cache.TreeCache;  
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;  
import org.apache.curator.framework.recipes.cache.TreeCacheListener;  
import org.apache.curator.retry.ExponentialBackoffRetry;  
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;  
import org.apache.zookeeper.Watcher;  
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.apache.zookeeper.ZooDefs.Perms;  
import org.apache.zookeeper.data.ACL;  
import org.apache.zookeeper.data.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import hk.curator.service.ICuratorServiceUtils;
import hk.curator.service.listener.ChildrenPathListener;
import hk.curator.service.listener.NodeListener;
import hk.curator.service.listener.TreeListener;


  

public class CuratorServiceUtils implements ICuratorServiceUtils {  
	
	// 日志对象
	private static final Logger logger = LoggerFactory.getLogger(CuratorServiceUtils.class);

	private static CuratorFramework client;

	private Map<String, PathChildrenCache> pachCahceMap = new HashMap<>();

	private static ExecutorService pool = null;
	public CuratorServiceUtils(String zk_address,int session_timeout,int connction_timeout,int thread_count) {
		ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, Integer.MAX_VALUE);
		client = CuratorFrameworkFactory.newClient(zk_address, session_timeout, connction_timeout, retryPolicy);
		client.start();
		pool = Executors.newFixedThreadPool(thread_count); // 处理watcher的线程池
	}
	

	@Override
	public void listenerChildrenPath(String path, final ChildrenPathListener listener) {
		// TODO Auto-generated method stub
		logger.info("listener zk path={} ..." + path);
		// 监听数据节点的变化情况
		PathChildrenCache childrenCache = pachCahceMap.get(path);
		if (null == childrenCache) {
			childrenCache = new PathChildrenCache(client, path, false);
			pachCahceMap.put(path, childrenCache);
		}
		try {
			childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
				@Override
				public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				String path=event.getData().getPath();
				String value= getData(path);
				listener.listenerChildrenPathEvent(path, value);
				}
			}, pool);
			childrenCache.start(StartMode.BUILD_INITIAL_CACHE);
		} catch (Exception e) {
			logger.error("error", e);
		}
	}


	@Override
	public void ListenerNode(String path, final NodeListener listener) {
	        NodeCache nodeCache = new NodeCache(client, path, false);  
	        nodeCache.getListenable().addListener(new NodeCacheListener() {  
	            @Override  
	            public void nodeChanged() throws Exception {  
	            	listener.listenerNodeEvent();
	            }  
	        });  
	        try {
				nodeCache.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	}
	@Override
	public void ListenerTree(String path, final TreeListener listener) {
		// TODO Auto-generated method stub
		  TreeCache treeCache = new TreeCache(client, path);  
	        //设置监听器和处理过程  
	        treeCache.getListenable().addListener(new TreeCacheListener() {  
	            @Override  
	            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {  
	            	String path=event.getData().getPath();
					String value= getData(path);
					listener.listenerTreeEvent(path, value);
	            }  
	        });  
	        //开始监听  
	        try {
				treeCache.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
	} 
	public boolean createNode(String path, String data, CreateMode createNode) {

		try {
			byte [] bytes =data.getBytes();
			client.create().creatingParentsIfNeeded().withMode(createNode).forPath(path, bytes);
			return true;
		} catch (NodeExistsException e) {
		
			logger.error("error", e);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		return false;
	}

	public boolean deleteNode(String path) {

		try {
			client.delete().forPath(getPath(path));
			return true;
		} catch (NoNodeException e) {
			logger.error("error", e);
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		return false;
	}
	public boolean checkExists(String path) {

		try {
			return client.checkExists().forPath(getPath(path)) == null;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);

		}
	}
	public List<String> getChildren(String path) {

		try {
			return client.getChildren().forPath(getPath(path));
		} catch (NoNodeException e) {
			logger.error("error", e);
			return null;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}
	public boolean UpdateNodeData(String path, String data) {

		try {	
			byte[] Bytes=data.getBytes();
			client.setData().forPath(getPath(path), Bytes);
			return true;
		} catch (NoNodeException e) {
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		return false;
	}
	private String getPath(String path) {
		if (null == path) {
			return null;
		}
		if (path.endsWith("/")) {
			int i = path.lastIndexOf("/");
			if (i > 0) {
				path = path.substring(0, i);
			}
		}
		return path;
	}
	public String getData(String path) {

		try {
			return new String(client.getData().forPath(getPath(path)));
		} catch (NoNodeException e) {
			return null;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}


	   
}  