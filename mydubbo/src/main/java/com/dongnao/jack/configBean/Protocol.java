package com.dongnao.jack.configBean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.dongnao.jack.rmi.RmiUtil;

public class Protocol extends BaseConfigBean implements InitializingBean, ApplicationContextAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3747830716340768610L;
	private String name;
	private String host;
	private String port;

	private String contextpath;

	public String getContextpath() {
		return contextpath;
	}

	public void setContextpath(String contextpath) {
		this.contextpath = contextpath;
	}

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

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	// implements InitializingBean
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		if (name.equalsIgnoreCase("rmi")) {
			RmiUtil rmiUtil = new RmiUtil();
			rmiUtil.startRmiServer(host, port, "SOA_RMI_SERVICE"); // 写死为固定字符串，与客户端调用的内容要一致
		}
	}

	// implements ApplicationContextAware
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		// TODO Auto-generated method stub
		Protocol.applicationContext = arg0;
	}

}
