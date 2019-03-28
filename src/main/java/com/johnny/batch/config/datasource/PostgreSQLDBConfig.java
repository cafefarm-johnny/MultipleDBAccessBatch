package com.johnny.batch.config.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;

// http://roufid.com/spring-boot-multiple-databases-configuration/ 해당 주소 참고

@Slf4j
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages="com.johnny.batch.domain.postgre", entityManagerFactoryRef="entityManagerFactory", transactionManagerRef="transactionManager") // 레포지토리 위치
public class PostgreSQLDBConfig {
	private static final String DEFAULT_NAMING_STRATEGY = "org.springframework.boot.orm.jpa.hibernate.SpringNamingStrategy";
	
	// 1. 데이터 소스 정의
	@Bean
	@Primary // autowire 순서 처리. (해당 애플리케이션은 동일한 유형의 bean(mysql)이 2개이므로 우선순위를 지정해준다.)
	@ConfigurationProperties(prefix="spring.datasource.postgresql.hikari") // application.yml inner 위치 (데이터소스 속성 선택)
	public DataSource postgresqlDataSource() {
		log.info(">>>>> DataSource - Postgre");
		return DataSourceBuilder.create().build();
	}
	
	// 2. 엔터티 관리자 정의 (JPA Repository에서 사용될 JPA entityManagerFactory를 정의한다.)
	@Primary
	@Bean(name="entityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
		Map<String, Object> propertiesHashMap = new HashMap<>();
		propertiesHashMap.put("hibernate.ejb.naming_strategy", DEFAULT_NAMING_STRATEGY);
		log.info(">>>>> entityManagerFactory - Postgre");
		return builder.dataSource(this.postgresqlDataSource())
				.packages("com.johnny.batch.domain.postgre") // Entity 위치 
				.properties(propertiesHashMap)
				.build();
	}
	
	// 3. 트랜잭션 관리자 정의
	@Primary
	@Bean(name="transactionManager")
	public PlatformTransactionManager transactionManager(@Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
		log.info(">>>>> transactionManager - Postgre");
		return new JpaTransactionManager(entityManagerFactory);
	}
}
