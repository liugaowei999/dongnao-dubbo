package com.dongnao.jack.spring.parse;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ServiceBeanDefinitionParse implements BeanDefinitionParser {

	private Class<?> beanClass;

	public ServiceBeanDefinitionParse(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		// TODO Auto-generated method stub
		RootBeanDefinition beanDefinition = new RootBeanDefinition();
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);

		// 获取配置文件中的参数配置
		String intf = element.getAttribute("interface");
		String ref = element.getAttribute("ref");
		String protocol = element.getAttribute("protocol");

		if (intf == null || "".equals(intf)) {
			throw new RuntimeException("<Service> interface 不能为空！");
		}
		if (ref == null || "".equals(ref)) {
			throw new RuntimeException("<Service> ref 不能为空！");
		}
		if (protocol == null || "".equals(protocol)) {
			throw new RuntimeException("<Service> protocol 不能为空！");
		}

		beanDefinition.getPropertyValues().addPropertyValue("inf", intf);
		beanDefinition.getPropertyValues().addPropertyValue("ref", ref);
		beanDefinition.getPropertyValues().addPropertyValue("protocol", protocol);

		// 注册
		parserContext.getRegistry().registerBeanDefinition("Service_" + intf, beanDefinition);
		return beanDefinition;
	}

}
