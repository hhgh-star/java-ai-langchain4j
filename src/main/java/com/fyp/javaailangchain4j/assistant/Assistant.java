package com.fyp.javaailangchain4j.assistant;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import reactor.core.publisher.Flux;

/**
 * @Project java-ai-langchain4j
 * @Author 房毅鹏
 * @Create 2026-04-29 18:12:11
 * @Description
 */
public interface Assistant
{
	@SystemMessage(fromResource = "xiaozhi-prompt-templete.txt")
	Flux<String> chat(@MemoryId Long memoryId,@UserMessage String message);
}
