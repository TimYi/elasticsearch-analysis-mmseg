package com.jiurong.search.plugin;

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.elasticsearch.plugin.analysis.mmseg.AnalysisMMsegPlugin;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Container {

	static {
		context = AccessController.doPrivileged(new PrivilegedAction<ApplicationContext>() {
			@Override
			public ApplicationContext run() {
				ApplicationContext c = new AnnotationConfigApplicationContext(AppConfig.class) {
					@Override
					public ClassLoader getClassLoader() {
						return AnalysisMMsegPlugin.class.getClassLoader();
					};
				};
				return c;
			}
		});
	}

	private static ApplicationContext context;

	public static <T> T getBean(Class<T> classT) {
		return AccessController.doPrivileged(new PrivilegedAction<T>() {
			@Override
			public T run() {
				return context.getBean(classT);
			}
		});
	}
}
