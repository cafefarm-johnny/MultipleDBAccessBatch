package com.johnny.batch.domain.mysql;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@Table(name="MySQLBoard")
@Entity
public class MysqlBoard {

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	
	private String content;
	
	private String writer;
	
	public MysqlBoard(String title, String content, String writer) {
		this.title = title;
		this.content = content;
		this.writer = writer;
	}
}
