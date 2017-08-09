package com.jiurong.search.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.AutoMappingBehavior;
import org.apache.ibatis.session.ExecutorType;
import org.elasticsearch.common.io.PathUtils;
import org.elasticsearch.plugin.analysis.mmseg.AnalysisMMsegPlugin;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration()
@ComponentScan(value = "com.jiurong", lazyInit = false)
@EnableTransactionManagement(proxyTargetClass = true)
@EnableScheduling
public class AppConfig {

	@Value("${url}")
	private String url;
	@Value("${username}")
	private String username;
	@Value("${password}")
	private String password;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		String appConfigPath = "app.properties";
		PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
		try {
			ClassPathResource classPathResource = new ClassPathResource(appConfigPath);
			if (classPathResource.exists()) {
				configurer.setLocation(classPathResource);
				return configurer;
			}
		} catch (Exception e) {
		}

		String path = PathUtils
		        .get(new File(AnalysisMMsegPlugin.class.getProtectionDomain().getCodeSource().getLocation().getPath())
		                .getParent(), appConfigPath)
		        .toAbsolutePath().toString();
		if (path.startsWith("/")) {
			path = "file:" + path;
		}
		try {
			UrlResource urlResource = new UrlResource(path);
			configurer.setLocation(urlResource);
			return configurer;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		Properties connectionProperties = new Properties();
		connectionProperties.put("useUnicode", "yes");
		connectionProperties.put("characterEncoding", "utf8");
		dataSource.setConnectionProperties(connectionProperties);
		return dataSource;
	}

	@Bean
	public SqlSessionFactoryBean sqlSessionFactory() {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource());
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setCacheEnabled(false);
		configuration.setUseGeneratedKeys(false);
		configuration.setDefaultExecutorType(ExecutorType.REUSE);
		configuration.setAutoMappingBehavior(AutoMappingBehavior.PARTIAL);
		configuration.setMapUnderscoreToCamelCase(true);
		sqlSessionFactoryBean.setConfiguration(configuration);
		Resource mapperLocation = new ClassPathResource("mybatis/mapper/**/*.xml");
		Resource[] mapperLocations = { mapperLocation };
		sqlSessionFactoryBean.setMapperLocations(mapperLocations);
		return sqlSessionFactoryBean;
	}

	@Bean
	public DataSourceTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}
}
