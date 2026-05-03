package com.fyp.javaailangchain4j.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Project java-ai-langchain4j
 * @Author 房毅鹏
 * @Create 2026-04-29 19:15:24
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("chat_messages")
public class ChatHistory
{
	//唯一标识，映射到MongoDB文档的id字段
	@Id
	private ObjectId messageId;
	
	private String memoryId;
	
	//存储当前聊天记录的json字符串
	private String content;
}
