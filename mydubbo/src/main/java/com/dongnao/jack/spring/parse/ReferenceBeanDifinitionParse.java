package com.dongnao.jack.spring.parse;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class ReferenceBeanDifinitionParse implements
		BeanDefinitionParser {

	private Class<?> beanClass;

	public ReferenceBeanDifinitionParse(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	public BeanDefinition parse(Element element, ParserContext parserContext) {
		// TODO Auto-generated method stub
		RootBeanDefinition beanDefinition = new RootBeanDefinition();

		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setLazyInit(false);

		String id = element.getAttribute("id");
		String intf = element.getAttribute("interface");
		String protocol = element.getAttribute("protocol");
		String loadBalance = element.getAttribute("loadbalance");

		if (id == null && "".equals(id)) {
			throw new RuntimeException("<Reference> id 不能为空");
		}
		if (intf == null && "".equals(intf)) {
			throw new RuntimeException("<Reference> intf 不能为空");
		}
		if (protocol == null && "".equals(protocol)) {
			throw new RuntimeException("<Reference> protocol 不能为空");
		}
		if (loadBalance == null && "".equals(loadBalance)) {
			throw new RuntimeException("<Reference> loadBalance 不能为空");
		}
		beanDefinition.getPropertyValues().addPropertyValue("id", id);
		beanDefinition.getPropertyValues().addPropertyValue("intf", intf);
		beanDefinition.getPropertyValues().addPropertyValue("protocol", protocol);
		beanDefinition.getPropertyValues().addPropertyValue("loadbalance", loadBalance);

		// 注册
		parserContext.getRegistry().registerBeanDefinition("Reference_" + id + intf, beanDefinition);
		return beanDefinition;
	}

}
