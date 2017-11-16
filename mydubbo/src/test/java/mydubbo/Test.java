package mydubbo;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("mytest.xml");
		// TestService testService =
		// applicationContext.getBean(TestService.class);
		// TestService testService = (TestService)
		// applicationContext.getBean("testServiceImpl1");
		// testService.eat("aaaa");
	}
}
