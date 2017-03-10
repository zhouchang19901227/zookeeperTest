package hk.curator.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hk.curator.service.impl.CuratorServiceUtils;

public class HKCuratorService {

	public ICuratorServiceUtils curatorSU;
	public HKCuratorService(String zk_address,int session_timeout,int connction_timeout,int thread_count)
	{
		curatorSU=new CuratorServiceUtils(zk_address,session_timeout,connction_timeout,thread_count);
	}
	

}
