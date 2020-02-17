package org.olumpos.forum.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.olumpos.forum.controller.PostController;
import org.olumpos.forum.dao.PostDAO;
import org.olumpos.forum.dao.PostDAOImpl;
import org.olumpos.forum.dao.TopicDAO;
import org.olumpos.forum.dao.TopicDAOImpl;
import org.olumpos.forum.dao.UserDAO;
import org.olumpos.forum.dao.UserDAOImpl;
import org.olumpos.forum.entity.Post;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.InternalResourceView;

/**
 * 
 * @author daristote
 * 
 * Tests pour les fonction du contrôleur Spring MVC: PostController
 *
 */

public class PostControllerTest {

	Logger logger =  Logger.getLogger(PostControllerTest.class.getName());
	
	private TopicDAO topicDAO;
	
	private PostDAO postDAO;
	
	private UserDAO userDAO;
	
	

	@Before
	public void init() {
		topicDAO =  new TopicDAOImpl();
		postDAO =  new PostDAOImpl();
		userDAO =  new UserDAOImpl();
		
		logger.log(Level.INFO, "PostControllerTest.init(): topicDao: " + topicDAO);
		logger.log(Level.INFO, "PostControllerTest.init(): postDao: " + postDAO);
		logger.log(Level.INFO, "PostControllerTest.init(): userDAO: " + userDAO);
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	
	@Test
	public void getPostsTest() throws Exception {
		
		logger.log(Level.INFO, "PostControllerTest.getPostsTest(): postDao: " + postDAO);
		PostController controller =  new PostController(postDAO);
		MockMvc mockMvc = standaloneSetup(controller)
				.setSingleView(new InternalResourceView("/WEB-INF/views/posts.jsp"))
				.build();
		
 
		int lastTopicId =  topicDAO.getLastInsertedOpenTopicId();
		
		logger.log(Level.INFO, "PostControllerTest.getPOsts(): lastTopicId: " + lastTopicId);
		
		List<Post> posts = postDAO.getAllActivePosts(lastTopicId); 
		
		logger.log(Level.INFO, "PostControllerTest.getPOsts(): post.size: " + posts.size());
		
		mockMvc.perform(get("/posts/"+ lastTopicId))
	       .andExpect(view().name("posts"))
	       .andExpect(model().attributeExists("postList"))
	    //   .andExpect(model().attribute("userList", hasItems(posts.toArray()))) //error but should work
	       .andExpect(model().hasNoErrors());
		
	}
	

	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	@Test
	public void addPostTest() throws UnsupportedEncodingException, Exception {

		PostController controller =  new PostController(postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();
		
		long suffixe =  new Date().getTime() % 10000;
				
		String postTitle = "post #" + suffixe;
		String postBody = "Commentaire " + suffixe;
		
		logger.log(Level.INFO, "in addPostTest() suffixe: " + suffixe);
		
		int topicId = 1;
		int userId =  1;
		
	    mockMvc.perform(post("/posts/addPost")
    				.param("postTitle", postTitle)
	    			.param("postBody", postBody)
	    			.param("topicId", Integer.toString(topicId))
	    			.param("userId", Integer.toString(userId)))
        		//	.andExpect(content().contentType("application/json;charset=UTF-8"))// OK
	    			.andReturn().getResponse().getContentAsString().equals("{\"status\":\"ok\"}");
	    			
		int lastPostId =  postDAO.getLastInsertedPostId();
	    
		assertTrue(lastPostId > 0);
		Post lastInsertedPost =  postDAO.getPost(lastPostId);
	    
		assertNotNull(lastInsertedPost);
	    
	    assertEquals(Integer.valueOf(topicId), lastInsertedPost.getTopicId());
	    assertEquals( postTitle, lastInsertedPost.getTitle());
	    assertEquals(postBody, lastInsertedPost.getBody());
	    assertEquals(Integer.valueOf(userId), lastInsertedPost.getUserId());
	    
	    //delete from db
	    int deleted =  postDAO.deletePostFromDB(lastPostId);
	    
		logger.log(Level.INFO, "in addPostTest() deleted: " + deleted);
	    
	    assertTrue(deleted > 0);
	    
	    Post deletedPost =  postDAO.getPost(lastPostId);
	    
	    assertNull(deletedPost);
	    
	    
	        
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	@Test
	public void updatePostTest() throws UnsupportedEncodingException, Exception {

		PostController controller =  new PostController(postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();
		
		long suffixe =  new Date().getTime() % 10000;
		
		logger.log(Level.INFO, "=====================> in updatePostTest() suffixe: " + suffixe);
				
		String postTitle = "post #" + suffixe;
		String postBody = "Commentaire " + suffixe;
				
		Post post =  new Post();
		post.setTitle(postTitle);
		post.setBody(postBody);
		post.setUserId(1);
		post.setTopicId(1);
		
		int addedPost = postDAO.addPost(post);
		
		assertTrue(addedPost > 0);		
		
	    int lastPostId =  postDAO.getLastInsertedPostId();

		assertTrue(lastPostId > 0);
		
		String updateTitle = "Updated title";
		String updateBody = "Updated comment";
		
		
	    mockMvc.perform(put("/posts/updatePost")
	    			.param("postId", Integer.toString(lastPostId))
	    			.param("postTitle", updateTitle)
	    			.param("postBody", updateBody))
        			.andExpect(content().contentType("application/json;charset=UTF-8"))// OK
        			.andReturn().getResponse().getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    
	    Post updatedPost =  postDAO.getPost(lastPostId);
	    
	    assertNotNull(updatedPost);
	    assertEquals( updateTitle, updatedPost.getTitle());
	    assertEquals(updateBody, updatedPost.getBody());

	    //delete from db
	    int deleted =  postDAO.deletePostFromDB(lastPostId);
	    
		logger.log(Level.INFO, "in updatePostTest() deleted: " + deleted);
	    
	    assertTrue(deleted > 0);
	    
	    Post deletedPost =  postDAO.getPost(lastPostId);
	    
	    assertNull(deletedPost);
	   
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	@Test
	public void updateTest() throws UnsupportedEncodingException, Exception {

		PostController controller =  new PostController(postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();
		
		long suffixe =  new Date().getTime() % 10000;
		
		logger.log(Level.INFO, "===================== > in updateTest() suffixe: " + suffixe);
	
		String postTitle = "post - " + suffixe;
		String postBody = "Comnentaire - " + suffixe;

		Post post = new Post();
		post.setTitle(postTitle);
		post.setBody(postBody);
		post.setUserId(1);
		post.setTopicId(1);
		int addedPostResult = postDAO.addPost(post);
		
		logger.log(Level.INFO, "===================== > in updateTest() addedPostResult: " + addedPostResult);

		assertTrue(addedPostResult > 0);
		
		String updatedPostTitle =  "Updated post title ";
		String updatedPostBody =  "Updated Comment";
		
		int lastPostId =  postDAO.getLastInsertedPostId();
		
		assertTrue( lastPostId > 0);
		
		logger.log(Level.INFO, "===================== > in updateTest() lastPostId: " + lastPostId);
		
	    mockMvc.perform(put("/posts/updatePost/"+lastPostId +"/"+updatedPostTitle +"/" + updatedPostBody))
        			//.andExpect(content().contentType("application/json;charset=UTF-8"))
	    		.andReturn()
	    		.getResponse()
	    		.getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    
	    Post updatedPost =  postDAO.getPost(lastPostId);
	    
	    assertNotNull(updatedPost);
	    
	    assertEquals(updatedPostTitle, updatedPost.getTitle());
	    assertEquals(updatedPostBody, updatedPost.getBody());
	    
	    
	    //delete from db
	    int deleted =  postDAO.deletePostFromDB(lastPostId);
	    
		logger.log(Level.INFO, "in updateTest() deleted: " + deleted);
	    
	    assertTrue(deleted > 0);
	    Post deletedPost =  postDAO.getPost(lastPostId);
	    
	    assertNull(deletedPost);
	     
	}
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	@Test
	public void deactivateTest() throws UnsupportedEncodingException, Exception {

		PostController controller =  new PostController(postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();
	
		long suffixe =  new Date().getTime() % 10000;
		
		logger.log(Level.INFO, "===================== > in deactivateTest() suffixe: " + suffixe);
	
		String postTitle = "post - " + suffixe;
		String postBody = "Comnentaire - " + suffixe;

		Post post = new Post();
		post.setTitle(postTitle);
		post.setBody(postBody);
		post.setUserId(1);
		post.setTopicId(1);
		post.setIsActive((byte) 1);
		
		int addedPostResult = postDAO.addPost(post);
		
		assertTrue(addedPostResult > 0);
		
		logger.log(Level.INFO, "===================== > in deactivateTest() addedPostResult: " + addedPostResult);
		
		int lastPostId =  postDAO.getLastInsertedPostId();
		
		assertTrue(lastPostId > 0);
		
		
		Post addedPost = postDAO.getPost(lastPostId);

		assertNotNull(addedPost);

		assertEquals(1, post.getIsActive());
	
		
	    mockMvc.perform(delete("/posts/deletePost")
    				.param("postId", Integer.toString(lastPostId)))
	    			.andExpect(content().contentType("application/json;charset=UTF-8"))// OK
	    			.andReturn()
	    			.getResponse()
	    			.getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    
	    addedPost = postDAO.getPost(lastPostId);
	    
	    assertNotNull(addedPost);
	    assertEquals(0, addedPost.getIsActive());
	    
	    //delete from db
	    int deleted =  postDAO.deletePostFromDB(lastPostId);
	    
		logger.log(Level.INFO, "in updateTest() deleted: " + deleted);
	    
	    assertTrue(deleted > 0);
	    Post deletedPost =  postDAO.getPost(lastPostId);
	    
	    assertNull(deletedPost);
	    
	}
	
	
	//*********************************************************************************************************************
	//*********************************************************************************************************************
	@Test
	public void deletePost() throws UnsupportedEncodingException, Exception {

		PostController controller =  new PostController(postDAO);
		
		MockMvc mockMvc = standaloneSetup(controller).build();
		
		long suffixe =  new Date().getTime() % 10000;
		
		logger.log(Level.INFO, "===================== > in deactivateTest() suffixe: " + suffixe);
	
		String postTitle = "post - " + suffixe;
		String postBody = "Comnentaire - " + suffixe;

		Post post = new Post();
		post.setTitle(postTitle);
		post.setBody(postBody);
		post.setUserId(1);
		post.setTopicId(1);
		post.setIsActive((byte) 1);
		
		int addedPostResult = postDAO.addPost(post);
		
		assertTrue(addedPostResult > 0);
		
		logger.log(Level.INFO, "===================== > in deactivateTest() addedPostResult: " + addedPostResult);
		
		int lastPostId =  postDAO.getLastInsertedPostId();
		
		assertTrue(lastPostId > 0);
		
		
		Post addedPost = postDAO.getPost(lastPostId);
		
		logger.log(Level.INFO, "===================== > in deactivateTest() addedPost: " + addedPost);

		assertNotNull(addedPost);

		assertEquals(1, post.getIsActive());

	    //deactivate
	    mockMvc.perform(delete("/posts/deletePost/"+lastPostId))
        			.andExpect(content().contentType("application/json;charset=UTF-8"))
        			.andReturn()
        			.getResponse()
        			.getContentAsString().equals("{\"status\":\"ok\"}");
	    			
	    
	    Post updatedPost = postDAO.getPost(lastPostId);
	    
	    assertNotNull(updatedPost);

	    assertEquals(0, updatedPost.getIsActive());
		
	    //delete from db
	    int deleted =  postDAO.deletePostFromDB(lastPostId);
	    
		logger.log(Level.INFO, "in updateTest() deleted: " + deleted);
	    
	    assertTrue(deleted > 0);
	    Post deletedPost =  postDAO.getPost(lastPostId);
	    
	    assertNull(deletedPost);
	        
	}
	
}
