package com.johnny.batch.paging;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.johnny.batch.domain.postgre.Board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcPagingItemReaderJobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DataSource dataSource;
	
	private static final int chunkSize = 10;
	
	
	@Bean
	public Job jdbcPagingItemReaderJob() throws Exception {
		return jobBuilderFactory.get("jdbcPagingItemReaderJob")
				.start(jdbcPagingItemReaderStep())
				.build();
	}
	
	@Bean
	public Step jdbcPagingItemReaderStep() throws Exception {
		return stepBuilderFactory.get("jdbcPagingItemReaderStep")
				.<Board, Board>chunk(chunkSize)
				.reader(jdbcPagingItemReader())
				.writer(jdbcPagingItemWriter())
				.build();
	}
	
	
	@Bean(destroyMethod = "")
	public JdbcPagingItemReader<Board> jdbcPagingItemReader() throws Exception {
		Map<String, Object> parameterValue = new HashMap<>();
		parameterValue.put("id", 12);
		
		return new JdbcPagingItemReaderBuilder<Board>()
				.fetchSize(chunkSize)
				.dataSource(dataSource)
				.rowMapper(new BeanPropertyRowMapper<>(Board.class))
				.queryProvider(createQueryProvider())
				.parameterValues(parameterValue)
				.name("jdbcPagingItemReader")
				.build();
	}
	
	private ItemWriter<Board> jdbcPagingItemWriter() {
		return list -> {
			for (Board board : list) {
				log.info("Current Board = {}", board);
			}
		};
	}
	
	@Bean
	public PagingQueryProvider createQueryProvider() throws Exception {
		SqlPagingQueryProviderFactoryBean queryProvidor = new SqlPagingQueryProviderFactoryBean();
		queryProvidor.setDataSource(dataSource);
		queryProvidor.setSelectClause("id, title, content, writer");
		queryProvidor.setFromClause("FROM Board");
		queryProvidor.setWhereClause("WHERE id <= :id");
		
		// Order = org.springframework.batch.item.database.Order
		Map<String, Order> sortKeys = new HashMap<>(1);
		sortKeys.put("id", Order.DESCENDING);
		
		queryProvidor.setSortKeys(sortKeys);
		
		return queryProvidor.getObject();
	}
}
