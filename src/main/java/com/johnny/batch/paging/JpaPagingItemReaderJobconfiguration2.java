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

import com.johnny.batch.domain.postgre.Board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPagingItemReaderJobconfiguration2 {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final EntityManagerFactory entityManagerFactory;
	
	private int chunkSize = 10;
	
	@Bean
	public Job jpaPagingItemReaderJob2() {
		return jobBuilderFactory.get("jpaPagingItemReaderJob2")
				.start(jpaPagingItemReaderStep2())
				.build();
	}
	
	@Bean
	public Step jpaPagingItemReaderStep2() {
		return stepBuilderFactory.get("jpaPagingItemReaderStep2")
				.<Board, Board>chunk(chunkSize)
				.reader(jpaPagingItemReader2())
				.writer(jpaPagingItemWriter())
				.build();
	}
	
	@Bean(destroyMethod = "")
	public JpaPagingItemReader<Board> jpaPagingItemReader2() {
		return new JpaPagingItemReaderBuilder<Board>()
				.name("jpaPagingItemReader2")
				.entityManagerFactory(entityManagerFactory)
				.pageSize(chunkSize)
				.queryString("SELECT p FROM Board p WHERE id <= 12 ORDER BY id DESC")
				.build();
	}
	
	private ItemWriter<Board> jpaPagingItemWriter() {
		return list -> {
			for (Board board : list)
			{
				log.info("Current Board = {}", board);
			}
		};
	}
}
