package com.johnny.batch.config.datasource;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages="com.johnny.batch.domain.mysql", entityManagerFactoryRef="entityManagerFactoryMysql", transactionManagerRef="transactionManagerMysql")
public class MySQLDBConfig {
	
	// 1. 데이터 소스 정의
	@Bean
	@ConfigurationProperties(prefix="spring.datasource.mysql.hikari") // application.yml inner 위치 (데이터소스 속성 선택)
	public DataSource mysqlDataSource() {
		log.info(">>>>> DataSource - MySQL");
		return DataSourceBuilder.create().build();
	}
	
	// 2. 엔터티 관리자 정의 (JPA Repository에서 사용될 JPA entityManagerFactory를 정의한다.)
	@Bean(name="entityManagerFactoryMysql")
	public LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		log.info(">>>>> mysqlEntityManagerFactory - MySQL");
		return builder.dataSource(this.mysqlDataSource())
				.packages("com.johnny.batch.domain.mysql")
				.build();
	}

	// 3. 트랜잭션 관리자 정의
	@Bean(name="transactionManagerMysql")
	public PlatformTransactionManager mysqlTransactionManager(@Qualifier("entityManagerFactoryMysql") EntityManagerFactory entityManagerFactory) {
		log.info(">>>>> mysqlTransactionManager - MySQL");
		return new JpaTransactionManager(entityManagerFactory);
	}
}
