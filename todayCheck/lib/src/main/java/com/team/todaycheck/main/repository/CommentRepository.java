package com.team.todaycheck.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team.todaycheck.main.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment , Long> , CustomCommentRepository {
	public void deleteByCommentId(Long commentId);
}
