package com.fyp.javaailangchain4j.store;

import com.fyp.javaailangchain4j.domain.ChatHistory;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import jakarta.annotation.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import java.util.LinkedList;
import java.util.List;

/**
 * @Project java-ai-langchain4j
 * @Author 房毅鹏
 * @Create 2026-04-29 19:31:55
 * @Description
 */
@Component
public class MongoChatMemoryStore implements ChatMemoryStore
{
	@Resource
	private MongoTemplate mongoTemplate;
	
	
	/**
	 * 获取指定用户的聊天消息历史记录
	 *
	 * @param memoryId 用户标识符，用于识别需要获取聊天记录的特定用户
	 * @return 返回该用户的聊天消息列表，按时间顺序排列；如果未找到记录则返回null
	 */
	@Override
	public List<ChatMessage> getMessages(Object memoryId)
	{
		Criteria criteria = Criteria.where("memoryId").is(memoryId);
		Query query = new Query(criteria);
		ChatHistory chatHistory = mongoTemplate.findOne(query,ChatHistory.class);
		if(chatHistory == null)
		{
			return new LinkedList<>();
		}
		String content = chatHistory.getContent();
		return ChatMessageDeserializer.messagesFromJson(content);
	}
	
	/**
	 * 更新指定用户的聊天消息历史记录
	 *
	 * @param memoryId 用户标识符，用于识别需要更新聊天记录的特定用户
	 * @param list 要更新的聊天消息列表，包含完整的对话历史记录
	 */
	@Override
	public void updateMessages(Object memoryId,List<ChatMessage> list)
	{
		Criteria criteria = Criteria.where("memoryId").is(memoryId);
		Query query = new Query(criteria);
		Update update = new Update();
		update.set("content", ChatMessageSerializer.messagesToJson(list));
		mongoTemplate.upsert(query, update, ChatHistory.class);
	}
	
	/**
	 * 删除指定用户的聊天消息历史记录
	 *
	 * @param memoryId 用户标识符，用于识别需要删除聊天记录的特定用户
	 */
	@Override
	public void deleteMessages(Object memoryId)
	{
		Criteria criteria = Criteria.where("memoryId").is(memoryId);
		Query query = new Query(criteria);
		mongoTemplate.remove(query, ChatHistory.class);
	}
}
