package com.fyp.javaailangchain4j.conf;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pinecone.PineconeEmbeddingStore;
import dev.langchain4j.store.embedding.pinecone.PineconeServerlessIndexConfig;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Project java-ai-langchain4j
 * @Author 房毅鹏
 * @Create 2026-05-01 16:14:11
 * @Description
 */
@Configuration
public class PineconeEmbeddingStroeConf
{
	@Value("${pinecone.api-key}")
	private String apiKey;
	@Value("${pinecone.index}")
	private String index;
	@Value("${pinecone.nameSpace}")
	private String nameSpace;
	
	@Resource
	private EmbeddingModel embeddingModel;
	@Bean
	public EmbeddingStore<TextSegment> embeddingStore()
	{
		EmbeddingStore<TextSegment> embeddingStore = PineconeEmbeddingStore.builder()
			.apiKey(apiKey)
			.index(index)//如果指定的索引不存在，将创建一个新的索引
			.nameSpace(nameSpace)//如果指定的名称空间不存在，将创建一个新的名称空间
			.createIndex(PineconeServerlessIndexConfig.builder().cloud("AWS")//指定索引的云提供商为 AWS
				.region("us-east-1")//指定索引所在的 AWS 区域为 us-east-1。
				.dimension(embeddingModel.dimension())//指定索引的向量维度，该维度与 embeddedModel 生成的向量维度相同
				.build())
			.build();
		return embeddingStore;
	}
}
