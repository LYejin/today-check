package com.team.todaycheck.main.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.team.todaycheck.main.DTO.CommentDTO;
import com.team.todaycheck.main.DTO.MessageDTO;
import com.team.todaycheck.main.DTO.PostDTO;
import com.team.todaycheck.main.entity.Post;
import com.team.todaycheck.main.service.PostService;

@RestController
@RequestMapping("/post")
public class PostController {

	@Autowired PostService postService;
	
	@RequestMapping(value = "/post" , method = RequestMethod.POST)
	public MessageDTO addPost(@RequestBody PostDTO postData , HttpServletRequest request) throws UnsupportedEncodingException {
		String header = request.getHeader("Authorization");
		
		postService.addPost(postData , header);
		return MessageDTO.builder()
				.code("1")
				.message("게시글이 성공적으로 게시되었습니다.")
				.build();
	}
	
	// /wholePost?page=0&size=20&sort=postKey,desc
	@RequestMapping(value = "/wholePost" , method = RequestMethod.GET)
	public List<PostDTO> getAllPost(Pageable pageable) {
		return postService.getAllPost(pageable);
	}
	
	@RequestMapping(value = "/onePost" , method = RequestMethod.GET)
	public Post getOnePost(@RequestParam(name = "number") String postNumber) {
		return postService.getOnePost(Integer.parseInt(postNumber));
	}
	
	@RequestMapping(value = "/post/{postNumber}" , method = RequestMethod.DELETE)
	public MessageDTO deletePost(@PathVariable(name = "postNumber") String postNumber , HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		postService.deletePost(postNumber , header);
		
		return MessageDTO.builder()
				.code("1")
				.message("게시글이 삭제되었습니다")
				.build();
	}
	
	@RequestMapping(value = "/post/{postNumber}" , method = RequestMethod.PATCH)
	public MessageDTO modifyPost(@PathVariable(name = "postNumber") String postNumber , @RequestBody PostDTO postData , HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		postService.modifyPost(postData , Integer.parseInt(postNumber) , header);
		
		return MessageDTO.builder()
				.code("1")
				.message("게시글이 수정되었습니다")
				.build();
	}
	
	@RequestMapping(value = "/post/recommendation/{postNumber}" , method = RequestMethod.PATCH)
	public MessageDTO increaseRecommendation(@PathVariable(name = "postNumber") String postNumber , HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		
		if(postService.increaseRecommendation(postNumber , header)) {
			return MessageDTO.builder()
					.code("1")
					.message("해당 게시물을 추천했습니다.")
					.build();
		} else {
			return MessageDTO.builder()
					.code("1")
					.message("이미 추천한 게시물입니다.")
					.build();			
		}
	}
	
	@RequestMapping(value = "/comment/{postNumber}" , method = RequestMethod.POST)
	public MessageDTO addCommentData(@PathVariable(name = "postNumber") String postNumber , 
			HttpServletRequest request , @RequestBody CommentDTO CommentDTO) {
		String header = request.getHeader("Authorization");
		
		postService.addComment(postNumber , CommentDTO , header);
		return MessageDTO.builder()
				.code("1")
				.message("댓글을 등록했습니다.")
				.build();
	}
	
	@RequestMapping(value = "/comment/{commentId}" , method = RequestMethod.DELETE)
	public MessageDTO removeCommentData(@PathVariable(name = "commentId") String commentId , HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		postService.deleteComment(commentId , header);
		return MessageDTO.builder()
				.code("1")
				.message("댓글을 삭제했습니다.")
				.build();
	}
}
