package com.johnny.batch.writer;

import java.time.LocalDate;

import javax.persistence.EntityManagerFactory;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.johnny.batch.domain.mysql.MysqlBoard;
import com.johnny.batch.domain.mysql.MysqlRepository;
import com.johnny.batch.domain.postgre.Board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaItemWriterJobConfiguration {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	@Qualifier("entityManagerFactory") // https://blog.naver.com/PostView.nhn?blogId=sksk3479&logNo=221178451242 참고
	private final EntityManagerFactory entityManagerFactoryPostgresql;
//	@Qualifier("entityManagerFactoryMysql")
//	private final EntityManagerFactory entityManagerFactoryMysql; // Repository를 만들어서 사용함
	
	private final MysqlRepository mysqlRepo;
	
	
	private static final int chunkSize = 10;
	
	@Bean
	public Job jpaItemWriterJob() {
		LocalDate today = LocalDate.now();
		log.info(">>>>> BatchJob Today() : " + today + "3");
		return jobBuilderFactory.get("worldspon" + today  + "3")
				.start(stepItemWriterStep())
				.build();
	}
	
	@Bean
	public Step stepItemWriterStep() {
		log.info(">>>>> Step");
		return stepBuilderFactory.get("stepItemWriterStep")
				.<Board, MysqlBoard>chunk(chunkSize)
				.reader(jpaItemWriterReader())
				.processor(jpaItemProcessor())
				.writer(customWriter())
				.build();
	}
	
	@Bean(destroyMethod="")
	public JpaPagingItemReader<Board> jpaItemWriterReader() {
		log.info(">>>>> Reader");
		return new JpaPagingItemReaderBuilder<Board>()
				.name("jpaItemWriterReader")
				.entityManagerFactory(entityManagerFactoryPostgresql)
				.pageSize(chunkSize)
				.queryString("SELECT p FROM Board p")
				.build();
	}
	
	@Bean
	public ItemProcessor<Board, MysqlBoard> jpaItemProcessor() {
		log.info(">>>>> Processor");
		String copyWriter = "CopyWriter";
		return board -> { 
			log.info(">>>>> board.getTitle() : " + board.getTitle());
			return new MysqlBoard(board.getTitle(), board.getContent(), copyWriter);
		};
	}
	
	private ItemWriter<MysqlBoard> customWriter() {
		log.info(">>>>> Writer");
		return myBoard -> {
			myBoard.forEach(item -> {
				log.info("myBoard.getTitle() : " + item.getTitle());
			});
			mysqlRepo.saveAll(myBoard);
		};
	}
	
//	JpaItemWriter를 사용할 때, 다중 DB를 사용하는 경우 
//	B Vendor사 DB로 설정한 EntityManager를 할당해도 Writing을 못함.
//	Writing을 시도하면 B Vendor사 Entity를 Entity로 인식하지 못함. 왤까???
//	@Bean
//	public JpaItemWriter<MysqlBoard> jpaItemWriter() {
//		JpaItemWriter<MysqlBoard> jpaItemWriter = new JpaItemWriter<>();
//		jpaItemWriter.setEntityManagerFactory(entityManagerFactoryMysql);
//		log.info(">>>>> jpaItemWriter.toString() : " + jpaItemWriter.toString());
//		return jpaItemWriter;
//	}
}
