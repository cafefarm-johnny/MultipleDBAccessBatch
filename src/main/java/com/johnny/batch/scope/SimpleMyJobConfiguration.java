package com.johnny.batch.scope;

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
@RequiredArgsConstructor
@Configuration
public class SimpleMyJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public Job simpleMyJob() {
		log.info(">>>>> definition simpleMyJob");
		return jobBuilderFactory.get("simpleMyJob")
				.start(simpleMyStep1())
				.next(simpleMyStep2(null))
				.build();
	}
	
	private final SimpleJobTasklet tasklet1;
	
//	@Bean
//	@JobScope
	public Step simpleMyStep1() {
		log.info(">>>>> definition simpleMyStep1");
		return stepBuilderFactory.get("simpleMyStep1")
				.tasklet(tasklet1)
				.build();
	}
	
	@Bean
	@JobScope
	public Step simpleMyStep2(@Value("#{jobParameters[requestDate]}") String requestDate) {
		return stepBuilderFactory.get("simpleMyStep2").tasklet( (contribution, chunkContext) -> {
			log.info("======= This is Step2");
			log.info("======= requestDate = {}", requestDate);
			return RepeatStatus.FINISHED;
		} ).build();
	}
}
