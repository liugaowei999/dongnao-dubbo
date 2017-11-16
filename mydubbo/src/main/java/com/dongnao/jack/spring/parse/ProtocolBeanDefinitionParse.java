package com.dongnao.jack.spring.parse;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @Description protocal 标签的解析
 * @ClassName ProtocolBeanDefinitionParse
 * @author liugaowei
 *
 */
public class ProtocolBeanDefinitionParse implements
		BeanDefinitionParser {

	private Class<?> beanClass;

	public ProtocolBeanDefinitionParse(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		// TODO Auto-generated method stub
		/*
		 * Root bean definitions may also be used for registering individual
		 * bean definitions in the configuration phase. However, since Spring
		 * 2.5, the preferred way to register bean definitions 通过配置项 自定义bean
		 */
		RootBeanDefinition beanDefinition = new RootBeanDefinition();

		// spring 对beanClass进行实例化
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);

		// 从mytest.xml xml标签中根据属性名称获取定义的属性值
		String name = element.getAttribute("name");
		String host = element.getAttribute("host");
		String port = element.getAttribute("port");

		System.out.println("ProtocolBeanDefinitionParse name=" + name + ", host=" + host + ", port=" + port);

		if (name == null || "".equals(name)) {
			throw new RuntimeException("<protocol> name不能为空！");
		}
		if (host == null || "".equals(host)) {
			throw new RuntimeException("<protocol> host不能为空！");
		}
		if (port == null || "".equals(port)) {
			throw new RuntimeException("<protocol> port不能为空！");
		}

		// 通过反射进行对象属性的初始化
		beanDefinition.getPropertyValues().addPropertyValue("name", name);
		beanDefinition.getPropertyValues().addPropertyValue("host", host);
		beanDefinition.getPropertyValues().addPropertyValue("port", port);

		// 在Spring容器中进行注册， 将bean实例放入容器中。
		parserContext.getRegistry().registerBeanDefinition("protocol_" + name + host + port, beanDefinition);
		return beanDefinition;
	}
}
