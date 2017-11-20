package com.dongnao.jack.invoke;

import java.lang.reflect.Method;

import com.dongnao.jack.configBean.Reference;

/**
 * 封装了 通过反射方式来调用某个具体实例对象的具体服务方法，所需要的 相关参数
 * 
 * @author liugaowei
 *
 */
public class Invocation {

	private Method method;

	// 参数值
	private Object[] objects;

	private Reference reference;

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object[] getObjects() {
		return objects;
	}

	public void setObjects(Object[] objects) {
		this.objects = objects;
	}

	public Reference getReference() {
		return reference;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}

}
