package com.dongnao.jack.configBean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class Protocol extends BaseConfigBean implements InitializingBean, ApplicationContextAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3747830716340768610L;
	private String name;
	private String host;
	private String port;

	private String contextpath;

	private static ApplicationContext applicationContext;

	public Protocol() {
		System.out.println("Protocol 类实例构造!");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getContextPath() {
		return contextpath;
	}

	public void setContextPath(String contextpath) {
		this.contextpath = contextpath;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	// implements InitializingBean
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	// implements ApplicationContextAware
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		// TODO Auto-generated method stub
		Protocol.applicationContext = arg0;
	}

}
