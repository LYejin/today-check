package com.team.todaycheck.main.repository;

public interface CustomCommentRepository {
	public Long deleteComment(Long commentId , String userId);
	public Long getCommentIdKeyMaxValue();
}