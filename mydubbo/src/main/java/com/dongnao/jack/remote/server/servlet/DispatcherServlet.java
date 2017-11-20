package com.dongnao.jack.remote.server.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dongnao.jack.configBean.Service;

public class DispatcherServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8988458607525496425L;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
		System.out.println("com.dongnao.jack.remote.server.servlet.DispatcherServlet init()");
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("com.dongnao.jack.remote.server.servlet.DispatcherServlet service()");
		System.out.println("Request Method:" + req.getMethod());
		if (req.getMethod().equals("GET")) {
			doGet(req, resp);
		} else if (req.getMethod().equals("POST")) {
			doPost(req, resp);
		} else {
			throw new ServletException("Cannot support this Method :[" + req.getMethod() + "]");
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("do get .......");
		try {
			doHttpRequest(req, resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("do post .......");
		try {
			doHttpRequest(req, resp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doHttpRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, ClassNotFoundException, NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		JSONObject requestParam = getParam(req, resp);
		if (requestParam == null) {
			throw new ServletException("服务端获取服务调用请求参数信息失败！");
		}
		// 获取远程客户端要调用的服务ID --- serviceId
		String serviceId = requestParam.getString("serviceId");
		// 获取远程客户端要调用的服务的具体方法信息 ---- methodName
		String methodName = requestParam.getString("methodName");
		// 获取要调用方法的参数类型数组（方法可能多个参数） --- 解决方法重载的问题
		JSONArray methodParamTypes = requestParam.getJSONArray("paramTypes");
		// 获取要调用方法的具体参数值数组
		JSONArray methodParamValues = requestParam.getJSONArray("methodParams");

		// 构建用于的反射的参数值 --- 为Object数组类型
		Object[] methodParamValueObjs = null;
		if (methodParamValues != null) {
			methodParamValueObjs = new Object[methodParamValues.size()];
			int i = 0;
			for (Object object : methodParamValues) {
				methodParamValueObjs[i++] = object;
			}
		}

		// 获取Spring容器上下文
		ApplicationContext applicationContext = Service.getApplicationContext();
		// 通过Spring上下文从容器中获取服务的实例
		Object serviceBean = applicationContext.getBean(serviceId);
		// 获取服务实例的方法 --- 用于反射invoke执行
		Method method = getMethod(serviceBean, methodName, methodParamTypes);
		PrintWriter pw = resp.getWriter();
		if (method != null) {
			Object result = method.invoke(serviceBean, methodParamValueObjs);
			pw.write(result.toString());
		} else {
			pw.write("The service:" + serviceId + ", has no such method: methodName=" + methodName + ", param:"
					+ methodParamValues);
		}
		pw.flush();
		pw.close();
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

	private JSONObject getParam(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		StringBuffer stringBuffer = new StringBuffer();
		InputStream is = req.getInputStream();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "utf-8"));

		String string = "";
		while ((string = bufferedReader.readLine()) != null) {
			stringBuffer.append(string);
		}
		if (stringBuffer.length() <= 0) {
			return null;
		} else {
			return JSONObject.parseObject(stringBuffer.toString());
		}
	}
}
