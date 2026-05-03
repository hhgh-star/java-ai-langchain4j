package com.fyp.javaailangchain4j.controller;

import com.fyp.javaailangchain4j.assistant.Assistant;
import com.fyp.javaailangchain4j.domain.ChatForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import java.awt.*;

/**
 * @Project java-ai-langchain4j
 * @Author 房毅鹏
 * @Create 2026-04-30 16:53:43
 * @Description
 */
@Tag(name = "硅谷小智")
@RestController
@RequestMapping("/xiaozhi")
public class XiaozhiController
{
	@Resource
	private Assistant assistant;
	
	@Operation(summary = "小智")
	@PostMapping(value = "/chat", produces = "text/stream;charset=utf-8")
	public Flux<String> chat(@RequestBody ChatForm chatForm){
		return assistant.chat(chatForm.getMemoryId(),chatForm.getMessage());
	}
}
