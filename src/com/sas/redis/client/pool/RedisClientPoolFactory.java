package com.sas.redis.client.pool;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;

import java.util.Map;
import java.util.Properties;

import javax.naming.RefAddr;
import javax.naming.Reference;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisClientPoolFactory implements ObjectFactory{
//	private final static Log LOG = LogFactory.getLog(RedisClientPoolFactory.class);
	
	public final static String PROP_URL                      = "url"; //
	public final static String PROP_PORT                     = "port"; //
	public final static String PROP_TIMEOUT                  = "timeOut";  //
	public final static String PROP_MAXIDLE                  = "maxIdle";  //
	public final static String PROP_MINIDLE                  = "minIdle";  //
	public final static String PROP_MAXWAIT                  = "maxWait";
	public final static String PROP_MAXTOTAL                 = "maxTotal"; //
	public final static String PROP_BLOCK_WHEN_EXHAUSTED     = "blockWhenExhausted"; //
	public final static String PROP_PASSWD                     = "passwd"; //
	public final static String PROP_LIFO                     = "lifo"; // 
	public final static String PROP_TEST_ON_BORROW           = "testOnBorrow"; //
	public final static String PROP_TEST_WHILEIDLE           = "testWhileIdle"; //
	
    private final static String[] ALL_PROPERTIES = {
    		PROP_URL,
    		PROP_PORT,
    		PROP_TIMEOUT,
    		PROP_MAXIDLE, 
    		PROP_MINIDLE,
    		PROP_MAXWAIT, 
    		PROP_MAXTOTAL,
    		PROP_BLOCK_WHEN_EXHAUSTED,
    		PROP_PASSWD,
    		PROP_LIFO,
    		PROP_TEST_ON_BORROW,
    		PROP_TEST_WHILEIDLE
    };

	@Override
	public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable<?, ?> environment)
			throws Exception {

        if ((obj == null) || !(obj instanceof Reference)) {
            return null;
        }
        Reference ref = (Reference) obj;
        Properties properties = new Properties();
            for (int i = 0; i < ALL_PROPERTIES.length; i++) {
                String propertyName = ALL_PROPERTIES[i];
                RefAddr ra = ref.get(propertyName);
                if (ra != null) {
                    String propertyValue = ra.getContent().toString();
                    properties.setProperty(propertyName, propertyValue);
                }
            }
        	return createJedisPool(properties);
	}
	
    protected JedisPool createJedisPool(Properties properties) throws Exception {
    	JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    	setPoolconfig(jedisPoolConfig, properties);
    	String host = "127.0.0.1";
    	if ((String) properties.get(PROP_URL) != null) {
    		host = (String) properties.get(PROP_URL);
		}
    	int timeOut = 10000;
    	if ((String) properties.get(PROP_TIMEOUT) != null) {
    		timeOut = Integer.valueOf((String) properties.get(PROP_TIMEOUT));
		}
    	int port = Integer.valueOf((String)properties.get(PROP_PORT));
    	String passwd = null;
    	passwd = (String)properties.get(PROP_PASSWD);
    	JedisPool jedisPool = null;
        	if (passwd != null) {
        		jedisPool = new JedisPool(jedisPoolConfig,host,port,timeOut,passwd);
    		}
        	else {
        		jedisPool = new JedisPool(jedisPoolConfig,host,port,timeOut);
    		}
        return jedisPool;
    }
    
    public static void setPoolconfig(JedisPoolConfig config, Map<?, ?> properties) throws Exception {
        String value = null;
        value = (String) properties.get(PROP_MAXIDLE);
        if (value != null) {
        	config.setMaxIdle(Integer.valueOf(value));
        }
        
        value = (String) properties.get(PROP_MINIDLE);
        if (value != null) {
        	config.setMinIdle(Integer.valueOf(value));
        }
        
        value = (String) properties.get(PROP_MAXWAIT);
        if (value != null) {
        	config.setMaxWaitMillis(Long.valueOf(value));
        }

        value = (String) properties.get(PROP_MAXTOTAL);
        if (value != null) {
        	config.setMaxTotal(Integer.valueOf(value));
        }
        
        value = (String) properties.get(PROP_BLOCK_WHEN_EXHAUSTED);
        if (value != null) {
        	config.setBlockWhenExhausted(Boolean.valueOf(value));
        }
        
        value = (String) properties.get(PROP_LIFO);
        if (value != null) {
        	config.setLifo(Boolean.valueOf(value));
        }
        
        value = (String) properties.get(PROP_TEST_ON_BORROW);
        if (value != null) {
        	config.setTestOnBorrow(Boolean.valueOf(value));
        }
        
        value = (String) properties.get(PROP_TEST_WHILEIDLE);
        if (value != null) {
        	config.setTestWhileIdle(Boolean.valueOf(value));
        }
    }
    
}
