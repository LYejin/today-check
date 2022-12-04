package com.team.todaycheck.main.DTO;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDTO {
	private String title;
	private String userId;
	private String description;
	private String thumbnail;
	@JsonFormat(shape = JsonFormat.Shape.STRING , pattern = "yyyy-MM-dd HH:mm:ss" , timezone = "Asia/Seoul")
	private Date date;
	private int postKey;
	private int views;
	private int recommendation;
	@Override
	public String toString() {
		return "PostDTO [title=" + title + ", userId=" + userId + ", description=" + description + ", thumbnail="
				+ thumbnail + ", date=" + date + ", postKey=" + postKey + "]";
	}
	
}
