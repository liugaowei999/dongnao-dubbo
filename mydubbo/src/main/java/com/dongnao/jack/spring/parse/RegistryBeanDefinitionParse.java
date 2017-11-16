package com.dongnao.jack.spring.parse;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class RegistryBeanDefinitionParse implements BeanDefinitionParser {

	private Class<?> beanClass;

	public RegistryBeanDefinitionParse(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		// TODO Auto-generated method stub
		RootBeanDefinition beanDefinition = new RootBeanDefinition();

		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);

		// 获取配置文件的参数值
		String protocol = element.getAttribute("protocol");
		String address = element.getAttribute("address");

		if (protocol == null || "".equals(protocol)) {
			throw new RuntimeException("<Registry> protocol 不能为空");
		}
		if (address == null || "".equals(address)) {
			throw new RuntimeException("<Registry> address 不能为空");
		}

		beanDefinition.getPropertyValues().addPropertyValue("protocol", protocol);
		beanDefinition.getPropertyValues().addPropertyValue("address", address);

		// 注册
		parserContext.getRegistry().registerBeanDefinition("Registry" + protocol, beanDefinition);
		return beanDefinition;
	}

}
