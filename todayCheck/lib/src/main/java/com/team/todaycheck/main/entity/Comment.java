package com.team.todaycheck.main.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

	@Id
	@GeneratedValue
	private Long commentId;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id" , nullable = false)
	private Post post;

	@Column(nullable = false , length = 500)
	private String content;
	
	@Column(nullable = false , length = 45)
	private String writer;

	@JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "Asia/Seoul")
	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column(nullable = false)
	private Date date;
}
