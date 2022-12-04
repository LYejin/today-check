package com.team.todaycheck.main.repository.Impl;

import static com.team.todaycheck.main.entity.QComment.comment;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQueryFactory;
// import com.querydsl.jpa.impl.JPAQueryFactory;
import com.team.todaycheck.main.entity.Comment;
import com.team.todaycheck.main.repository.CustomCommentRepository;

public class CustomCommentRepositoryImpl extends QuerydslRepositorySupport implements CustomCommentRepository {

	private JPAQueryFactory queryFactory;
	
	public CustomCommentRepositoryImpl(JPAQueryFactory queryFactory) {
		super(Comment.class);
		this.queryFactory = queryFactory;
	}

	@Override
	public Long deleteComment(Long commentId, String userId) {
		JPADeleteClause delete = new JPADeleteClause(getEntityManager(), comment);
		
		return delete.where(comment.commentId.eq(commentId).and(comment.writer.eq(userId))).execute();
	}
	
	/*
	 *  CommentId 에서 가장 큰 commentId 키값을 반환합니다.
	 */
	@Override
	public Long getCommentIdKeyMaxValue() {
		return queryFactory.select(comment.commentId.max()).from(comment).fetchOne();
	}
}
