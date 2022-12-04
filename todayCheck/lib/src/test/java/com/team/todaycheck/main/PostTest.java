package com.team.todaycheck.main;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.team.todaycheck.main.DTO.CommentDTO;
import com.team.todaycheck.main.DTO.PostDTO;
import com.team.todaycheck.main.controller.PostController;
import com.team.todaycheck.main.entity.Post;
import com.team.todaycheck.main.entity.UserEntity;
import com.team.todaycheck.main.entity.UserEntity.Admin;
import com.team.todaycheck.main.exception.InvalidateTokenException;
import com.team.todaycheck.main.exception.UnknownPostException;
import com.team.todaycheck.main.repository.CommentRepository;
import com.team.todaycheck.main.repository.PostRepository;
import com.team.todaycheck.main.repository.UserRepository;
import com.team.todaycheck.main.service.LoginService;
import com.team.todaycheck.main.service.PostService;
/*
 * assertEquals(result.getCode() , "1");
 * 
 */

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // Test 순서를 제시합니다.
@TestInstance(Lifecycle.PER_CLASS)
@Rollback(value = true)
public class PostTest {

	@Autowired PostController postController;
	@Autowired LoginService loginService;
	@Autowired PostService postService;
	@Autowired UserRepository userRepos;
	@Autowired PostRepository postRepos;
	@Autowired CommentRepository commentRepos;
	@Autowired MockMvc mvc;
	
	private String testUserId = "wfa3fg51qqeRTf351wD";
	private String testUserPw = "FgG34lcp091xZCbnfaw";
	private int postNumber = 0;
	/*
	 *  testerHeader1 은 testUserId 의 토큰입니다.
	 *  testerHeader2 는 testUserId 가 아닌 임의의 Access 토큰입니다.
	 */
	private String testerHeader1 = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3ZmEzZmc1MXFxZVJUZjM1MXdEIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTY2OTczMzc1OSwiZXhwIjoxNjY5NzM1NTU5fQ.ZcO1cZIy1S-Rx3_N6s7Zh1U4MxcjEcTEy83fQsFIG2A";
	private String testerHeader2 = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmd2dkIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTY2OTczMzc1OSwiZXhwIjoxNjY5NzM1NTU5fQ.LS1Eb5SZpPcE4fpahedKXm4hjb6SHZZlWqVHO-VGUf8";
	
	public void addPostAndReturnNumber() {
		PostDTO post = PostDTO.builder()
				.title("titleName")
				.userId(testUserId)
				.description("descData")
				.thumbnail("thumbnail")
				.build();
		
		postService.addPost(post , testerHeader1);
		postNumber = postRepos.getPostKeyMaxValue();
	}
	
	@Test
	@Order(1)
	@DisplayName("새 Post 등록")
	public void addNewPost() {
		if(userRepos.findById(testUserId) == null) {
			UserEntity user = UserEntity.builder()
					.admin(Admin.GENERAL)
					.id(testUserId)
					.password(testUserPw)
					.roles(Collections.singletonList("ROLE_USER"))
					.build();
			
			userRepos.save(user);
		}
		
		PostDTO post = PostDTO.builder()
				.title("titleName")
				.userId(testUserId)
				.description("descData")
				.thumbnail("thumbnail")
				.build();
		
		//Assertions.assertThatCode(() -> postService.addPost(post , testerHeader1)).doesNotThrowAnyException();
		Assertions.assertDoesNotThrow(() -> postService.addPost(post , testerHeader1));
		// 예외가 발생하지 않았을 떄 저장된 post키를 반환
		postNumber = postRepos.getPostKeyMaxValue();
	}
	
	@Test
	@Order(2)
	@DisplayName("Post 리스트 가져오기")
	public void getAllPost() {
		PageRequest pageRequest = PageRequest.of(1, 1, Sort.by(Sort.Direction.DESC, "postKey"));
		//Assertions.assertThatCode(() -> postController.getAllPost(pageRequest)).doesNotThrowAnyException();
		Assertions.assertDoesNotThrow(() -> postController.getAllPost(pageRequest));
	}
	
	@Test
	@Order(3)
	@DisplayName("특정 포스트 가져오기")
	public void getPostOne() {
		addPostAndReturnNumber();
		Post result = postService.getOnePost(postNumber);
		Assertions.assertNotNull(result);
	}
	
	@Test
	@Order(4)
	@DisplayName("동일 작성자가 Post 수정")
	public void sameAuthorModifyPost() {
		addPostAndReturnNumber();
		PostDTO modifiedPost = PostDTO.builder()
				.title("modifyTitleName")
				.description("modifyDescriptionData")
				.thumbnail("modifiyThumbnail")
				.build();
		
		Assertions.assertDoesNotThrow(() -> postService.modifyPost(modifiedPost, postNumber, testerHeader1));
	}
	
	@Test
	@Order(5)
	@DisplayName("다른 작성자가 Post 수정 거부")
	public void otherAuthorModifyPost() {
		addPostAndReturnNumber();
		PostDTO modifiedPost = PostDTO.builder()
				.title("modifyTitleName")
				.description("modifyDescriptionData")
				.thumbnail("modifiyThumbnail")
				.build();
		
		Assertions.assertThrows(UnknownPostException.class , () -> postService.modifyPost(modifiedPost, postNumber , testerHeader2));
	}
	
	@Test
	@Order(6)
	@DisplayName("Post 삭제")
	public void deletePost() {
		addPostAndReturnNumber();
		Assertions.assertDoesNotThrow(() -> postService.deletePost(Integer.toString(postNumber) , testerHeader1));
	}
	
	@Test
	@Order(7)
	@DisplayName("Comment 등록")
	public void addComment() {
		addPostAndReturnNumber();
		CommentDTO comment = CommentDTO.builder()
				.content(testUserId)
				.build();
		Assertions.assertDoesNotThrow(() -> postService.addComment(Integer.toString(postNumber) , comment , testerHeader1)) ;
	}
	
	@Test
	@Order(8)
	@DisplayName("등록자가 아닌 다른 사용자가 Comment 삭제")
	public void removeCommitFromAnotherUser() {
		addPostAndReturnNumber();
		CommentDTO comment = CommentDTO.builder()
				.content(testUserId)
				.build();
		
		postService.addComment(Integer.toString(postNumber) , comment , testerHeader1);
		Long commentKeyValue = commentRepos.getCommentIdKeyMaxValue();
		
		Assertions.assertThrows(InvalidateTokenException.class , () -> postService.deleteComment(Long.toString(commentKeyValue) , testerHeader2));
	}
	
	@Test
	@Order(8)
	@DisplayName("동일 사용자가 Comment 삭제")
	public void removeCommitFromSameUser() {
		addPostAndReturnNumber();
		CommentDTO comment = CommentDTO.builder()
				.content(testUserId)
				.build();
		
		postService.addComment(Integer.toString(postNumber) , comment , testerHeader1);
		Long commentKeyValue = commentRepos.getCommentIdKeyMaxValue();
		
		Assertions.assertDoesNotThrow(() -> postService.deleteComment(Long.toString(commentKeyValue) , testerHeader1));
	}
	
	@Test
	@Order(9)
	@DisplayName("인증이 필요한 요청에 존재하지 않는 헤더 예외 발생")
	public void notexistHeaderException() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/post/post").accept(MediaType.APPLICATION_JSON))
    	.andExpect(status().is3xxRedirection());
	}
}
