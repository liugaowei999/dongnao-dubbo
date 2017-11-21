package com.dongnao.jack.rmi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dongnao.jack.configBean.Service;

/**
 * 服务提供者 RMI服务。接收到RMI消费端调用的远程服务参数信息，调用服务端的 服务， 并返回服务执行结果给消费者
 * 
 * @author liugaowei
 *
 */
public class SoaRmiImpl extends UnicastRemoteObject implements SoaRmi {

	public SoaRmiImpl() throws RemoteException {
		super();
	}

	public String invoke(String param) throws RemoteException {
		JSONObject requestparam = JSONObject.parseObject(param);

		// 获取远程消费者发送来的远程调用参数信息
		// serviceid
		String serviceId = requestparam.getString("serviceId");
		String methodName = requestparam.getString("methodName");
		JSONArray paramTypes = requestparam.getJSONArray("paramTypes");
		JSONArray methodParamJa = requestparam.getJSONArray("methodParams");
		System.out.println("【RMI】Server 收到服务调用参数 serviceId=" + serviceId + ", methodName=" + methodName);

		Object[] objects = null;
		if (methodParamJa != null) {
			objects = new Object[methodParamJa.size()];
			int i = 0;
			for (Object object : methodParamJa) {
				objects[i++] = object;
			}
		}

		ApplicationContext applicationContext = Service.getApplicationContext();
		Object serviceBean = applicationContext.getBean(serviceId);
		Method method = null;
		try {
			method = getMethod(serviceBean, methodName, paramTypes);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if (method != null) {
			Object result;
			try {
				result = method.invoke(serviceBean, objects);
				System.out.println("RMI Server result=" + result);
				return (String) (result instanceof String ? result : result.toString());
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return "---------- No this method -----------";
		}

		return null;
	}

	private Method getMethod(Object serviceBean, String methodName, JSONArray methodParamTypes)
			throws ClassNotFoundException, NoSuchMethodException, SecurityException {

		// 先根据名称找， 如果按名称只有一个函数， 则默认是此方法
		// 这种方式效率高， 但并不严谨， 有可能，客户端传错参数，导致执行失败，异常。
		Method[] methods = serviceBean.getClass().getMethods();
		List<Method> retMethod = new ArrayList<Method>();
		for (Method method : methods) {
			if (methodName.trim().equals(method.getName())) {
				retMethod.add(method);
			}
		}
		if (retMethod.size() == 1) {
			return retMethod.get(0);
		}
		// 完全匹配查询
		Class<?>[] paraTypeClass = new Class<?>[methodParamTypes.size()];
		for (int i = 0; i < methodParamTypes.size(); i++) {
			String typeName = methodParamTypes.getString(i);
			paraTypeClass[i] = Class.forName(typeName);
		}
		return serviceBean.getClass().getMethod(methodName, paraTypeClass);
	}

	private Method getMethod1(Object bean, String methodName,
			JSONArray paramTypes) {

		Method[] methods = bean.getClass().getMethods();
		List<Method> retMethod = new ArrayList<Method>();

		for (Method method : methods) {
			// 把名字和methodName入参相同的方法加入到list中来
			if (methodName.trim().equals(method.getName())) {
				retMethod.add(method);
			}
		}

		// 如果大小是1就说明相同的方法只有一个
		if (retMethod.size() == 1) {
			return retMethod.get(0);
		}

		boolean isSameSize = false;
		boolean isSameType = false;
		jack: for (Method method : retMethod) {
			Class<?>[] types = method.getParameterTypes();

			if (types.length == paramTypes.size()) {
				isSameSize = true;
			}

			if (!isSameSize) {
				continue;
			}

			for (int i = 0; i < types.length; i++) {
				if (types[i].toString().contains(paramTypes.getString(i))) {
					isSameType = true;
				} else {
					isSameType = false;
				}
				if (!isSameType) {
					continue jack;
				}
			}

			if (isSameType) {
				return method;
			}
		}
		return null;
	}

}
