package com.johnny.batch.writer;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.johnny.batch.domain.postgre.Board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcBatchItemWriterJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DataSource dataSource;
	
	private static final int chunkSize = 10;
	
	@Bean
	public Job jdbcBatchItemWriterJob() {
		return jobBuilderFactory.get("jdbcBatchItemWriterJob")
				.start(jdbcBatchItemWriterStep())
				.build();
	}
	
	@Bean
	public Step jdbcBatchItemWriterStep() {
		return stepBuilderFactory.get("jdbcBatchItemWriterStep")
				.<Board, Board>chunk(chunkSize)
				.reader(jdbcBatchItemWriterReader())
				.writer(jdbcBatchItemWriter())
				.build();
	}
	
	@Bean(destroyMethod = "")
	public JdbcCursorItemReader<Board> jdbcBatchItemWriterReader() {
		return new JdbcCursorItemReaderBuilder<Board>()
				.fetchSize(chunkSize)
				.dataSource(dataSource)
				.rowMapper(new BeanPropertyRowMapper<Board>(Board.class))
				.sql("SELECT id, title, content, writer FROM Board")
				.name("jdbcBatchItemWriter")
				.build();
	}
	
	// reader에서 넘어온 데이터를 하나씩 출력하는 Writer
	@Bean // beanMapped()를 사용할때는 반드시 Bean으로 등록해야 한다.
	public JdbcBatchItemWriter<Board> jdbcBatchItemWriter() {
		return new JdbcBatchItemWriterBuilder<Board>()
				.dataSource(dataSource)
				.sql("INSERT INTO OtherBoard (title, content, writer) VALUES (:title, :content, :writer)")
				.beanMapped()
				.build();
	}
	
}
