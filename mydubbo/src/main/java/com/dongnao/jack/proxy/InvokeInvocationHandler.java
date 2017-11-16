package com.dongnao.jack.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.dongnao.jack.configBean.Reference;
import com.dongnao.jack.invoke.Invocation;
import com.dongnao.jack.invoke.Invoke;

/**
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
	Invocation invocation = new Invocation();
	invocation.setMethod(method);
	invocation.setObjects(args);
	invocation.setReference(reference);
	String Result = invoke.invoke(invocation);
	return Result;
    }

}
