package com.dongnao.jack.invoke;

import java.lang.reflect.Method;

import com.dongnao.jack.configBean.Reference;

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
