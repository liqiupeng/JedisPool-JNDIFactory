package com.sas.redis.client.pool;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;


public class RedisClientFactory implements ObjectFactory{

	@Override
	public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment)
			throws Exception {
	     
		
	      Reference ref = (Reference) obj;
	      String host = null;
	      int port = 0;
	      
	      Enumeration addrs = ref.getAll();
	      while (addrs.hasMoreElements()) {
	          RefAddr addr = (RefAddr) addrs.nextElement();
	          String propName = addr.getType();
	          String propValue = (String) addr.getContent();
	          if ("url".equals(propName)) {
				host = propValue;
			}
	          if ("port".equals(propName)) {
				port = Integer.valueOf(propValue);
			}
	      }
	      Jedis jedis= new Jedis();
	      jedis.setHost(host);
	      jedis.setPort(port);
	      // Return the customized instance
	      return jedis;
	}

}
