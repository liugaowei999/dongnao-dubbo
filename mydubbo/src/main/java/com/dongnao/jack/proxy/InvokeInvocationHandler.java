package com.dongnao.jack.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.dongnao.jack.configBean.Reference;
import com.dongnao.jack.invoke.Invocation;
import com.dongnao.jack.invoke.Invoke;

/**
 * 代理对象执行方法的的统一处理中心。 不管何种对象，调用何种方法统一
 * 
 * 由此类来统一处理 从而实现服务调用的聚集， 与具体对象解耦，
 * 
 * 达到对象无关性。
 * 
 * @Description InvokeInvocationHandler , 这里实现rpc的远程调用
 * 
 *              rpc: http, rmi, netty
 * 
 * @author liugaowei
 *
 */
public class InvokeInvocationHandler implements InvocationHandler {

	private Invoke invoke;

	private Reference reference;

	public InvokeInvocationHandler(Invoke invoke, Reference reference) {
		this.invoke = invoke;
		this.reference = reference;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		System.out.println("已经生成了代理实例，开始执行：InvokeInvocationHandler.invoke()");
		// System.out.println("proxy=" + proxy); // 打印语句会导致进入死循环 导致堆栈溢出，
		// 调用代理对象的方法toString，会触发再次进入invoke又触发，形成无限递归调用
		Invocation invocation = new Invocation();
		invocation.setMethod(method);
		invocation.setObjects(args);
		invocation.setReference(reference);
		String Result = invoke.invoke(invocation);
		return Result;
	}

}
