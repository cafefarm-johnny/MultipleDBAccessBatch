package com.johnny.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor // 생성자 DI
@Configuration // Spring Batch의 모든 Job은 @Configuration으로 등록해서 사용한다.
public class SimpleJobConfiguration {
	
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	/*
	// Job은 하나의 배치 작업 단위를 뜻한다.
	// * tasklet 하나가 Reader & Processor & Writer 한 묶음과 같은 레벨이다.
	// * 그래서 Reader & Processor가 끝나고 tasklet으로 마무리 짓는 등으로 만들 수 없다.
	// * tasklet은 Spring MVC에서 @Component와 @Bean과 비슷한 역할을 한다.
	
	@Bean // 1. simpleJob이라는 이름의 Batch Job을 생성한다.
	public Job simpleJob() {
		return jobBuilderFactory.get("simpleJob").start(simpleStep1()).build(); // 2. job의 이름은 별도로 지정하지 않고, Builder를 통해 지정한다.
	}
	
	
	@Bean // 1. simpleStep1 이라는 이름의 Batch Step을 생성한다.
	public Step simpleStep1() {
		return stepBuilderFactory.get("simpleStep1").tasklet((contribution, chunkContext) -> { // 3. Step 안에서 수행될 기능들을 명시한다. tasklet은 Step안에서 단일로 수행될 커스텀한 기능들을 선언할 때 사용한다.
			log.info("======== This is step1");
			log.info("======== contribution : " + contribution.toString());
			log.info("======== chunkContext : " + chunkContext.toString());
			return RepeatStatus.FINISHED;
		}).build(); // 2. Builder를 통해 이름을 지정한다.
	}
	*/
	
	
//	@Bean
//	public Job simpleJob() {
//		return jobBuilderFactory.get("simpleJob").start(simpleStep1(null)).build();
//	}
//	
//	@Bean
//	@JobScope
//	public Step simpleStep1(@Value("#{jobParameters[requestDate]}") String requestDate) {
//		return stepBuilderFactory.get("simpleStep1").tasklet((contribution, chunkContext) -> {
//			log.info("======== This is step1");
//			log.info("======== requestDate = {}", requestDate);
//			return RepeatStatus.FINISHED;
//		}).build();
//	}
	
	@Bean
	public Job simpleJob() {
		return jobBuilderFactory.get("simpleJob").start( simpleStep1(null) ).next( simpleStep2(null) ).build();
	}
	
	@Bean
	@JobScope
	public Step simpleStep1(@Value("#{jobParameters[requestDate]}") String requestDate) {
		return stepBuilderFactory.get("simpleStep1").tasklet( (contribution, chunkContext) -> {
			throw new IllegalArgumentException("step1에서 실패한다.");
		} ).build();
	}
	
	@Bean
	@JobScope
	public Step simpleStep2(@Value("#{jobParameters[requestDate]}") String requestDate) {
		return stepBuilderFactory.get("simpleStep2").tasklet( (contribution, chunkContext) -> {
			log.info("======= This is Step2");
			log.info("======= requestDate = {}", requestDate);
			return RepeatStatus.FINISHED;
		} ).build();
	}
}
