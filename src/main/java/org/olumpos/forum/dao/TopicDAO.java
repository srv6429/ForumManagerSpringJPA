package org.olumpos.forum.dao;

import java.util.List;

import org.olumpos.forum.entity.Post;
import org.olumpos.forum.entity.Topic;
/**
 * 
 * @author daristote
 *
 */

public interface TopicDAO {
	
	public List<Topic> getAllTopics();
	public List<Topic> getAllOpenTopics();
	public Topic getTopic(int id);
	public int getLastInsertedTopicId();
	public int getLastInsertedOpenTopicId();
	public int addTopic(Topic topic);
	public int addTopic(Topic topic, Post post);
	public int updateTopic(int topicId, String title);
	public int updateTopic(Topic topic);
	public int openCloseTopic(int topicId, byte status);	
	public int deleteTopicFromDB(int topicId);
	
}
