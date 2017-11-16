package com.dongnao.jack.registry;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.dongnao.jack.configBean.Registry;

public class BaseRegistryDelegate {

	public static void registry(String ref, ApplicationContext applicationContext) {
		Registry registry = applicationContext.getBean(Registry.class);
		String protocol = registry.getProtocol();
		System.out.println("注册中心类型：" + protocol);
		BaseRegistry registryBean = Registry.getRegistryMap().get(protocol);
		registryBean.registry(ref, applicationContext);
	}

	public static List<String> getRegistry(String serviceId, ApplicationContext applicationContext) {
		Registry registry = applicationContext.getBean(Registry.class);
		String protocol = registry.getProtocol();
		System.out.println("注册中心类型：" + protocol);
		BaseRegistry registryBean = Registry.getRegistryMap().get(protocol);
		return registryBean.getRegistry(serviceId, applicationContext);
	}
}
