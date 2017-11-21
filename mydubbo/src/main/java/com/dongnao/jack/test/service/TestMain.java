package com.dongnao.jack.test.service;

public class TestMain {

	public static String getStr() {
		String ssss = "afdsafd";
		return ssss;
	}

	public static void main(String[] args) {
		// Null point exception:
		// Exception in thread "main" java.lang.NullPointerException
		// at com.dongnao.jack.test.service.TestMain.main(TestMain.java:5)
		try {
			String classLoaderName = Thread.currentThread().getClass().getClassLoader().getClass().getName();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 上下文类加载器
		String classLoaderName1 = Thread.currentThread().getContextClassLoader().toString();
		System.out.println("run:Reference --- getObject!, classLoaderName=[" + classLoaderName1 + "]");

		Object result;
		try {
			result = getStr();
			System.out.println(result instanceof String ? result.toString() : result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
