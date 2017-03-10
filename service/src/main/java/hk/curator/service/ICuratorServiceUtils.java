package hk.curator.service;

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

import hk.curator.service.listener.ChildrenPathListener;
import hk.curator.service.listener.NodeListener;
import hk.curator.service.listener.TreeListener;


  

public interface ICuratorServiceUtils {  
	
	public void listenerChildrenPath(String path,ChildrenPathListener listener);

    public void ListenerNode(String path,NodeListener listener);
    
    public void ListenerTree(String path,TreeListener listener);
    
    public boolean createNode(String path, String data, CreateMode createNode);
	public boolean deleteNode(String path);
	public boolean checkExists(String path);
	public List<String> getChildren(String path);
	public boolean UpdateNodeData(String path, String data);
	public String getData(String path);
    
}  