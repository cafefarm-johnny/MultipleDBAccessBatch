package com.johnny.batch.paging;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.johnny.batch.domain.Question;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPagingItemReaderJobConfiguration {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;
	
	private int chunkSize = 10;
	
	
	@Bean
	public Job jpaPagingItemReaderJob() {
		return jobBuilderFactory.get("jpaPagingItemReaderJob")
				.start(jpaPagingItemReaderStep())
				.build();
	}

	@Bean
	public Step jpaPagingItemReaderStep() {
		return stepBuilderFactory.get("jpaPagingItemReaderStep")
				.<Question, Question>chunk(chunkSize)
				.reader(jpaPagingItemReader())
				.writer(jpaPagingItemWriter())
				.build();
	}

	@Bean(destroyMethod="")
	public JpaPagingItemReader<Question> jpaPagingItemReader() {
		return new JpaPagingItemReaderBuilder<Question>()
				.name("jpaPagingItemReader")
				.entityManagerFactory(entityManagerFactory)
				.pageSize(chunkSize)
				.queryString("SELECT p FROM Question p WHERE used = true ORDER BY id DESC")
				.build();
	}
	
	private ItemWriter<Question> jpaPagingItemWriter() {
		return list -> {
			for (Question q : list) {
				log.info("Current Question = {}", q);
			}
		};
	}
}
