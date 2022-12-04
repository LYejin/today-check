package com.team.todaycheck.main.service;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.team.todaycheck.main.DTO.CommentDTO;
import com.team.todaycheck.main.DTO.PostDTO;
import com.team.todaycheck.main.entity.Comment;
import com.team.todaycheck.main.entity.Post;
import com.team.todaycheck.main.entity.UserEntity;
import com.team.todaycheck.main.exception.FalsifyTokenException;
import com.team.todaycheck.main.exception.NotAuthorizationException;
import com.team.todaycheck.main.exception.UnknownPostException;
import com.team.todaycheck.main.exception.InvalidateTokenException;
import com.team.todaycheck.main.repository.CommentRepository;
import com.team.todaycheck.main.repository.PostRepository;
import com.team.todaycheck.main.repository.UserRepository;

@Service
@Transactional
public class PostService {
	
	@Autowired PostRepository postRepos;
	@Autowired UserRepository userRepos;
	@Autowired CommentRepository commentRepos;
	
	public void addPost(PostDTO post , String header) {
		String userId = getUserIdFromToken(header);
		UserEntity user = userRepos.findById(userId);
		
		if(user == null) throw new FalsifyTokenException("토큰이 변조되었거나 손상되었습니다.");

		post.setUserId(userId);
		user.addpost(toEntity(post));
	}
	
	public List<PostDTO> getAllPost(Pageable pageable) {
		return postRepos.getAllPost(pageable);
	}
	
	public Post getOnePost(int postnumber) {
		postRepos.updateView(postnumber);
		return postRepos.findByPostKey(postnumber);
	}
	
	public static Post toEntity(PostDTO post) {
		return Post.builder()
				.postKey(post.getPostKey())
				.title(post.getTitle())
				.userId(post.getUserId())
				.description(post.getDescription())
				.thumbnail(post.getThumbnail())
				.build();
	}
	
	public static PostDTO fromEntity(Post post) {
		return PostDTO.builder()
				.postKey(post.getPostKey())
				.title(post.getTitle())
				.userId(post.getUserId())
				.description(post.getDescription())
				.thumbnail(post.getThumbnail())
				.date(post.getDate())
				.build();
	}

	public void deletePost(String postNumber , String header) {
		String userId = getUserIdFromToken(header);
		
		if(postRepos.deleteOnePost(Integer.parseInt(postNumber) , userId) != 1L) {
			throw new NotAuthorizationException("게시물 번호가 잘못되었거나 , 해당 게시글은 작성자만 지울 수 있습니다.");
		}
	}

	public void modifyPost(PostDTO postData , int postNumber , String header) {
		String userId = getUserIdFromToken(header);
		Post post = postRepos.findByPostKey(postNumber , userId);
		
		if(post == null) {
			throw new UnknownPostException("작성자가 다르거나 , 알 수 없는 페이지입니다.");
		}
		post.setDescription(postData.getDescription());
		post.setThumbnail(postData.getThumbnail());
		post.setTitle(postData.getTitle());
	}

	public boolean increaseRecommendation(String postNumber , String header) {
		String userId = getUserIdFromToken(header);
		
		return postRepos.increaseRecommander(Integer.parseInt(postNumber), userId);
	}

	public void addComment(String postNumber, CommentDTO CommentDTO, String header) {
		String userId = getUserIdFromToken(header);
		
		Post result = postRepos.findByPostKey(Integer.parseInt(postNumber));
		result.addComment(Comment.builder()
				.writer(userId)
				.content(CommentDTO.getContent())
				.build());
	}

	public void deleteComment(String commentId , String header) {
		String userId = getUserIdFromToken(header);
		if (commentRepos.deleteComment(Long.parseLong(commentId) , userId) != 1L) {
			throw new InvalidateTokenException("댓글 ID가 잘못되었거나 , 해당 게시글은 작성자만 지울 수 있습니다.");
		};
	}
	
	public static String getUserIdFromToken(String header) {
				// We need a signing key, so we'll create one just for this example. Usually
				// the key would be read from your application configuration instead.
				String[] split_string = header.split("\\.");
		        String base64EncodedBody = split_string[1];
		        Base64 base64Url = new Base64(true);
		        
		        //~~~~~~~~~ JWT Body ~~~~~~~~~
		        String body = new String(base64Url.decode(base64EncodedBody));
		        // System.out.println("JWT Body : "+body);
		        // JWT Body Ex ) {"sub":"thisisid","roles":["ROLE_USER"],"iat":1669686350,"exp":1669688150}
		        
		        JsonParser parser = new JsonParser();
				JsonElement element = parser.parse(body);
				
				return element.getAsJsonObject().get("sub").getAsString();
				// parsing END
	}
}
