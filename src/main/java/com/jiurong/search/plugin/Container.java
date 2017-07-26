package com.jiurong.search.plugin;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Container {

	private static ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

	public static <T> T getBean(Class<T> classT) {
		return context.getBean(classT);
	}
}
