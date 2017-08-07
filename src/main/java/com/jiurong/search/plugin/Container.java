package com.jiurong.search.plugin;

import java.lang.reflect.ReflectPermission;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Container {

	static {
		SecurityManager sm = System.getSecurityManager();
		sm.checkPermission(new ReflectPermission("suppressAccessChecks"));
	}

	private static ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

	public static <T> T getBean(Class<T> classT) {
		return context.getBean(classT);
	}
}
