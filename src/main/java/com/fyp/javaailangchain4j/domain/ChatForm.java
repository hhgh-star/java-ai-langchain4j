package com.fyp.javaailangchain4j.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project java-ai-langchain4j
 * @Author 房毅鹏
 * @Create 2026-04-30 16:55:56
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatForm
{
	private Long memoryId;//对话id
	
	private String message;//用户问题
}
