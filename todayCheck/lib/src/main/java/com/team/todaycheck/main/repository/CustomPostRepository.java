package com.team.todaycheck.main.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.team.todaycheck.main.DTO.PostDTO;
import com.team.todaycheck.main.entity.Post;

public interface CustomPostRepository {
	public void updateView(int postnumber);
	public boolean increaseRecommander(int postNumber , String userId);
	public List<PostDTO> getAllPost(Pageable pageable);
	public Long deleteOnePost(int post_key , String userId);
	public Post findByPostKey(int post_key , String userId);
	int getPostKeyMaxValue();
}
