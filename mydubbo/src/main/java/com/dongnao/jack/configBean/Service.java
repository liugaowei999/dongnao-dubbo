package com.dongnao.jack.configBean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.dongnao.jack.registry.BaseRegistryDelegate;

public class Service extends BaseConfigBean implements
		InitializingBean, ApplicationContextAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = -669626644285470696L;
	private String inf;
	private String ref;
	private String protocol;

	private static ApplicationContext applicationContext;

	public Service() {
		System.out.println("Service 类实例构造!");
	}

	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		// TODO Auto-generated method stub
		Service.applicationContext = arg0;
	}

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("inf=" + inf + ", protocol=" + protocol + ", ref=" + ref + "-------------- 开始往注册中心进行服务注册");
		BaseRegistryDelegate.registry(ref, applicationContext);
	}

	public String getInf() {
		return inf;
	}

	public void setInf(String inf) {
		this.inf = inf;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
