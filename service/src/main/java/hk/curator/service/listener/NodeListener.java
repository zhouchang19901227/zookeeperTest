package hk.curator.service.listener;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

public interface NodeListener {

	public void listenerNodeEvent();
}
