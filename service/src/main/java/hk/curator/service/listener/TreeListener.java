package hk.curator.service.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;

public interface TreeListener {

	public void listenerTreeEvent(String path, String value);
}
