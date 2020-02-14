package org.olumpos.forum.dao;

import java.util.List;

import org.olumpos.forum.entity.Post;

public interface PostDAO {
	
	public List<Post> getAllPosts();
	public List<Post> getAllActivePosts();
	public List<Post> getAllPosts(int topicId);
	public List<Post> getAllActivePosts(int topicId);
	public Post getPost(int postId);
	public int getLastInsertedPostId();
	public int getLastInsertedActivePostId();
	public int addPost(Post post);
	public int updatePost(Post post);
	public int activateDeactivatePost(int postId, byte status);
	public int deletePost(int postId);

	
}
