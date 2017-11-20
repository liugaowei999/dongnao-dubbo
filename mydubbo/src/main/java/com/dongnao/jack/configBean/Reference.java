package com.dongnao.jack.configBean;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.dongnao.jack.invoke.HttpInvoke;
import com.dongnao.jack.invoke.Invoke;
import com.dongnao.jack.invoke.RmiInvoke;
import com.dongnao.jack.loadbalance.LoadBalance;
import com.dongnao.jack.loadbalance.RandomLoadBalance;
import com.dongnao.jack.loadbalance.RoundRobinLoadBalance;
import com.dongnao.jack.proxy.InvokeInvocationHandler;
import com.dongnao.jack.registry.BaseRegistryDelegate;

public class Reference extends BaseConfigBean implements FactoryBean, InitializingBean, ApplicationContextAware {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6069258948365509991L;

	private String intf;

	private String loadbalance;

	private String protocol;

	private static ApplicationContext applicationContext;

	private Invoke invoke;

	private LoadBalance loadBalanceBean;

	private static Map<String, Invoke> invokes = new HashMap<String, Invoke>();

	private static Map<String, LoadBalance> loadbanlanceMap = new HashMap<String, LoadBalance>();

	public static Map<String, LoadBalance> getLoadbanlanceMap() {
		return loadbanlanceMap;
	}

	public static void setLoadbanlanceMap(Map<String, LoadBalance> loadbanlanceMap) {
		Reference.loadbanlanceMap = loadbanlanceMap;
	}

	public LoadBalance getLoadBalanceBean() {
		return loadBalanceBean;
	}

	// 服务提供者 提供服务的列表
	private List<String> registryInfo = new ArrayList<String>();

	static {
		invokes.put("http", new HttpInvoke());
		invokes.put("rmi", new RmiInvoke());

		loadbanlanceMap.put("random", new RandomLoadBalance());
		loadbanlanceMap.put("roundrob", new RoundRobinLoadBalance());
	}

	public List<String> getRegistryInfo() {
		return registryInfo;
	}

	public void setRegistryInfo(List<String> registryInfo) {
		this.registryInfo = registryInfo;
	}

	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		// TODO Auto-generated method stub
		Reference.applicationContext = arg0;
	}

	public Reference() {
		System.out.println("Reference 类实例构造!");
	}

	public String getIntf() {
		return intf;
	}

	public void setIntf(String intf) {
		this.intf = intf;
	}

	public String getLoadbalance() {
		return loadbalance;
	}

	public void setLoadbalance(String loadbalance) {
		this.loadbalance = loadbalance;
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

	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("afterPropertiesSet");
		registryInfo = BaseRegistryDelegate.getRegistry(id, applicationContext);
		System.out.println("从注册中心获取服务信息：" + registryInfo);

		// 取客户端该服务调用的负载均衡算法配置策略选择信息,设置负载均衡策略
		loadBalanceBean = Reference.loadbanlanceMap.get(loadbalance);
	}

	// implements FactoryBean
	/**
	 * 通过Reference标签注册的服务， 通过FactoryBean -->getObject接口为容器提供 具体的实例对象。
	 * 实例对象时通过动态代理方式生产的代理对象， 从而实现了与 具体对象的解耦， 通过统一的Handler方法
	 * 通过不同的rpc通信协议，发起向服务提供者的远程服务调用。
	 * 
	 * getObject() 只会被容器调用一次。 生产代理对象放入容器。
	 */
	public Object getObject() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("run:Reference --- getObject!");
		if (protocol != null && (!"".equals(protocol))) {
			invoke = invokes.get(protocol);
		} else {
			// 默认使用Http协议
			invoke = invokes.get("http");
		}
		// 生成代理对象
		Object proxyObj = Proxy.newProxyInstance(this.getClass().getClassLoader(),
				new Class<?>[] { Class.forName(intf) },
				new InvokeInvocationHandler(invoke, this));
		System.out.println("proxyObj=" + proxyObj);
		return proxyObj;
	}

	// implements FactoryBean
	public Class getObjectType() {
		// TODO Auto-generated method stub
		System.out.println("Reference.java  Class getObjectType()");
		if (intf != null && !"".equals(intf)) {
			try {
				return Class.forName(intf);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	// implements FactoryBean
	public boolean isSingleton() {
		// TODO Auto-generated method stub
		return false;
	}

}
