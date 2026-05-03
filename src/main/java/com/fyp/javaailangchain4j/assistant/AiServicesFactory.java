package com.fyp.javaailangchain4j.assistant;

import com.fyp.javaailangchain4j.store.MongoChatMemoryStore;
import com.fyp.javaailangchain4j.tools.AppointmentTools;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Project java-ai-langchain4j
 * @Author 房毅鹏
 * @Create 2026-04-29 18:15:30
 * @Description
 */
@Configuration
public class AiServicesFactory
{
	//@Resource
	//private ChatModel qwenChatModel;
	
	@Resource
	private StreamingChatModel qwenStreamingChatModel;
	
	@Resource
	private MongoChatMemoryStore mongoChatMemoryStore;
	
	@Resource
	private AppointmentTools appointmentTools;
	
	@Resource
	private ContentRetriever contentRetriever;
	@Bean
	public Assistant assistant()
	{
		ChatMemoryProvider memoryProvider = memoryId -> MessageWindowChatMemory.builder()
			.id(memoryId)
			.maxMessages(20)
			.chatMemoryStore(mongoChatMemoryStore)
			.build();
		Assistant assistant = AiServices.builder(Assistant.class)
			//.chatModel(qwenChatModel)
			.streamingChatModel(qwenStreamingChatModel)
			.chatMemoryProvider(memoryProvider)
			.tools(appointmentTools)
			.contentRetriever(contentRetriever)
			.build();
		return assistant;
	}
}
