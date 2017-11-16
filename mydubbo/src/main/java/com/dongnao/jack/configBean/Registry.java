package com.dongnao.jack.configBean;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.dongnao.jack.registry.BaseRegistry;
import com.dongnao.jack.registry.RedisRegistry;

public class Registry extends BaseConfigBean implements InitializingBean,
		ApplicationContextAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1026596990948181780L;
	private String protocol;
	private String address;

	private static ApplicationContext applicationContext;

	private static Map<String, BaseRegistry> registryMap = new HashMap<String, BaseRegistry>();

	static {
		registryMap.put("redis", new RedisRegistry());
	}

	public Registry() {
		System.out.println("Registry 类实例构造!");
	}

	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		// TODO Auto-generated method stub
		Registry.applicationContext = arg0;
	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public static Map<String, BaseRegistry> getRegistryMap() {
		return registryMap;
	}

	public static void setRegistryMap(Map<String, BaseRegistry> registryMap) {
		Registry.registryMap = registryMap;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
