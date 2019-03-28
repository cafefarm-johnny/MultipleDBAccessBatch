package com.johnny.batch.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@ToString
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Question {

	private static final DateTimeFormatter fm = DateTimeFormatter.ofPattern("yyyy-mm-dd");
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String answered;
	private LocalDate created;
	private String phoneType;
	private String q_content;
	private String title;
	private boolean used;
	
	public Question(Long id, String answered, String created, String phoneType, String q_content, String title, boolean used) {
		this.id = id;
		this.answered = answered;
		this.created = LocalDate.parse(created, fm);
		this.phoneType = phoneType;
		this.q_content = q_content;
		this.title = title;
		this.used = used;
	}
}
